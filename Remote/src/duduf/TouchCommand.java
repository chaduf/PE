/**
 * 
 */
package duduf;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Charles-Henry
 *
 */
public class TouchCommand extends Command {
	final protected int mType = 2;
	private static final String POINTER_ID_TAG = "pointerId";

	private ScreenState mScreenState;
	
	public TouchCommand (ScreenState screenState){
		mScreenState = screenState;
	}
	
	public ScreenState getScreenState(){
		return mScreenState;
	}
	
	@Override
	public JSONObject getJson (){
		JSONObject jsonCommand = super.getJson();
		JSONArray jsonPointerList = new JSONArray();
		JSONObject jsonPointer;
		JSONArray jsonPointerValues;
		List<Pointer> pointerList = mScreenState.getPointers();
		try{
			for (Pointer p : pointerList){
				 jsonPointer = new JSONObject();
				 jsonPointerValues = new JSONArray();
				 
				 jsonPointer.put("id", p.getId());
				 
				 jsonPointerValues.put(0, p.getX());
				 jsonPointerValues.put(1, p.getY());
				 jsonPointer.put("values", jsonPointerValues);
				 
				 jsonPointerList.put(jsonPointer);
			}
			
			jsonCommand.put("pointers", jsonPointerList);
			jsonCommand.put("size", pointerList.size());
			return jsonCommand;
			
		} catch (JSONException e){
			e.printStackTrace();
			return super.getJson();
		}
	}
}
