package gtron.com.gtronsystem.SelectViewFunc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import gtron.com.gtronsystem.R;

import static android.content.Context.MODE_PRIVATE;

public class SelectTileViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<SelectTileViewListItem> listViewItemList = new ArrayList<SelectTileViewListItem>() ;
    ArrayList<TextView> textViewArrayList = new ArrayList<>();
    ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
    // ListViewAdapter의 생성자
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SelectTileViewAdapter() {

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

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.select_tile_view_list_item, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.select_tile_view_list_item_title) ;
            viewHolder.selectedTextView = (TextView) convertView.findViewById(R.id.txt_selected_btn) ;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        SelectTileViewListItem listViewItem = listViewItemList.get(position);

        viewHolder.txtTitle.setText(listViewItem.getTitle());

        textViewArrayList.add(viewHolder.selectedTextView);

        for (int i = 0; i < textViewArrayList.size(); i++){
            textViewArrayList.get(i).setText("Not Selected");
            textViewArrayList.get(i).setTextColor(Color.parseColor("#6A6C70"));
            textViewArrayList.get(i).setBackgroundResource(R.drawable.btn_02);
        }

        textViewArrayList.get(0).setText("Selected");
        textViewArrayList.get(0).setTextColor(Color.parseColor("#ffffff"));
        textViewArrayList.get(0).setBackgroundResource(R.drawable.btn_01);

//        try{
//            if (pref.getInt("select_tile_view_position",0) != 0){
//                textViewArrayList.get(pref.getInt("select_tile_view_position",0)).setText("Selected");
//                textViewArrayList.get(pref.getInt("select_tile_view_position",0)).setTextColor(Color.parseColor("#ffffff"));
//                textViewArrayList.get(pref.getInt("select_tile_view_position",0)).setBackgroundResource(R.drawable.btn_01);
//            }else{
//                textViewArrayList.get(0).setText("Selected");
//                textViewArrayList.get(0).setTextColor(Color.parseColor("#ffffff"));
//                textViewArrayList.get(0).setBackgroundResource(R.drawable.btn_01);
//            }
//        }catch (Exception e){
//
//        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.selectedTextView.setOnTouchListener(new View.OnTouchListener() {
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
                    finalViewHolder.selectedTextView.setText("Selected");
                    finalViewHolder.selectedTextView.setTextColor(Color.parseColor("#ffffff"));
                    finalViewHolder.selectedTextView.setBackgroundResource(R.drawable.btn_01);
                    textViewArrayList.get(position).getBackground().setAlpha(254);
                    editor.putInt("select_tile_view_position",position);
                    editor.putString("title_no",listViewItemList.get(position).getTileNo());
                    editor.commit();

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
    public void addItem(String title, String columns, String rows,String tileNo,Boolean flag) {
        SelectTileViewListItem item = new SelectTileViewListItem();

        item.setTitle(title);
        item.setColumns(columns);
        item.setRow(rows);
        item.setTileNo(tileNo);
        item.setSelectFlag(flag);

        listViewItemList.add(item);
    }

    private class ViewHolder{
        public TextView txtTitle,txtColumns,txtRows,selectedTextView;
    }
}
