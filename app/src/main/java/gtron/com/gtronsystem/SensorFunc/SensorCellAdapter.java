package gtron.com.gtronsystem.SensorFunc;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gtron.com.gtronsystem.Activity.MainActivity;
import gtron.com.gtronsystem.R;

/**
 * Created by euibosim on 2017. 10. 9..
 */

public class SensorCellAdapter extends BaseAdapter{

    private String TAG = "SensorCellAdapter";
    public ArrayList<SensorCell> listItems = new ArrayList<SensorCell>();
    Context context;
    public static ArrayList<TextView> sensorIdArrayList = new ArrayList<>();
    public static ArrayList<TextView> nameArrayList = new ArrayList<>();
    public static ArrayList<TextView> statusArrayList = new ArrayList<>();
    public static int colums = 0;
    WindowManager wm;
    Display display;
    int test;
    SimpleDateFormat simpleDateFormatYYYY = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public SensorCellAdapter(Context context,int columns) {
        this.context = context;
        this.test = columns;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();


        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            viewHolder.sensorName = (TextView) convertView.findViewById(R.id.tv_sensor_2);
            viewHolder.sensorStatus = (TextView) convertView.findViewById(R.id.tv_sensor_3);
            viewHolder.sensorTime = (TextView) convertView.findViewById(R.id.tv_sensor_1);
            viewHolder.lin = (LinearLayout)convertView.findViewById(R.id.lin_sensor_cell);

            convertView.setTag(viewHolder);

            sensorIdArrayList.add(viewHolder.sensorTime);
            nameArrayList.add(viewHolder.sensorName);
            statusArrayList.add(viewHolder.sensorStatus);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.lin.setBackgroundColor(-31221532);

        if (colums == 0){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(display.getWidth()/4, display.getWidth()/4);
            convertView.setLayoutParams(params);
        }else{
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(display.getWidth()/colums, display.getWidth()/colums);
            convertView.setLayoutParams(params);
        }

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if (colums == 2){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            }else if (colums == 3){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            }else if (colums == 4){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            }else if (colums == 5){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
            }else{
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            }
        }else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (colums == 2){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
            }else if (colums == 3){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,29);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,29);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,29);
            }else if (colums == 4){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
            }else if (colums == 5){
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,23);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,23);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,23);
            }else{
                viewHolder.sensorTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
                viewHolder.sensorName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
                viewHolder.sensorStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
            }
        }

        viewHolder.sensorTime.setTextColor(Color.parseColor("#000000"));
        viewHolder.sensorName.setTextColor(Color.parseColor("#000000"));
        viewHolder.sensorStatus.setTextColor(Color.parseColor("#000000"));
        viewHolder.sensorName.setText(listItems.get(position).getSensorName());

        int highPriority = 1000;

        ArrayList<String> sameNameList = new ArrayList<>();
        ArrayList<String> sameColorList = new ArrayList<>();
        ArrayList<String> textColorList = new ArrayList<>();


        final ArrayList<String> portDateList = new ArrayList<>();
        final ArrayList<String> statusNameList = new ArrayList<>();

        for (int i = 0; i < listItems.get(position).getPort1().size(); i++){
            if (listItems.get(position).getPort1().get(i) == 1){
                portDateList.add(listItems.get(position).getDataList().get(i));
                statusNameList.add(listItems.get(position).getNameList().get(i));

                if (highPriority > listItems.get(position).getPriority().get(i)){
                    viewHolder.lin.setBackgroundColor(Color.parseColor(listItems.get(position).getColorList().get(i)));
                    viewHolder.sensorStatus.setText(listItems.get(position).getNameList().get(i));
                    viewHolder.sensorTime.setText(listItems.get(position).getDataList().get(i).substring(10,19));

                    highPriority = listItems.get(position).getPriority().get(i);
                    sameNameList.add(listItems.get(position).getNameList().get(i));
                    sameColorList.add(listItems.get(position).getColorList().get(i));
                    textColorList.add(listItems.get(position).getTextColorList().get(i));
                }else if (highPriority == listItems.get(position).getPriority().get(i)){
                    sameNameList.add(listItems.get(position).getNameList().get(i));
                    sameColorList.add(listItems.get(position).getColorList().get(i));
                    textColorList.add(listItems.get(position).getTextColorList().get(i));
                }
            }
        }

        if (sameNameList.size() > 1){
//            Log.i(TAG,"test position : " + position + " bgColor : " + sameColorList.get(MainActivity.aa % sameColorList.size()));
            viewHolder.lin.setBackgroundColor(Color.parseColor(sameColorList.get(MainActivity.aa % sameColorList.size())));
            viewHolder.sensorStatus.setText(sameNameList.get(MainActivity.aa % sameColorList.size()));
            viewHolder.sensorTime.setText(portDateList.get(MainActivity.aa % sameColorList.size()).substring(10,19));

            if (textColorList.get(MainActivity.aa % textColorList.size()).equals("#FFF")){
                viewHolder.sensorTime.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.sensorName.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.sensorStatus.setTextColor(Color.parseColor("#FFFFFF"));
            }else{
                viewHolder.sensorTime.setTextColor(Color.parseColor("#000000"));
                viewHolder.sensorName.setTextColor(Color.parseColor("#000000"));
                viewHolder.sensorStatus.setTextColor(Color.parseColor("#000000"));
            }

            convertView.invalidate();
        }
        else if (sameNameList.size() == 0){
            viewHolder.lin.setBackgroundColor(Color.parseColor(MainActivity.allOffColor));
            viewHolder.sensorStatus.setText("ALL OFF");
        }

        viewHolder.lin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP : {
                        Log.i(TAG,"click");

                        MainActivity.linearStatus.removeAllViews();
                        MainActivity.linearLandStatus.removeAllViews();

                        MainActivity.txtName.setText(listItems.get(position).getSensorName());
                        MainActivity.txtProcess.setText(listItems.get(position).getProcessList());
                        MainActivity.txtGroup.setText(listItems.get(position).getGroupList());
                        MainActivity.txtLocation.setText(listItems.get(position).getLocationList());

                        MainActivity.txtLandName.setText(listItems.get(position).getSensorName());
                        MainActivity.txtLandProcess.setText(listItems.get(position).getProcessList());
                        MainActivity.txtLandGroup.setText(listItems.get(position).getGroupList());
                        MainActivity.txtLandLocation.setText(listItems.get(position).getLocationList());

                        if (portDateList.size() > 0){
                            for (int i = 0; i < portDateList.size(); i++){
                                TextView txtStatus = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                txtStatus.setLayoutParams(params);
                                txtStatus.setGravity(Gravity.CENTER);
                                txtStatus.setTextSize(12);

                                String date = "";
                                if (portDateList.get(i).length() > 0){
                                    try {
                                       date = simpleDateFormat.format(simpleDateFormatYYYY.parse(portDateList.get(i)));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                txtStatus.setText(statusNameList.get(i)+"("+date+")");

                                TextView txtStatus1 = new TextView(context);
                                txtStatus1.setLayoutParams(params);
                                txtStatus1.setGravity(Gravity.CENTER);
                                txtStatus1.setTextSize(12);
                                txtStatus1.setText(statusNameList.get(i)+"("+date+")");

                                MainActivity.linearStatus.addView(txtStatus);
                                MainActivity.linearLandStatus.addView(txtStatus1);
                            }
                        }else if (portDateList.size() == 0){
                            TextView txtStatus = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            txtStatus.setLayoutParams(params);
                            txtStatus.setGravity(Gravity.CENTER);
                            txtStatus.setText("ALL OFF");

                            TextView txtStatus1 = new TextView(context);
                            txtStatus1.setLayoutParams(params);
                            txtStatus1.setGravity(Gravity.CENTER);
                            txtStatus1.setText("ALL OFF");

                            MainActivity.linearStatus.addView(txtStatus);
                            MainActivity.linearLandStatus.addView(txtStatus1);
                        }

                        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                        {
                            MainActivity.dialog.show();
                        }else if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            MainActivity.linearDialogTest.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
                return true;
            }
        });

        return convertView;
    }
    public void addItem(String sensorId,String sensorName,ArrayList<Integer> port1,ArrayList<String> color,ArrayList<Integer> priority,ArrayList<String> name,
                        int modelIdList,String processList,String lineList,
                        String locationList,String groupList,ArrayList<String> dataList,ArrayList<String> textColor){
        SensorCell item = new SensorCell();

        item.setSensorId(sensorId);
        item.setSensorName(sensorName);
        item.setPort1(port1);
        item.setColorList(color);
        item.setPriority(priority);
        item.setNameList(name);
        item.setModelIdList(modelIdList);
        item.setProcessList(processList);
        item.setLineList(lineList);
        item.setLocationList(locationList);
        item.setGroupList(groupList);
        item.setDataList(dataList);
        item.setTextColorList(textColor);

        listItems.add(item);
    }

    private class ViewHolder{
        public TextView sensorName, sensorStatus, sensorTime;
        public LinearLayout lin;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}