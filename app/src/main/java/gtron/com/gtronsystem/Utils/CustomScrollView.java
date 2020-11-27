package gtron.com.gtronsystem.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private boolean enableScrolling = true;
    private static final int OFF_SET = 10;
    private float preX;

    public boolean isEnableScrolling() {
        return enableScrolling;
    }

    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isEnableScrolling()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }

//        switch (ev.getAction() & MotionEvent.ACTION_MASK){
//            case MotionEvent.ACTION_DOWN:
//                preX = ev.getX();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//
//                float x = ev.getX();
//                if((x - OFF_SET <= preX && preX <= x+ OFF_SET)){
//                    return false;
//                }else{
//                    return true;
//                }
//
//            case MotionEvent.ACTION_POINTER_DOWN : {
//                return false;
//            }
//        }
//        return super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnableScrolling()) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }
}
