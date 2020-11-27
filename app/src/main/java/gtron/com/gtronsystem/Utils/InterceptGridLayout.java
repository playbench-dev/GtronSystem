package gtron.com.gtronsystem.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import gtron.com.gtronsystem.Activity.TileViewActivity;
import gtron.com.gtronsystem.R;

import static gtron.com.gtronsystem.Activity.TileViewActivity.touchBoolean;

public class InterceptGridLayout extends GridLayout {

    String TAG = "InterceptGridLayout";
    private static final int OFF_SET = 10;
    private float preX;
    WindowManager wm;
    Display display;

    int x[] = new int[2];
    int x1[] = new int[2];
    int y[] = new int[2];
    int y1[] = new int[2];

    public InterceptGridLayout(Context context) {
        super(context);
        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
    }

    public InterceptGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
    }

    public InterceptGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointer = event.getPointerCount();

        if (pointer > 2){
            pointer = 2;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN : {
                Log.i(TAG,"down---");
                touchBoolean = false;
                return true;
            }
            case MotionEvent.ACTION_POINTER_DOWN : {
                x[0] = (int) event.getX(0);
                x[1] = (int) event.getX(1);
                y[0] = (int) event.getY(0);
                y[1] = (int) event.getY(1);

                touchBoolean = true;

                TileViewActivity.scrParent.setEnableScrolling(false);

                return true;
            }
            case MotionEvent.ACTION_POINTER_UP : {

                TileViewActivity.scrParent.setEnableScrolling(true);

                break;
            }
            case MotionEvent.ACTION_MOVE : {
                if (pointer == 2){
                    TileViewActivity.scrParent.setEnableScrolling(false);
                    x1[0] = (int) event.getX(0);
                    x1[1] = (int) event.getX(1);
                    y1[0] = (int) event.getY(0);
                    y1[1] = (int) event.getY(1);

                    Log.i(TAG,"down - x1[0] : " + x1[0] + " x1[1] : " + x1[1]);

                    if (x[0] < x[1]){
                        Log.i(TAG,"1");
                        if (x[0] - x1[0] < 50 && x1[1] - x[1]  > 50){
                            Log.i(TAG,"2");
                            if (getColumnCount() == 5){
                                changeColumnCount(4);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(3);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(2);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }
                        }else if (x1[0] - x[0] < 50 && x[1] - x1[1] > 50){
                            Log.i(TAG,"3");
                            if (getColumnCount() == 2){
                                changeColumnCount(3);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(4);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(5);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }
                        }
                    }else if (x[0] > x[1]){
                        Log.i(TAG,"4");
                        if (x1[0] - x[0] > 50 && x[1] - x1[1]  > 50){
                            Log.i(TAG,"5");
                            if (getColumnCount() == 5){
                                changeColumnCount(4);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(3);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(2);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }
                        }else if (x[0] - x1[0] > 50 && x1[1] - x[1] > 50){
                            Log.i(TAG,"6");
                            if (getColumnCount() == 2){
                                changeColumnCount(3);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(4);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(5);
                                x[0] = x1[0];
                                x[1] = x1[1];
                            }
                        }
                    }

                    if (y[0] < y[1]){
                        Log.i(TAG,"1");
                        if (y[0] - y1[0] < 50 && y1[1] - y[1]  > 50){
                            Log.i(TAG,"2");
                            if (getColumnCount() == 5){
                                changeColumnCount(4);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(3);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(2);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }
                        }else if (y1[0] - y[0] < 50 && y[1] - y1[1] > 50){
                            Log.i(TAG,"3");
                            if (getColumnCount() == 2){
                                changeColumnCount(3);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(4);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(5);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }
                        }
                    }else if (y[0] > y[1]){
                        Log.i(TAG,"4");
                        if (y1[0] - y[0] > 50 && y[1] - y1[1]  > 50){
                            Log.i(TAG,"5");
                            if (getColumnCount() == 5){
                                changeColumnCount(4);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(3);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(2);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }
                        }else if (y[0] - y1[0] > 50 && y1[1] - y[1] > 50){
                            Log.i(TAG,"6");
                            if (getColumnCount() == 2){
                                changeColumnCount(3);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 3){
                                changeColumnCount(4);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }else if (getColumnCount() == 4){
                                changeColumnCount(5);
                                y[0] = y1[0];
                                y[1] = y1[1];
                            }
                        }
                    }
                }
                return false;
            }
        }
        return true;
    }

    public void changeColumnCount(int columnCount) {
        if (getColumnCount() != columnCount) {
            final int viewsCount = getChildCount();
            for (int i = 0; i < viewsCount; i++) {
                View view = getChildAt(i);

                if (columnCount == 2){
                    TileViewActivity.txtSensorIdList.get(i).setTextSize(25);
                    TileViewActivity.txtSensorNameList.get(i).setTextSize(25);
                    TileViewActivity.txtSensorStatusList.get(i).setTextSize(25);
                }else if (columnCount == 3){
                    TileViewActivity.txtSensorIdList.get(i).setTextSize(22);
                    TileViewActivity.txtSensorNameList.get(i).setTextSize(22);
                    TileViewActivity.txtSensorStatusList.get(i).setTextSize(22);
                }else if (columnCount == 4){
                    TileViewActivity.txtSensorIdList.get(i).setTextSize(19);
                    TileViewActivity.txtSensorNameList.get(i).setTextSize(19);
                    TileViewActivity.txtSensorStatusList.get(i).setTextSize(19);
                }else if (columnCount == 5){
                    TileViewActivity.txtSensorIdList.get(i).setTextSize(15);
                    TileViewActivity.txtSensorNameList.get(i).setTextSize(15);
                    TileViewActivity.txtSensorStatusList.get(i).setTextSize(15);
                }

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = (int)(display.getWidth()/columnCount);
                params.height = (int)(display.getWidth()/columnCount);
                view.setLayoutParams(params);
            }
//            TileViewActivity.scaleCount = columnCount;
            setColumnCount(columnCount);
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        return false;
    }
}
