/**
 * 
 */
package duduf.udpclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Charles-Henry
 *
 * Orientation Orientation is type 1;
 */
public class OrientationCommand extends Command {

	final protected int mType = 1;
	
	private float mW;
	private float mX;
	private float mY;
	private float mZ;
	
	/**
	 * 
	 */
	public OrientationCommand(float[] quaternion) {
		super();
		
		mW = quaternion[0];
		mX = quaternion[1];
		mY = quaternion[2];
		mZ = quaternion[3];
	}
	
	public static void convertMatrixToQuaternion (float[] quaternion, float[] matrix){		
		quaternion[0] = (float) (Math.sqrt(1.0 + matrix[0] + matrix[4] + matrix[8]) / 2.0);
		float w4 = (float) (4.0 * quaternion[0]);
		quaternion[1] = (matrix[7] - matrix[5]) / w4 ;
		quaternion[2] = (matrix[2] - matrix[6]) / w4 ;
		quaternion[3] = (matrix[3] - matrix[1]) / w4 ;
	}
	
	public float[] getOrientation () {
		float[] orientation = {mW, mX, mY, mZ};
		return orientation;
	}
	
	@Override
	public JSONObject getJson (){
		JSONObject jsonObject;
		JSONArray jsonValues;
		jsonObject = super.getJson();
		jsonValues = new JSONArray();
		
		try {
			if (jsonObject != null){
				jsonObject.put("type", mType);// Orientation is type One;
				
				jsonValues.put(mW);
				jsonValues.put(mX);
				jsonValues.put(mY);
				jsonValues.put(mZ);
				jsonObject.put("values", jsonValues);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
}
