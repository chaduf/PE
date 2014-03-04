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
public abstract class Command {
	private long mId;
	protected static long nbInstances;
	
	/**
	 * Constructor
	 */
	public Command () {
		mId = nbInstances;
		nbInstances ++;
	}
	
	public static void init () {
		nbInstances = 0;
	}
	
	public JSONObject getJson (){
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("id", mId);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
		return jsonObject;
	}
}