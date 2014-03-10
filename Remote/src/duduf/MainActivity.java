package duduf;


import duduf.udpclient.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	public static final String IP_ADDRESS = "duduf.ipAddress";
	public static final String PORT = "duduf.port";
	public static final String BASIS_REF = "duduf.basisRef";
	
	
	private OrientationListener mOrientationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mOrientationListener = new OrientationListener(this, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
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
	
	public void onGoButtonClick (View v) {
		Intent intent = new Intent(this, ControlActivity.class);
		String ipAddress;
		int port = 11000;
		Quaternion sumQ = new Quaternion(0,0,0,0);
		Quaternion basis;
		
		// Computes the position
		int nbMeasures = 100;
		for (int i=0; i<nbMeasures; i++){
			sumQ = sumQ.add(mOrientationListener.getOrientation());
		}
		basis = new Quaternion(sumQ.getW()/nbMeasures, sumQ.getX()/nbMeasures, sumQ.getY()/nbMeasures, sumQ.getZ()/nbMeasures);

		// gets the IP address
		//ipAddress =  ((EditText) findViewById(R.id.ipTextField)).getText().toString();
		ipAddress = "192.168.174.1";
		
		// gets the port
		//String portStr = ((EditText) findViewById(R.id.portTextField)).getText().toString();
		//if (!portStr.equals(""))
		//	port = Integer.parseInt(portStr);
		port = 11000;
		
		intent.putExtra(IP_ADDRESS, ipAddress);
		intent.putExtra(PORT, port);
		intent.putExtra(BASIS_REF, basis);
		startActivity(intent);
	}
}