package gtron.com.gtronsystem.SelectViewFunc;

/**
 * Created by euibosim on 2017. 10. 10..
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
        import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.ArrayList;

import gtron.com.gtronsystem.Activity.SelectViewActivity;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.Utils;

import static android.content.Context.MODE_PRIVATE;

public class SelectViewListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<SelectViewListItem> listViewItemList = new ArrayList<SelectViewListItem>() ;
    ArrayList<TextView> textViewArrayList = new ArrayList<>();
    ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
    // ListViewAdapter의 생성자
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public SelectViewListAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        pref = context.getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();
        editor.commit();
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.select_view_list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.select_view_list_item_title) ;
        TextView processTextView = (TextView) convertView.findViewById(R.id.select_view_list_item_process_cnt) ;
        TextView sensorTextView = (TextView) convertView.findViewById(R.id.select_view_list_item_sensor_cnt) ;
        final TextView selectedTextView = (TextView) convertView.findViewById(R.id.txt_selected_btn) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SelectViewListItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitleStr());
        processTextView.setText(listViewItem.getViewCntStr());
        sensorTextView.setText(listViewItem.getSensorCntStr());

//        if(listViewItemList.get(position).getSelectFlag() == true){
//            selectedTextView.setText("Selected");
//            selectedButton.setImageResource(R.drawable.btn_01);
//        }else{
//            selectedTextView.setText("Not Selected");
//            selectedButton.setImageResource(R.drawable.btn_02);
//        }

        textViewArrayList.add(selectedTextView);
//        imageViewArrayList.add(selectedButton);

        for (int i = 0; i < textViewArrayList.size(); i++){
            textViewArrayList.get(i).setText("Not Selected");
            textViewArrayList.get(i).setTextColor(Color.parseColor("#6A6C70"));
            textViewArrayList.get(i).setBackgroundResource(R.drawable.btn_02);
        }

        try{
            if (pref.getInt("select_view_position",0)==0){
                textViewArrayList.get(0).setText("Selected");
                textViewArrayList.get(0).setTextColor(Color.parseColor("#ffffff"));
                textViewArrayList.get(0).setBackgroundResource(R.drawable.btn_01);
            }else{
                textViewArrayList.get(pref.getInt("select_view_position",0)).setText("Selected");
                textViewArrayList.get(pref.getInt("select_view_position",0)).setTextColor(Color.parseColor("#ffffff"));
                textViewArrayList.get(pref.getInt("select_view_position",0)).setBackgroundResource(R.drawable.btn_01);
            }
        }catch (Exception e){

        }

        selectedTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    textViewArrayList.get(position).getBackground().setAlpha(125);

                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    for (int i = 0; i < textViewArrayList.size(); i++){
                        textViewArrayList.get(i).setText("Not Selected");
                        textViewArrayList.get(i).setTextColor(Color.parseColor("#6A6C70"));
                        textViewArrayList.get(i).setBackgroundResource(R.drawable.btn_02);
                    }
                    selectedTextView.setText("Selected");
                    selectedTextView.setTextColor(Color.parseColor("#ffffff"));
                    selectedTextView.setBackgroundResource(R.drawable.btn_01);
                    textViewArrayList.get(position).getBackground().setAlpha(254);
                    editor.putInt("select_view_position",position);
                    editor.putString("view_no",listViewItemList.get(position).getViewNo());
                    editor.commit();


                    new SelectViewActivity.ViewSelectServer().execute(String.valueOf(Utils.USER_NO), String.valueOf(Utils.USER_SITE_NO), pref.getString("view_no", ""));

                }else if (event.getAction() == MotionEvent.ACTION_CANCEL){

                }
                return true;
            }
        });

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String process, String sensor,String viewNo,Boolean flag) {
        SelectViewListItem item = new SelectViewListItem();

        item.setTitleStr(title);
        item.setViewCntStr(process);
        item.setSensorCntStr(sensor);
        item.setViewNo(viewNo);
        item.setSelectFlag(flag);

        listViewItemList.add(item);
    }
}

