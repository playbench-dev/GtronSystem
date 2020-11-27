package gtron.com.gtronsystem.MessageFunc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gtron.com.gtronsystem.Activity.MessageActivity;
import gtron.com.gtronsystem.Activity.MessageDetailActivity;
import gtron.com.gtronsystem.R;

/**
 * Created by euibosim on 2017. 10. 11..
 */

public class MessageListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MessageListItem> listViewItemList = new ArrayList<MessageListItem>() ;
    public ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
    Activity activity;
    // ListViewAdapter의 생성자
    public MessageListAdapter(Activity activity) {
        this.activity = activity;
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
        ViewHolder viewHolder = new ViewHolder();
        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_list_item, parent, false);

            viewHolder.linearParent = (LinearLayout)convertView.findViewById(R.id.linear_message_parent);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.message_list_item_title) ;
            viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.message_list_item_message) ;
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.message_list_item_date_string) ;
            viewHolder.chbCheck = (CheckBox)convertView.findViewById(R.id.chb_message_check);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MessageListItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        viewHolder.txtTitle.setText(listViewItem.getTitleStr());
        viewHolder.txtMessage.setText(listViewItem.getMessageStr());
        viewHolder.txtDate.setText(listViewItem.getDateStr());

        checkBoxArrayList.add(viewHolder.chbCheck);



        final View finalConvertView = convertView;
        viewHolder.linearParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    finalConvertView.setBackgroundColor(Color.parseColor("#E3E6E6"));
                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    finalConvertView.setBackgroundColor(Color.parseColor("#ffffff"));
                    Intent intent = new Intent(context, MessageDetailActivity.class);
                    intent.putExtra("title",listViewItemList.get(position).getTitleStr());
                    intent.putExtra("message",listViewItemList.get(position).getMessageStr());
                    intent.putExtra("date",listViewItemList.get(position).getDateStr());
                    context.startActivity(intent);
                }else if (event.getAction() == MotionEvent.ACTION_CANCEL){
                    finalConvertView.setBackgroundColor(Color.parseColor("#ffffff"));
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
    public void addItem(String title, String message, String dateStr,Boolean flag) {
        MessageListItem item = new MessageListItem();

        item.setTitleStr(title);
        item.setMessageStr(message);
        item.setDateStr(dateStr);
        item.setReadFlag(flag);

        listViewItemList.add(item);
    }
    private class ViewHolder{
        public TextView txtTitle,txtMessage,txtDate;
        public CheckBox chbCheck;
        public LinearLayout linearParent;
    }
}

