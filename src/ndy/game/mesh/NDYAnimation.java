package ndy.game.mesh;

import ndy.game.math.NDYQuaternion;

public class NDYAnimation {
	private String mName;
	private NDYQuaternion [] mTransforms;
	private float [] mTimes;
	private boolean mLoop = true;
	
	public NDYAnimation(String name, NDYQuaternion [] transforms, float [] times) {
		mName = name;
		mTransforms = transforms;
		mTimes = times;
	}
	
	public void computeTransform(NDYQuaternion q, float t) {
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
	
	public void computeTransform(NDYQuaternion q, int ta, int tb, float t) {
		NDYQuaternion.slerp(q, mTransforms[ta], mTransforms[tb], t);
	}
	
	public String getName() {
		return mName;
	}
}
