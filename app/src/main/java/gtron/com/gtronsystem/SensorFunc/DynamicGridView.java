package gtron.com.gtronsystem.SensorFunc;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * Created by euibosim on 2017. 10. 9..
 */


public class DynamicGridView  extends GridView {

    public DynamicGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#EFF2F9"));
    }

    public DynamicGridView(Context context) {
        super(context);
        setBackgroundColor(Color.parseColor("#EFF2F9"));
    }

    public DynamicGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundColor(Color.parseColor("#EFF2F9"));
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}