package edu.gatech.ubicomp.motionauth;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.MotionEvent;



//Service class, need to be created and called from main activity
public class MonitoringService extends Service implements SensorEventListener{
 
	private static final String TAG = "MobiAuth";
	//private boolean mStarted = false;
	private BroadcastReceiver mReceiver = new ScreenReceiver();
	private Sensor mLinAcclSensor, mGyroSensor, mMagSensor, mOrientSensor, mAcclSensor;
	private SensorManager mSensorManager;
	private FileWriter mLinAcclWriter, mAcclWriter, mGyroWriter, mMagWriter, mOrientWriter;
	private File dir;
	private int count;
	private boolean toWrite;
	
	private class Monitor implements Runnable {
		@Override
		public void run() {
//			if (!mStarted) {
//				return;
//			}
		}
	}
	// retrieve broadcast event.
	public class ScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				Log.w(TAG, "Screen Off Detected");
				System.out.println("************Screen off");
				count++;
				try {
					closeFiles();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				toWrite = false;	
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				Log.w(TAG, "Screen On Detected");
				System.out.println("************Screen on");
				try {
					initializeFiles();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				toWrite = true;
			} else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
				
			}
		}
	}
	
	@Override
	public void onCreate() {	
		Log.v("service", "created");	
		super.onCreate();
		this.startService();
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if(mLinAcclSensor == null) mLinAcclSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		if(mAcclSensor == null) mAcclSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(mOrientSensor ==null) mOrientSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		if(mGyroSensor ==null) mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		if(mMagSensor ==null) mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		mSensorManager.registerListener(this, mLinAcclSensor, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mAcclSensor, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mOrientSensor, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, mMagSensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		android.content.IntentFilter filter = new android.content.IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_POWER_CONNECTED);
		filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
		registerReceiver(mReceiver, filter);
		
		
		count = 0;
		dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/motionAuth");
		if(dir.exists())
			dir.delete();
		dir.mkdirs();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();	

	}

	private void start() {
		//mStarted = true;
	}

	private void stop() {
		//mStarted = false;
	}

	private void startService() {
		// setup acclerometer sensor at the very begining
		startLogging();		
	}
	
	private void initializeFiles() throws IOException {
		mLinAcclWriter = new FileWriter(new File(dir, "mLinAcclSensor_" + count + ".txt"), false);
		mLinAcclWriter.write("Timestamp LinAccl_X LinAccl_Y LinAccl_Z\n");
		mAcclWriter = new FileWriter(new File(dir, "mAcclSensor_" + count + ".txt"), false);
		mAcclWriter.write("Timestamp Accl_X Accl_Y Accl_Z\n");
		mGyroWriter = new FileWriter(new File(dir, "mGyroSensor_" + count + ".txt"), false);
		mGyroWriter.write("Timestamp Gyro_X Gyro_Y Gyro_Z\n");
		mMagWriter = new FileWriter(new File(dir, "mMagSensor_" + count + ".txt"), false);
		mMagWriter.write("Timestamp Mag_X Mag_Y Mag_Z\n");
		mOrientWriter = new FileWriter(new File(dir, "mOrientSensor_" + count + ".txt"), false);
		mOrientWriter.write("Timestamp Orient\n");
	}
	private void closeFiles() throws IOException {
		mLinAcclWriter.close();
		mAcclWriter.close();
		mGyroWriter.close();
		mMagWriter.close();
		mOrientWriter.close();
	}
 
 /**
  *  Start register those sensors, require only once registration. 
  */
 public void startLogging(){	
		Handler mHandler = new Handler(Looper.getMainLooper());			    
	       mHandler.post(new Runnable() {
	          public void run() {
     	    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
     	    PowerManager.WakeLock sWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LoggingWakelock");
	  			if((sWakeLock!=null) && (sWakeLock.isHeld()==false)) 
	  				sWakeLock.acquire();  	
	  			
	  			//if(sWakeLock.isHeld()) sWakeLock.release();	        	
	        }
	    });
 	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (toWrite) {
			long curTime = System.currentTimeMillis();
			long eventTime = event.timestamp;
			
			switch (event.sensor.getType()) {
				case Sensor.TYPE_LINEAR_ACCELERATION: {
					
					try {
						mLinAcclWriter.write(curTime + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				case Sensor.TYPE_ACCELEROMETER: {
					try {
						mAcclWriter.write(curTime + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				case Sensor.TYPE_GYROSCOPE: {
					try {
						mGyroWriter.write(curTime + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				case Sensor.TYPE_MAGNETIC_FIELD: {
					try {
						mMagWriter.write(curTime + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				case Sensor.TYPE_ORIENTATION: {
					try {
						mOrientWriter.write(curTime + "," + event.values[0] + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		//Log.d("time", String.valueOf(curTime));
		}
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
