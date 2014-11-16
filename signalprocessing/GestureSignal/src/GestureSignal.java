import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GestureSignal {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String location = "/Users/weirenwang/Dropbox (MIT)/MotionAuth/Data/Aug30th/AutoSegmentData/all.txt";
	    BufferedReader br = new BufferedReader(new FileReader(new File(location)));
	    String s;
	    while((s = br.readLine()) != null){
	    	String strReplace = s.replaceAll("AlanZhang", "weirenwang").replaceAll("Dropbox", "Dropbox (MIT)");
	    	String[] splitStr = strReplace.split(" ");
	    	removeLine(splitStr[0] + " " + splitStr[1], Long.valueOf(splitStr[2]).longValue(),Long.valueOf(splitStr[3]).longValue());
	    }
	}
	
	public static void removeLine(String f, long toRemoveH, long toRemoveT) throws IOException {

	    File tmp = File.createTempFile("tmp", "");

	    BufferedReader br = new BufferedReader(new FileReader(f));
	    BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));

	    String str;
	    str = br.readLine();//skip header
	    
	    while (null != (str = br.readLine())){
	    	String[] splitStr = str.split(",");
	    	long timeStamp = Long.valueOf(splitStr[0]).longValue();
	    	if(timeStamp < toRemoveH || timeStamp > toRemoveT) continue;
	    	else{
	    		bw.write(String.format("%s%n", str));
	    	}
	    }

	    br.close();
	    bw.close();

	    File oldFile = new File(f);
	    if (oldFile.delete())
	        tmp.renameTo(oldFile);

	}
}
