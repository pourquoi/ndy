package ndy.game.mesh;

import ndy.game.math.Quaternion;

public class NDYAnimation {
	private String mName;
	private Quaternion [] mTransforms;
	private float [] mTimes;
	private boolean mLoop = true;
	
	public NDYAnimation(String name, Quaternion [] transforms, float [] times) {
		mName = name;
		mTransforms = transforms;
		mTimes = times;
	}
	
	public void computeTransform(Quaternion q, float t) {
		int ta=0, tb=mTimes.length-1;
		float mint=mTimes[ta],maxt=mTimes[tb];
		
		if(mLoop) {
			int r = (int)(t/maxt);
			t -= maxt*r;
		}
		
		for(int i=0; i<mTimes.length; i++) {
			float ct = mTimes[i];
			if(t-ct<mint && t-ct>=0f) {mint=ct; ta=i;}
			if(ct-t>maxt) {maxt=ct; tb=i;}
		}
		
		computeTransform(q, ta, tb, (t-mint)/(maxt-mint));
	}
	
	public void computeTransform(Quaternion q, int ta, int tb, float t) {
		Quaternion.slerp(q, mTransforms[ta], mTransforms[tb], t);
	}
	
	public String getName() {
		return mName;
	}
}
