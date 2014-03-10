/**
 * 
 */
package duduf;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * @author Charles-Henry
 *
 */
public class OrientationListener implements SensorEventListener {
	
	private float[] mGravity;
	private float[] mMagneticField;
	private SensorManager mSensorManager;
	private int mSensorDelay;
	
	private Quaternion mOrientation;
	private Quaternion mInitOrientation;

	/**
	 * Constructor
	 * 
	 * @param c: The context of the application
	 */
	public OrientationListener(Context c,int sensorDelay) {
		mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		mOrientation = new Quaternion(1,0,0,0);
		mInitOrientation = new Quaternion(1,0,0,0);
		mSensorDelay = sensorDelay;
	}
	
	/**
	 * 
	 * @param c: The context of the application
	 * @param initOrientation: The initial orientation, <br>
	 * Used as a basis for the orientation measurements  
	 */
	public OrientationListener(Context c, int sensorDelay, Quaternion initOrientation) {
		mSensorManager = (SensorManager) c.getSystemService(Context.SENSOR_SERVICE);
		mOrientation = new Quaternion(1,0,0,0);
		mInitOrientation = initOrientation;
	}
	
	/**
	 * Starts the orientation listenning
	 */
	public void startListening() {
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	/**
	 * Stops the orientaion listening
	 */
	public void stopListening () {
		mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
	}
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] rotationMatrix = new float[9];
		float[] inclinaison = new float[9];
		float alpha = 0.8f;

		try {
			// gravity init
			if (mGravity == null){
				mGravity = new float[3];
			}
			
			//Magnetic field init
			if (mMagneticField == null){
				mMagneticField = new float[3];
			}
			
			// Getting gravity
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				// Isolate the force of gravity with the low-pass filter.
				mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0];
				mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1];
				mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2];
			}
			
			// Getting Magnetic field
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
				mMagneticField[0] = event.values[0];
				mMagneticField[1] = event.values[1];
				mMagneticField[2] = event.values[2];
			}
			
			// Computing rotation matrix of the phone
			SensorManager.getRotationMatrix(rotationMatrix, inclinaison, mGravity, mMagneticField);
			
			// converting rotation matrix to a quaternion and setting the orientation
			mOrientation = (mInitOrientation.getInverse()).multiply(new Quaternion(rotationMatrix));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// getters and setters
	public Quaternion getOrientation() {
		return mOrientation;
	}
	
	public Quaternion getInitOrientation() {
		return mInitOrientation;
	}

	public void setInitOrientation(Quaternion initOrientation) {
		this.mInitOrientation = initOrientation;
	}

	
}
