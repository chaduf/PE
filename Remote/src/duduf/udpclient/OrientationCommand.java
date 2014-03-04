/**
 * 
 */
package duduf.udpclient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Charles-Henry
 *
 */
public class OrientationCommand extends Command {

	private float mAzimuth;
	private float mPitch;
	private float mRoll;
	
	/**
	 * 
	 */
	public OrientationCommand(float[] orientation) {
		super();
		mAzimuth = orientation[0];
		mPitch = orientation[1];
		mRoll = orientation[2];
	}

	public float getAzimuth() {
		return mAzimuth;
	}

	public float getPitch() {
		return mPitch;
	}

	public float getRoll() {
		return mRoll;
	}
	
	public float[] getOrientation () {
		float[] orientation = {mAzimuth, mPitch, mRoll};
		return orientation;
	}
	
	@Override
	public JSONObject getJson (){
		JSONObject jsonObject;
		jsonObject = super.getJson();
		
		try {
			if (jsonObject != null){
				jsonObject.put("type", "orientation");
				jsonObject.put("azimuth", mAzimuth);
				jsonObject.put("pitch", mPitch);
				jsonObject.put("roll", mRoll);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
}
