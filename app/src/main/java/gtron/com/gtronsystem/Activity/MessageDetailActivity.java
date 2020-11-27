package gtron.com.gtronsystem.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gtron.com.gtronsystem.R;

public class MessageDetailActivity extends AppCompatActivity {

    TextView txtTitle,txtMessage,txtDate;
    LinearLayout linearBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        txtTitle = (TextView)findViewById(R.id.txt_ms_detail_title);
        txtMessage = (TextView)findViewById(R.id.txt_ms_detail_contents);
        txtDate = (TextView)findViewById(R.id.txt_ms_detail_date);
        linearBack = (LinearLayout)findViewById(R.id.linear_message_detail_back);

        Intent intent = getIntent();
        txtTitle.setText(intent.getStringExtra("title"));
        txtMessage.setText(intent.getStringExtra("message"));
        txtDate.setText(intent.getStringExtra("date"));

        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
