package gtron.com.gtronsystem.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by kmw on 2017. 12. 22..
 */

public class CustomScrollView extends ScrollView {
    private String TAG = "CustomScrollView";
    private Handler handler = null;
    private Rect rect;


    public CustomScrollView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkIsLocatedAtFooter();
    }

    public boolean checkIsLocatedAtFooter() {
        if (rect == null) {
            rect = new Rect();
            getLocalVisibleRect(rect);
        }
        int oldBottom = rect.bottom;
        getLocalVisibleRect(rect);
        int height = getMeasuredHeight();
        View v = getChildAt(0);
        if (oldBottom > 0 && height > 0) {
            if (oldBottom != rect.bottom && rect.bottom == v.getMeasuredHeight()) {
//                if (handler != null) {
//                    handler.sendEmptyMessage(/*메세지*/);
//                }
                Log.i(TAG,"바닥");
            }
            return true;
        }else{
            return false;
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);

        if (rect == null) {
            rect = new Rect();
            getLocalVisibleRect(rect);
        }
        int oldBottom = rect.bottom;
        getLocalVisibleRect(rect);
        int height = getMeasuredHeight();
        View v = getChildAt(0);
        if (oldBottom > 0 && height > 0) {
            if (oldBottom != rect.bottom && rect.bottom == v.getMeasuredHeight()) {
//                if (handler != null) {
//                    handler.sendEmptyMessage(/*메세지*/);
//                }
                Log.i(TAG,"바닥aasdsadasdasdasd");
            }
        }
    }
}

