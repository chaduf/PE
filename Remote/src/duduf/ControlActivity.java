package duduf;

import duduf.udpclient.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class ControlActivity extends Activity /*implements SensorEventListener*/ {
	
	private final long SEND_RATE = 100;
	private UdpClient mClient;
	
//	private Quaternion mStartingOrientationInverse;
//	private Quaternion mOrientation;
//	private SensorManager mSensorManager;

//	/* Orientation values*/
//	private float[] mGravity = null;
//	private float[] mMagneticField = null;
	
	private boolean isRunning = false;
	
	private OrientationListener mOrientationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		
		try {
			String ipAddress = this.getIntent().getStringExtra(MainActivity.IP_ADDRESS);
			int port = this.getIntent().getIntExtra(MainActivity.PORT, 11000);
			Quaternion basisRef = this.getIntent().getParcelableExtra(MainActivity.BASIS_REF);
			mClient = new UdpClient(ipAddress, port);
			mOrientationListener = new OrientationListener(this, SensorManager.SENSOR_DELAY_GAME, basisRef);
			//mOrientationListener = new OrientationListener(this, SensorManager.SENSOR_DELAY_GAME);
			mClient.start(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final Handler handler = new Handler();
		
		Runnable sendStateRunnable = new Runnable () {

			@Override
			public void run() {
				String jsonText;
				OrientationCommand oCommand = new OrientationCommand(mOrientationListener.getOrientation());
				//Quaternion Q = (mOrientationListener.getOrientation());
				//OrientationCommand oCommand = new OrientationCommand(Q.getMagnitude());
				jsonText = oCommand.getJson().toString();	
				mClient.sendPacket(jsonText);
				
				///*
				//Used to text measurements
				TextView tv = (TextView) findViewById(R.id.hello);
				tv.setText(jsonText);
				//tv.setText(mStartingOrientationInverse.getW() + "\n" + mStartingOrientationInverse.getX() +"\n" + mStartingOrientationInverse.getY() + "\n" + mStartingOrientationInverse.getZ());
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
	protected void onStart() {
		super.onStart();
		mOrientationListener.startListening();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mOrientationListener.stopListening();
	}

	@Override
	protected void onDestroy() {
		isRunning = false;
		mClient.closeConnection();
		super.onDestroy();
	}
}
