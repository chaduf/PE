/**
 * 
 */
package duduf;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles-Henry
 *
 */
public class ScreenState {
	private ArrayList<Pointer> mPointers;
	
	public ScreenState (){
		mPointers = new ArrayList<Pointer> ();
	}
	
	public boolean isTouched() {
		if (mPointers.size() > 0)
			return true;
		else 
			return false;
	}
	
	public List<Pointer> getPointers() {
		return mPointers;
	}
	
	public void updatePointer (int id, float x, float y){
		for (Pointer p : mPointers){
			if (id == p.getId()){
				p.setX(x);
				p.setY(y);
				return;
			}
		}
		mPointers.add(new Pointer(id, x, y));
	}
	
	public void removePointer (Pointer p){
		mPointers.remove(p);
	}
	
	public void removePointer (int id){
		for (int i=0; i<mPointers.size(); i++){
			if (mPointers.get(i).getId() == id){
				mPointers.remove(i);
			}
		}
	}
}
