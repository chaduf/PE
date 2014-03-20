package duduf;

import duduf.udpclient.R;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.app.Activity;
import android.graphics.Canvas;
import android.hardware.SensorManager;

public class ControlActivity extends Activity {
	
	private final long SEND_RATE = 50;
	private final long DISPLAY_RATE = 10;
	private UdpClient mClient;
	
	private boolean isRunning = false;
	private boolean isScreenActive = false;
	
	private OrientationListener mOrientationListener;
	private TouchHandler mTouchHandler;
	private RelativeLayout mMainView;
	private TouchCanvas mCanvas;
	//private View mScreen;
	
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
			mMainView = (RelativeLayout) findViewById(R.id.touchView);
			mCanvas = (TouchCanvas) findViewById(R.id.touchCanvas);
			mMainView.setOnTouchListener(mTouchHandler);
			mCanvas.setState(mTouchHandler.getState());
			mClient.start();
			startTasks();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mOrientationListener.startListening();
		isScreenActive = true;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mOrientationListener.stopListening();
		isScreenActive = false;
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
		
		Runnable refreshCanvas = new Runnable () {
			@Override
			public void run() {
				if (isRunning){
					handler.postDelayed(this, DISPLAY_RATE);
				}
				mCanvas.postInvalidate();
			}
		};
		
		isRunning = true;
		handler.postDelayed(sendOrientationTask, 0);
		handler.postDelayed(sendTouchTask, 10);
		handler.postDelayed(refreshCanvas, 20);
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
