/**
 * 
 */
package duduf;

/**
 * @author Charles-Henry
 *
 */
public class Pointer {
	
	private int mId;
	private float mX;
	private float mY;
	
	public Pointer (int id, float x, float y){
		mId = id;
		mX = x;
		mY = y;
	}
	
	public int getId() {
		return mId;
	}
	
	public float getX() {
		return mX;
	}
	
	public float getY() {
		return mY;
	}
	
	public void setX(float x) {
		mX = x;
	}
	public void setY(float y) {
		mY = y;
	}
}
