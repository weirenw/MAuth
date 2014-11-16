import math, glob, os, numpy
def read(filename):
	binnum = 20
	gyroRange = [-2.0,2.0]
	slot = (gyroRange[1]-gyroRange[0])/binnum
	ans = [0] * binnum
	target = 3 #get x value
	with open(filename, 'r') as fin:
		data = fin.read().splitlines(True)
	with open(filename,'w') as fout:
		fout.writelines(data[1:])

	for line in open(filename):
		columns = line.split(',')
		if len(columns) >= target:
			x = (float(columns[target])-gyroRange[0])/slot
			print x
			x=math.trunc(x)
			print x
			if x-1>=binnum-1:
				ans[binnum-1]+=1
			else:
				ans[x-1]+=1
	
	print normalize(ans)
	return normalize(ans)
def normalize(v):
	norm=numpy.linalg.norm(v)
	if norm==0:
		return v
	return v/norm

def read_write_files(path, dest):
	print path
	with open(dest+'.txt', 'wt') as wf:
		for filename in glob.glob(os.path.join(path, 'gyro*')):
			ans = read(filename)
			print '---------'
			print ans
			s = "" 
			for item in ans:
				s+=str(item)+","
			s = s[:-1]
			wf.write(s+"\n")
		wf.close()


if __name__=="__main__":
	folder = '/Users/weirenwang/Dropbox (MIT)/MotionAuth/Data/20141110/JeffreyData/Weiren/'
	dest = '/Users/weirenwang/Dropbox (MIT)/MotionAuth/Data/20141110/JeffreyData/WeirenResult/'
	for subdir, dirs, files in os.walk(folder):
		for item in dirs:
			read_write_files(folder+item, dest+item[-4:])	
