package duduf;

import duduf.udpclient.R;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.app.Activity;
import android.hardware.SensorManager;

public class ControlActivity extends Activity {
	
	private final long SEND_RATE = 50;
	private UdpClient mClient;
	
	private boolean isRunning = false;
	
	private OrientationListener mOrientationListener;
	private TouchHandler mTouchHandler;
	
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
			mTouchHandler = new TouchHandler();
			View mainView = (View) findViewById(R.id.touchView);
			mainView.setOnTouchListener(mTouchHandler);
			mClient.start(); 
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		startTasks();
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
	
	//private functions
	private void startTasks(){
		final Handler handler = new Handler();
		
		Runnable sendOrientationTask = new Runnable () {
			@Override
			public void run() {
				if (isRunning){
					handler.postDelayed(this, SEND_RATE);
				}
				sendOrientation();
			}
		};
		
		Runnable sendTouchTask = new Runnable () {
			@Override
			public void run() {
				if (isRunning){
					handler.postDelayed(this, SEND_RATE);
				}
				sendTouch();
			}
		};
		
		isRunning = true;
		handler.postDelayed(sendOrientationTask, 0);
		handler.postDelayed(sendTouchTask, 10);
	}
	
	private void sendOrientation (){
		String jsonText;
		OrientationCommand oCommand = new OrientationCommand(mOrientationListener.getOrientation());
		jsonText = oCommand.getJson().toString();
		mClient.sendPacket(jsonText);
	}
	
	private void sendTouch (){
		String jsonText;
		TouchCommand tCommand = new TouchCommand(mTouchHandler.getState());
		jsonText = tCommand.getJson().toString();
		mClient.sendPacket(jsonText);
	}
}
