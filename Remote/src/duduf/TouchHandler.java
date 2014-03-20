/**
 * 
 */
package duduf;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author Charles-Henry
 *
 */
public class TouchHandler implements OnTouchListener {	
	private ScreenState mState;
	
	public TouchHandler() {
		mState = new ScreenState();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int pointerIndex =  event.getActionIndex();
		int action = event.getActionMasked();
		int pointerId = event.getPointerId(pointerIndex);
		boolean returnValue;
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mState.updatePointer(pointerId, event.getX(pointerIndex)/v.getWidth(), event.getY(pointerIndex)/v.getHeight());
			return true;

		case MotionEvent.ACTION_POINTER_DOWN:
			mState.updatePointer(pointerId, event.getX(pointerIndex)/v.getWidth(), event.getY(pointerIndex)/v.getHeight());
			return true;
			
		case MotionEvent.ACTION_MOVE:
			for (int i=0; i<event.getPointerCount(); i++)
			mState.updatePointer(event.getPointerId(i), event.getX(i)/v.getWidth(), event.getY(i)/v.getHeight());
			return true;
			
		case MotionEvent.ACTION_POINTER_UP:
			mState.removePointer(pointerId);
			return true;
			
		case MotionEvent.ACTION_UP:
			mState.removePointer(pointerId);
			return true;
			
		default:
			return false;
		}
	}
	
	public ScreenState getState(){
		return mState;
	}
}
