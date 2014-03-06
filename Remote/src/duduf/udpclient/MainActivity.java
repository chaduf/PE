package duduf.udpclient;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private final long SEND_RATE = 20;
	private UdpClient mClient;
	
	private float[] mOrientation;
	private SensorManager mSensorManager;

	/* Orientation values*/
	private float[] mGravity = null;
	private float[] mMagneticField = null;
	
	private boolean isRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Setting up sensors
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
		mOrientation = new float[4];
		
		try {
			String ipAddress = "192.168.174.1";
			int port = 11000;
			mClient = new UdpClient(ipAddress, port);
			mClient.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final Handler handler = new Handler();
		
		Runnable sendStateRunnable = new Runnable () {

			@Override
			public void run() {
				String jsonText;
				OrientationCommand oCommand = new OrientationCommand(mOrientation);
				jsonText = oCommand.getJson().toString();	
				mClient.sendPacket(jsonText);
				
				///*
				//Used to text measurements
				TextView tv = (TextView) findViewById(R.id.hello);
				tv.setText(jsonText);
				//*/
				
				if (isRunning){
					handler.postDelayed(this, SEND_RATE);
				}
			}
		};
		
		isRunning = true;
		
		handler.postDelayed(sendStateRunnable, 0);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	protected void onDestroy() {
		mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		mSensorManager.unregisterListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
		
		isRunning = false;
		mClient.closeConnection();
		super.onDestroy();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		initSensor();
		
		updateOrientation(event);		
	}
	
	/**
	 * Updates mOrientation variable
	 * 
	 * @param event
	 */
	private void updateOrientation(SensorEvent event){
		//TODO Signal processing
		float[] rotationMatrix = new float[9];;
		float[] inclinaison = new float[9];
		float alpha = 0.8f;
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
			// Isolate the force of gravity with the low-pass filter.
			mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0];
			mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1];
			mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2];
		}
		
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
			mMagneticField[0] = event.values[0];
			mMagneticField[1] = event.values[1];
			mMagneticField[2] = event.values[2];
		}
		
		SensorManager.getRotationMatrix(rotationMatrix, inclinaison, mGravity, mMagneticField);
		OrientationCommand.convertMatrixToQuaternion(mOrientation, rotationMatrix);
	}
	
	/**
	 * Initializes the measured values
	 */
	private void initSensor (){
		// gravity init
		if (mGravity == null){
			mGravity = new float[3];
		}
		
		//Magnetic field init
		if (mMagneticField == null){
			mMagneticField = new float[3];
		}
	}
}
