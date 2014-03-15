/**
 * 
 */
package duduf;

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
	private Quaternion mOrientation;
	
	/**
	 * 
	 */
	public OrientationCommand(Quaternion orientation){
		super();
		mOrientation = orientation;
	}
	
	public Quaternion getOrientation () {
		return mOrientation;
	}
	
	@Override
	public JSONObject getJson (){
		JSONObject jsonObject;
		JSONArray jsonValues;
		jsonObject = super.getJson();
		jsonValues = new JSONArray();
		
		try {
			if (jsonObject != null){
				jsonObject.put("type", mType);
				
				jsonValues.put(0, mOrientation.getW());
				jsonValues.put(1, mOrientation.getX());
				jsonValues.put(2, mOrientation.getY());
				jsonValues.put(3, mOrientation.getZ());
				jsonObject.put("values", jsonValues);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject;
	}
}
