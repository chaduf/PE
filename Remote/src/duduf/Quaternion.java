/**
 * 
 */
package duduf;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Charles-Henry
 *
 *	
 */
public class Quaternion implements Parcelable{
	/**
	 * Scalar component
	 */
	private float mW;
	/**
	 * First vector component
	 */
	private float mX;
	/**
	 * Second vector component
	 */
	private float mY;
	/**
	 * Third vector component
	 */
	private float mZ;
	
	/**
	 * Constructor : the instantiated quaternion would be written:<br>
	 *  Q = w + x.i + y.j + z.k <br>
	 *  where i² = j² = k² = i.j.k = -1
	 * 
	 * @param w 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Quaternion(float w, float x, float y, float z){
		mW = w;
		mX = x;
		mY = y;
		mZ = z;
	}
	
	public Quaternion(float[] quaternionOrMatrix) throws Exception {
		//float w4;
		float r = 0;
		
		if (quaternionOrMatrix.length == 4){
			// the argument is a quaternion
			mW = quaternionOrMatrix[0];
			mX = quaternionOrMatrix[1];
			mY = quaternionOrMatrix[2];
			mZ = quaternionOrMatrix[3];
		}
		else if (quaternionOrMatrix.length == 9) { 
			// the argument is a matrix		
			mW = ( quaternionOrMatrix[0] + quaternionOrMatrix[4] + quaternionOrMatrix[8] + 1.0f) / 4.0f;
			mX = ( quaternionOrMatrix[0] - quaternionOrMatrix[4] - quaternionOrMatrix[8] + 1.0f) / 4.0f;
			mY = (-quaternionOrMatrix[0] + quaternionOrMatrix[4] - quaternionOrMatrix[8] + 1.0f) / 4.0f;
			mZ = (-quaternionOrMatrix[0] - quaternionOrMatrix[4] + quaternionOrMatrix[8] + 1.0f) / 4.0f;
			
			if(mW < 0.0f) mW = 0.0f;
			if(mX < 0.0f) mX = 0.0f;
			if(mY < 0.0f) mY = 0.0f;
			if(mZ < 0.0f) mZ = 0.0f;
			
			mW = (float)Math.sqrt(mW);
			mX = (float)Math.sqrt(mX);
			mY = (float)Math.sqrt(mY);
			mZ = (float)Math.sqrt(mZ);
			
			if(mW >= mX && mW >= mY && mW >= mZ) {
			    mW *= +1.0f;
			    mX *= sign(quaternionOrMatrix[7] - quaternionOrMatrix[5]);
			    mY *= sign(quaternionOrMatrix[2] - quaternionOrMatrix[6]);
			    mZ *= sign(quaternionOrMatrix[3] - quaternionOrMatrix[1]);
			} else if(mX >= mW && mX >= mY && mX >= mZ) {
			    mW *= sign(quaternionOrMatrix[7] - quaternionOrMatrix[5]);
			    mX *= +1.0f;
			    mY *= sign(quaternionOrMatrix[3] + quaternionOrMatrix[1]);
			    mZ *= sign(quaternionOrMatrix[2] + quaternionOrMatrix[6]);
			} else if(mY >= mW && mY >= mX && mY >= mZ) {
			    mW *= sign(quaternionOrMatrix[2] - quaternionOrMatrix[6]);
			    mX *= sign(quaternionOrMatrix[3] + quaternionOrMatrix[1]);
			    mY *= +1.0f;
			    mZ *= sign(quaternionOrMatrix[7] + quaternionOrMatrix[5]);
			} else if(mZ >= mW && mZ >= mX && mZ >= mY) {
			    mW *= sign(quaternionOrMatrix[3] - quaternionOrMatrix[1]);
			    mX *= sign(quaternionOrMatrix[6] + quaternionOrMatrix[2]);
			    mY *= sign(quaternionOrMatrix[7] + quaternionOrMatrix[5]);
			    mZ *= +1.0f;
			} else {
				throw new Exception("Coding Error !");
			}
			
			r = norm(mW, mX, mY, mZ);
			mW /= r;
			mX /= r;
			mY /= r;
			mZ /= r;
		}
		else
			throw new Exception("Argument array size is wrong");
	}
	
	public Quaternion(Parcel in){
		mW = in.readFloat();
		mX = in.readFloat();
		mY = in.readFloat();
		mZ = in.readFloat();
	}
	
	
	//Utilities functions
	public Quaternion add(Quaternion Q){
		return new Quaternion(mW + Q.getW(), mX + Q.getX(), mY + Q.getY(), mZ + Q.getZ());
	}
	
	public Quaternion multiply(Quaternion Q){
		return new Quaternion(	mW*Q.getW() - mX*Q.getX() - mY*Q.getY() - mZ*Q.getZ(),
								mW*Q.getX() + Q.getW()*mX + mY*Q.getZ() - mZ*Q.getY(),
								mW*Q.getY() + Q.getW()*mY + mZ*Q.getX() - mX*Q.getZ(),
								mW*Q.getZ() + Q.getW()*mZ + mX*Q.getY() - mY*Q.getX());
	}
	
	//getters and setters
	
	public float getW() {
		return mW;
	}

	public float getX() {
		return mX;
	}

	public float getY() {
		return mY;
	}

	public float getZ() {
		return mZ;
	}

	public void setZ(float mZ) {
		this.mZ = mZ;
		
	}
	
	public void setW(float mW) {
		this.mW = mW;
	}

	public void setX(float mX) {
		this.mX = mX;
	}

	public void setY(float mY) {
		this.mY = mY;
	}

	public float getMagnitude() {
		return (float) Math.sqrt(getSquareMagnitude());
	}
	
	public float getSquareMagnitude () {
		return mW*mW + mX*mX + mY*mY + mZ*mZ;
	}

	public Quaternion getInverse () {
		float m2 = this.getSquareMagnitude();
		return new Quaternion(mW/m2, -mX/m2, -mY/m2, -mZ/m2);
	}
	
	public Quaternion getConjugate () {
		return new Quaternion(mW, -mX, -mY, -mZ);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		dest.writeFloat(mW);
		dest.writeFloat(mX);
		dest.writeFloat(mY);
		dest.writeFloat(mZ);
	}
	
	public static final Parcelable.Creator<Quaternion> CREATOR = new Parcelable.Creator<Quaternion>()
	{
	    @Override
	    public Quaternion createFromParcel(Parcel source)
	    {
	        return new Quaternion(source);
	    }

	    @Override
	    public Quaternion[] newArray(int size)
	    {
		return new Quaternion[size];
	    }
	};
	
	private float sign (float x){
		return (x >= 0.0f) ? +1.0f : -1.0f;
	}
	private float norm (float w, float x, float y, float z){
		return (float)Math.sqrt(w*w + x*x + y*y + z*z);
	}
}
