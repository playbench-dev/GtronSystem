package gtron.com.gtronsystem.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gtron.com.gtronsystem.Utils.DateTimePicker.SingleDateAndTimePicker;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.Utils;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Views.InterceptLinearLayout;

import static gtron.com.gtronsystem.Utils.Utils.PUSH_END_TIME;
import static gtron.com.gtronsystem.Utils.Utils.PUSH_START_TIME;
import static gtron.com.gtronsystem.Utils.Utils.SMS_CERT;
import static gtron.com.gtronsystem.Utils.Utils.SMS_ENABLE;
import static gtron.com.gtronsystem.Utils.Utils.SOUND;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "SettingActivity";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView timeStart,timeEnd;
    Switch swhPush,swhPopup,swhSound;
    RadioGroup repeatRadio,soundVibrationRadio;
    RadioButton rd0,rd1,rd2,rd3,rd4,rd5;
    RadioButton rdoSound,rdoVibration,rdoSoundVibration;
    Vibrator vibrator;
    AudioManager audioManager,audioManager1;
    Notification notification;
//    ArrayList<Uri> uriArrayList;
//    ArrayList<String> ringtoneTitleList;
//    ArrayList<String> ringtoneIdList;
    MediaPlayer player;
    LinearLayout linearBack;
    TextView txtSelectAlarm;
    TextView txtSound,txtVibration,txtSoundAndVib;
    FrameLayout frameSound,frameVibration,frameSVibration;
    TextView txt0,txt1,txt2,txt3,txt4,txt5;
    FrameLayout frame0,frame1,frame2,frame3,frame4,frame5;
    TextView txtSave;

    String selectDate;
    Date clickDate = null;

    LinearLayout linearSoundMax,linearSoundAndVibrator,linearSelectAlarmSound,linearAlarmRepeat;
    InterceptLinearLayout linearPopupCheckParent;

    RadioButton rdo1s,rdo5s,rdo10s,rdo20s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        getSupportActionBar().hide();
        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

//        uriArrayList = new ArrayList<>();
//        ringtoneTitleList = new ArrayList<>();
//        ringtoneIdList = new ArrayList<>();

        txtSave = (TextView)findViewById(R.id.txt_setting_save);
        txtSelectAlarm = (TextView) findViewById(R.id.txt_setting_alarm_sound);
        timeStart = (TextView) findViewById(R.id.txt_setting_time_start);
        timeEnd = (TextView) findViewById(R.id.txt_setting_time_end);
        swhPush = (Switch)findViewById(R.id.swh_setting_push);
        swhPopup = (Switch)findViewById(R.id.swh_setting_popup);
        swhSound = (Switch)findViewById(R.id.swh_setting_sound);
        repeatRadio = (RadioGroup)findViewById(R.id.radio_setting_alarm_grp);
        rd0 = (RadioButton)findViewById(R.id.radio_setting_0);
        rd1 = (RadioButton)findViewById(R.id.radio_setting_1);
        rd2 = (RadioButton)findViewById(R.id.radio_setting_2);
        rd3 = (RadioButton)findViewById(R.id.radio_setting_3);
        rd4 = (RadioButton)findViewById(R.id.radio_setting_4);
        rd5 = (RadioButton)findViewById(R.id.radio_setting_5);
        soundVibrationRadio = (RadioGroup)findViewById(R.id.rdo_setting_sound_vibration_rdg);
        rdoSound = (RadioButton)findViewById(R.id.rdo_setting_sound);
        rdoVibration = (RadioButton)findViewById(R.id.rdo_setting_vibration);
        rdoSoundVibration = (RadioButton)findViewById(R.id.rdo_setting_sound_vibration);
        linearBack = (LinearLayout)findViewById(R.id.linear_setting_back);
        txtSound = (TextView)findViewById(R.id.txt_setting_sound);
        txtVibration = (TextView)findViewById(R.id.txt_setting_vibration);
        txtSoundAndVib = (TextView)findViewById(R.id.txt_setting_sound_vibration);
        frameSound = (FrameLayout)findViewById(R.id.frame_setting_sound);
        frameVibration = (FrameLayout)findViewById(R.id.frame_setting_vibration);
        frameSVibration = (FrameLayout)findViewById(R.id.frame_setting_sound_vibration);
        txt0 = (TextView)findViewById(R.id.txt_setting_0);
        txt1 = (TextView)findViewById(R.id.txt_setting_1);
        txt2 = (TextView)findViewById(R.id.txt_setting_2);
        txt3 = (TextView)findViewById(R.id.txt_setting_3);
        txt4 = (TextView)findViewById(R.id.txt_setting_4);
        txt5 = (TextView)findViewById(R.id.txt_setting_5);
        frame0 = (FrameLayout)findViewById(R.id.frame_setting_0);
        frame1 = (FrameLayout)findViewById(R.id.frame_setting_1);
        frame2 = (FrameLayout)findViewById(R.id.frame_setting_2);
        frame3 = (FrameLayout)findViewById(R.id.frame_setting_3);
        frame4 = (FrameLayout)findViewById(R.id.frame_setting_4);
        frame5 = (FrameLayout)findViewById(R.id.frame_setting_5);

        linearSoundMax = (LinearLayout)findViewById(R.id.linear_setting_sound_max_parent);
        linearSoundAndVibrator = (LinearLayout)findViewById(R.id.linear_setting_sound_vibrator_parent);
        linearSelectAlarmSound = (LinearLayout)findViewById(R.id.linear_setting_select_alarm_sound_parent);
        linearAlarmRepeat = (LinearLayout)findViewById(R.id.linear_setting_repeat);

        rdo1s = (RadioButton)findViewById(R.id.rdo_setting_1s);
        rdo5s = (RadioButton)findViewById(R.id.rdo_setting_5s);
        rdo10s = (RadioButton)findViewById(R.id.rdo_setting_10s);
        rdo20s = (RadioButton)findViewById(R.id.rdo_setting_20s);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        audioManager1 = (AudioManager)getSystemService(AUDIO_SERVICE);

        linearPopupCheckParent = (InterceptLinearLayout)findViewById(R.id.linear_popup_swh_parent);

        linearPopupCheckParent.setVisibility(View.GONE);

        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            linearSoundMax.setVisibility(View.GONE);
            linearSoundAndVibrator.setVisibility(View.GONE);
            linearAlarmRepeat.setVisibility(View.GONE);
        }else{
            linearSoundMax.setVisibility(View.VISIBLE);
            linearSoundAndVibrator.setVisibility(View.GONE);
            linearSelectAlarmSound.setVisibility(View.VISIBLE);
        }

        //deafault setting
        swhPush.setChecked(pref.getBoolean("push_on",true));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.canDrawOverlays(this)) {
//                swhPopup.setChecked(true);
//                editor.putBoolean("popup_on",true);
//                editor.commit();
//            } else {
//                swhPopup.setChecked(false);
//                editor.putBoolean("popup_on",false);
//                editor.commit();
//            }
//        }

        swhSound.setChecked(pref.getBoolean("sound_on",true));

        timeStart.setText(PUSH_START_TIME);
        timeEnd.setText(PUSH_END_TIME);

        notification = new Notification();

        if (SOUND.contains("defualt")){
            txtSelectAlarm.setText("sound1");
        }else{
            txtSelectAlarm.setText(Utils.SOUND.substring(0, SOUND.indexOf("_")));
        }


        if (Utils.SOUND.contains("_1s")){
            rdo1s.setChecked(true);
        }else if (Utils.SOUND.contains("_5s")){
            rdo5s.setChecked(true);
        }else if (Utils.SOUND.contains("_10s")){
            rdo10s.setChecked(true);
        }else if (Utils.SOUND.contains("_20s")){
            rdo20s.setChecked(true);
        }else if (SOUND.contains("defualt")){
            rdo5s.setChecked(true);
        }

        Log.i("setting","pref.getInt(\"repeat_cycle\",1) = "+pref.getInt("repeat_cycle",1));
        switch (pref.getInt("repeat_cycle",1)){
            case 1: {
                rd1.setChecked(true);
                rd2.setChecked(false);
                rd3.setChecked(false);
                rd4.setChecked(false);
                rd5.setChecked(false);
                rd0.setChecked(false);

                txt1.setTextColor(Color.parseColor("#333333"));
                txt2.setTextColor(Color.parseColor("#DEDCDE"));
                txt3.setTextColor(Color.parseColor("#DEDCDE"));
                txt4.setTextColor(Color.parseColor("#DEDCDE"));
                txt5.setTextColor(Color.parseColor("#DEDCDE"));
                txt0.setTextColor(Color.parseColor("#DEDCDE"));
                break;
            }

            case 2: {
                rd1.setChecked(false);
                rd2.setChecked(true);
                rd3.setChecked(false);
                rd4.setChecked(false);
                rd5.setChecked(false);
                rd0.setChecked(false);
                txt1.setTextColor(Color.parseColor("#DEDCDE"));
                txt2.setTextColor(Color.parseColor("#333333"));
                txt3.setTextColor(Color.parseColor("#DEDCDE"));
                txt4.setTextColor(Color.parseColor("#DEDCDE"));
                txt5.setTextColor(Color.parseColor("#DEDCDE"));
                txt0.setTextColor(Color.parseColor("#DEDCDE"));
                break;
            }
            case 3: {
                rd1.setChecked(false);
                rd2.setChecked(false);
                rd3.setChecked(true);
                rd4.setChecked(false);
                rd5.setChecked(false);
                rd0.setChecked(false);
                txt1.setTextColor(Color.parseColor("#DEDCDE"));
                txt2.setTextColor(Color.parseColor("#DEDCDE"));
                txt3.setTextColor(Color.parseColor("#333333"));
                txt4.setTextColor(Color.parseColor("#DEDCDE"));
                txt5.setTextColor(Color.parseColor("#DEDCDE"));
                txt0.setTextColor(Color.parseColor("#DEDCDE"));
                break;
            }
            case 4: {
                rd1.setChecked(false);
                rd2.setChecked(false);
                rd3.setChecked(false);
                rd4.setChecked(true);
                rd5.setChecked(false);
                rd0.setChecked(false);
                txt1.setTextColor(Color.parseColor("#DEDCDE"));
                txt2.setTextColor(Color.parseColor("#DEDCDE"));
                txt3.setTextColor(Color.parseColor("#DEDCDE"));
                txt4.setTextColor(Color.parseColor("#333333"));
                txt5.setTextColor(Color.parseColor("#DEDCDE"));
                txt0.setTextColor(Color.parseColor("#DEDCDE"));
                break;
            }
            case 5: {
                rd1.setChecked(false);
                rd2.setChecked(false);
                rd3.setChecked(false);
                rd4.setChecked(false);
                rd5.setChecked(true);
                rd0.setChecked(false);
                txt1.setTextColor(Color.parseColor("#DEDCDE"));
                txt2.setTextColor(Color.parseColor("#DEDCDE"));
                txt3.setTextColor(Color.parseColor("#DEDCDE"));
                txt4.setTextColor(Color.parseColor("#DEDCDE"));
                txt5.setTextColor(Color.parseColor("#333333"));
                txt0.setTextColor(Color.parseColor("#DEDCDE"));
                break;
            }
            case 6: {
                rd1.setChecked(false);
                rd2.setChecked(false);
                rd3.setChecked(false);
                rd4.setChecked(false);
                rd5.setChecked(false);
                rd0.setChecked(true);
                txt1.setTextColor(Color.parseColor("#DEDCDE"));
                txt2.setTextColor(Color.parseColor("#DEDCDE"));
                txt3.setTextColor(Color.parseColor("#DEDCDE"));
                txt4.setTextColor(Color.parseColor("#DEDCDE"));
                txt5.setTextColor(Color.parseColor("#DEDCDE"));
                txt0.setTextColor(Color.parseColor("#333333"));
                break;
            }
        }

        if (Utils.CHANNEL_FLAG.equals("1")){
            rdoSound.setChecked(true);
            rdoVibration.setChecked(false);
            rdoSoundVibration.setChecked(false);
            rdoSound.setTextColor(Color.parseColor("#333333"));
            rdoVibration.setTextColor(Color.parseColor("#DEDCDE"));
            rdoSoundVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSound.setTextColor(Color.parseColor("#333333"));
            txtVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSoundAndVib.setTextColor(Color.parseColor("#DEDCDE"));
        }else if (Utils.CHANNEL_FLAG.equals("2")){
            rdoSound.setChecked(false);
            rdoVibration.setChecked(true);
            rdoSoundVibration.setChecked(false);

            rdoSound.setTextColor(Color.parseColor("#DEDCDE"));
            rdoVibration.setTextColor(Color.parseColor("#333333"));
            rdoSoundVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSound.setTextColor(Color.parseColor("#DEDCDE"));
            txtVibration.setTextColor(Color.parseColor("#333333"));
            txtSoundAndVib.setTextColor(Color.parseColor("#DEDCDE"));
        }else if (Utils.CHANNEL_FLAG.equals("3")){
            rdoSound.setChecked(false);
            rdoVibration.setChecked(false);
            rdoSoundVibration.setChecked(true);
            rdoSound.setTextColor(Color.parseColor("#DEDCDE"));
            rdoVibration.setTextColor(Color.parseColor("#DEDCDE"));
            rdoSoundVibration.setTextColor(Color.parseColor("#333333"));
            txtSound.setTextColor(Color.parseColor("#DEDCDE"));
            txtVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSoundAndVib.setTextColor(Color.parseColor("#333333"));
        }else{
            rdoSound.setChecked(true);
            rdoVibration.setChecked(false);
            rdoSoundVibration.setChecked(false);
            rdoSound.setTextColor(Color.parseColor("#333333"));
            rdoVibration.setTextColor(Color.parseColor("#DEDCDE"));
            rdoSoundVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSound.setTextColor(Color.parseColor("#333333"));
            txtVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSoundAndVib.setTextColor(Color.parseColor("#DEDCDE"));
        }

        rd0.setOnCheckedChangeListener(rdcheck);
        rd1.setOnCheckedChangeListener(rdcheck);
        rd2.setOnCheckedChangeListener(rdcheck);
        rd3.setOnCheckedChangeListener(rdcheck);
        rd4.setOnCheckedChangeListener(rdcheck);
        rd5.setOnCheckedChangeListener(rdcheck);

        rdoSound.setOnCheckedChangeListener(rdcheck);
        rdoVibration.setOnCheckedChangeListener(rdcheck);
        rdoSoundVibration.setOnCheckedChangeListener(rdcheck);

        txtSave.setOnClickListener(this);

//        frameSound.setOnClickListener(this);
//        frameVibration.setOnClickListener(this);
//        frameSVibration.setOnClickListener(this);

//        frame0.setOnClickListener(this);
//        frame1.setOnClickListener(this);
//        frame2.setOnClickListener(this);
//        frame3.setOnClickListener(this);
//        frame4.setOnClickListener(this);
//        frame5.setOnClickListener(this);

//        linearSoundAndVibrator.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//                intent.putExtra(Settings.EXTRA_CHANNEL_ID,SOUND);
//                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
//                startActivity(intent);
//            }
//        });

        linearPopupCheckParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onObtainingPermissionOverlayWindow();
            }
        });

        //add event
        swhPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("push_on",isChecked);
                editor.commit();
            }
        });
        swhSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("sound_on",isChecked);
                editor.commit();
            }
        });

        txtSelectAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showRingtonePickerDialog();
//                showRadioButtonDialog();
                final View innerView = getLayoutInflater().inflate(R.layout.ringtone_radio_btn_list_dialog, null);
                final Dialog dialog = new Dialog(SettingActivity.this);
                dialog.setContentView(innerView);
                TextView txtCancel = (TextView)dialog.findViewById(R.id.btn_dialog_cancel);
                TextView txtDone = (TextView)dialog.findViewById(R.id.btn_dialog_done);

                final List<String> stringList=new ArrayList<>();

                stringList.add("sound1");
                stringList.add("sound2");
                stringList.add("sound3");
                stringList.add("sound4");
                stringList.add("sound5");
                stringList.add("sound6");

//                for(int i=0;i<uriArrayList.size();i++) {
//                    stringList.add(ringtoneTitleList.get(i).toString());
//                }

                RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_ringtone_dialog_rdg);

                final String[] ringtonePath = {""};
                final String[] titleName = {""};

                for(int i=0;i<stringList.size();i++){

                    RadioButton rb=new RadioButton(SettingActivity.this);
                    rb.setText(stringList.get(i));
                    rg.addView(rb);

                    if (txtSelectAlarm.getText().toString().contains(stringList.get(i))){
                        rb.setChecked(true);
                    }

                    final int finalI = i;
                    rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                            if (isChecked) {
                                ringtonePath[0] = "android.resource://"+getPackageName()+"/raw/"+stringList.get(finalI);
                                titleName[0] = buttonView.getText().toString();

                                stopPlaying();
                                AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
                                player = MediaPlayer.create(SettingActivity.this, Uri.parse("android.resource://"+getPackageName()+"/raw/"+stringList.get(finalI) + "_5s"));
                                player.setVolume(100,100);
                                player.start();
                            }
                        }
                    });
                }

                Display display = getWindowManager().getDefaultDisplay();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

                dialog.show();

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopPlaying();
                        dialog.dismiss();
                    }
                });
                txtDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopPlaying();

                        if (ringtonePath[0].length() != 0){
                            txtSelectAlarm.setText(titleName[0]);
                            editor.putString("ringtone_path", ringtonePath[0]);
                            editor.putString("title_name",titleName[0]);
                            editor.commit();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });


        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker(1);

            }
        });
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker(2);
            }
        });

//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

//        RingtoneManager ringtoneManager = new RingtoneManager(this);
//        ringtoneManager.setType(RingtoneManager.TYPE_NOTIFICATION);
//        Cursor cursor = ringtoneManager.getCursor();
//        Uri ringtoneInsert = Uri.parse("android.resource://" + getPackageName() +"/raw/test");
//        uriArrayList.add(ringtoneInsert);
//        ringtoneTitleList.add("Alarm");
//        ringtoneIdList.add("");
//        if (cursor != null){
//            if (cursor.moveToFirst()){
//                do {
//                    Uri ringtoneUri = Uri.parse(cursor.getString(RingtoneManager.TYPE_NOTIFICATION));
//                    long id = cursor.getLong(RingtoneManager.ID_COLUMN_INDEX);
//                    String title1 = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
//                    uriArrayList.add(ringtoneUri);
//                    ringtoneTitleList.add(title1);
//                    ringtoneIdList.add(String.valueOf(id));
//
//                }while (cursor.moveToNext());
//            }
//        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void startOverlayWindowService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(context)) {
            onObtainingPermissionOverlayWindow();
        } else {
//            onStartOverlay();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onObtainingPermissionOverlayWindow() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1234);
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1234);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.i(TAG,"code : " + requestCode);

//        if (Settings.canDrawOverlays(this)) {
//            swhPopup.setChecked(true);
//            editor.putBoolean("popup_on",true);
//            editor.commit();
//        } else {
//            swhPopup.setChecked(false);
//            editor.putBoolean("popup_on",false);
//            editor.commit();
//        }
    }

    private RadioButton.OnCheckedChangeListener rdcheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(isChecked == true){
                if(buttonView.getId() == R.id.radio_setting_1){
                    editor.putInt("repeat_cycle",1);
                    rd1.setChecked(true);
                    rd2.setChecked(false);
                    rd3.setChecked(false);
                    rd4.setChecked(false);
                    rd5.setChecked(false);
                    rd0.setChecked(false);

                    txt1.setTextColor(Color.parseColor("#333333"));
                    txt2.setTextColor(Color.parseColor("#DEDCDE"));
                    txt3.setTextColor(Color.parseColor("#DEDCDE"));
                    txt4.setTextColor(Color.parseColor("#DEDCDE"));
                    txt5.setTextColor(Color.parseColor("#DEDCDE"));
                    txt0.setTextColor(Color.parseColor("#DEDCDE"));
                }else if(buttonView.getId() == R.id.radio_setting_2){
                    editor.putInt("repeat_cycle",2);
                    rd1.setChecked(false);
                    rd2.setChecked(true);
                    rd3.setChecked(false);
                    rd4.setChecked(false);
                    rd5.setChecked(false);
                    rd0.setChecked(false);

                    txt1.setTextColor(Color.parseColor("#DEDCDE"));
                    txt2.setTextColor(Color.parseColor("#333333"));
                    txt3.setTextColor(Color.parseColor("#DEDCDE"));
                    txt4.setTextColor(Color.parseColor("#DEDCDE"));
                    txt5.setTextColor(Color.parseColor("#DEDCDE"));
                    txt0.setTextColor(Color.parseColor("#DEDCDE"));
                }else if(buttonView.getId() == R.id.radio_setting_3){
                    editor.putInt("repeat_cycle",3);
                    rd1.setChecked(false);
                    rd2.setChecked(false);
                    rd3.setChecked(true);
                    rd4.setChecked(false);
                    rd5.setChecked(false);
                    rd0.setChecked(false);

                    txt1.setTextColor(Color.parseColor("#DEDCDE"));
                    txt2.setTextColor(Color.parseColor("#DEDCDE"));
                    txt3.setTextColor(Color.parseColor("#333333"));
                    txt4.setTextColor(Color.parseColor("#DEDCDE"));
                    txt5.setTextColor(Color.parseColor("#DEDCDE"));
                    txt0.setTextColor(Color.parseColor("#DEDCDE"));
                }else if(buttonView.getId() == R.id.radio_setting_4){
                    editor.putInt("repeat_cycle",4);
                    rd1.setChecked(false);
                    rd2.setChecked(false);
                    rd3.setChecked(false);
                    rd4.setChecked(true);
                    rd5.setChecked(false);
                    rd0.setChecked(false);

                    txt1.setTextColor(Color.parseColor("#DEDCDE"));
                    txt2.setTextColor(Color.parseColor("#DEDCDE"));
                    txt3.setTextColor(Color.parseColor("#DEDCDE"));
                    txt4.setTextColor(Color.parseColor("#333333"));
                    txt5.setTextColor(Color.parseColor("#DEDCDE"));
                    txt0.setTextColor(Color.parseColor("#DEDCDE"));
                }else if(buttonView.getId() == R.id.radio_setting_5){
                    editor.putInt("repeat_cycle",5);
                    rd1.setChecked(false);
                    rd2.setChecked(false);
                    rd3.setChecked(false);
                    rd4.setChecked(false);
                    rd5.setChecked(true);
                    rd0.setChecked(false);

                    txt1.setTextColor(Color.parseColor("#DEDCDE"));
                    txt2.setTextColor(Color.parseColor("#DEDCDE"));
                    txt3.setTextColor(Color.parseColor("#DEDCDE"));
                    txt4.setTextColor(Color.parseColor("#DEDCDE"));
                    txt5.setTextColor(Color.parseColor("#333333"));
                    txt0.setTextColor(Color.parseColor("#DEDCDE"));
                }else if (buttonView.getId() == R.id.radio_setting_0){
                    editor.putInt("repeat_cycle",6);
                    rd1.setChecked(false);
                    rd2.setChecked(false);
                    rd3.setChecked(false);
                    rd4.setChecked(false);
                    rd5.setChecked(false);
                    rd0.setChecked(true);

                    txt1.setTextColor(Color.parseColor("#DEDCDE"));
                    txt2.setTextColor(Color.parseColor("#DEDCDE"));
                    txt3.setTextColor(Color.parseColor("#DEDCDE"));
                    txt4.setTextColor(Color.parseColor("#DEDCDE"));
                    txt5.setTextColor(Color.parseColor("#DEDCDE"));
                    txt0.setTextColor(Color.parseColor("#333333"));
                }else if (buttonView.getId() == R.id.rdo_setting_sound){
                    editor.putInt("setting_sound",1);
                    rdoSound.setChecked(true);
                    rdoVibration.setChecked(false);
                    rdoSoundVibration.setChecked(false);
                    rdoSound.setTextColor(Color.parseColor("#333333"));
                    rdoVibration.setTextColor(Color.parseColor("#DEDCDE"));
                    rdoSoundVibration.setTextColor(Color.parseColor("#DEDCDE"));
                    txtSound.setTextColor(Color.parseColor("#333333"));
                    txtVibration.setTextColor(Color.parseColor("#DEDCDE"));
                    txtSoundAndVib.setTextColor(Color.parseColor("#DEDCDE"));


                }else if (buttonView.getId() == R.id.rdo_setting_vibration){
                    editor.putInt("setting_sound",2);
                    rdoSound.setChecked(false);
                    rdoVibration.setChecked(true);
                    rdoSoundVibration.setChecked(false);
                    rdoSound.setTextColor(Color.parseColor("#DEDCDE"));
                    rdoVibration.setTextColor(Color.parseColor("#333333"));
                    rdoSoundVibration.setTextColor(Color.parseColor("#DEDCDE"));
                    txtSound.setTextColor(Color.parseColor("#DEDCDE"));
                    txtVibration.setTextColor(Color.parseColor("#333333"));
                    txtSoundAndVib.setTextColor(Color.parseColor("#DEDCDE"));


                }else if (buttonView.getId() == R.id.rdo_setting_sound_vibration){
                    editor.putInt("setting_sound",3);
                    rdoSound.setChecked(false);
                    rdoVibration.setChecked(false);
                    rdoSoundVibration.setChecked(true);
                    rdoSound.setTextColor(Color.parseColor("#DEDCDE"));
                    rdoVibration.setTextColor(Color.parseColor("#DEDCDE"));
                    rdoSoundVibration.setTextColor(Color.parseColor("#333333"));
                    txtSound.setTextColor(Color.parseColor("#DEDCDE"));
                    txtVibration.setTextColor(Color.parseColor("#DEDCDE"));
                    txtSoundAndVib.setTextColor(Color.parseColor("#333333"));


                }
                editor.commit();
            }
        }
    };

    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    void DateTimePicker(final int mode){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(SettingActivity.this);
        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);
        SimpleDateFormat simpleNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date now = null;

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayDaysOfMonth(false);
        singleDateAndTimePicker.setDisplayDays(false);

        try{
            if (mode == 1 && timeStart.getText().length() != 0){
                now = simpleNow.parse(simpleNow.format(new Date(System.currentTimeMillis())).substring(0,10) + " " + timeStart.getText().toString());
                singleDateAndTimePicker.setDefaultDate(now);
            }else if (mode == 2 && timeEnd.getText().length() != 0){
                now = simpleNow.parse(simpleNow.format(new Date(System.currentTimeMillis())).substring(0,10) + " " + timeEnd.getText().toString());
                singleDateAndTimePicker.setDefaultDate(now);
            }
        }catch (ParseException e){

        }

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                Log.i(TAG,"test : " + date);


                selectDate = simpleDateFormat.format(date);
                clickDate = date;

                if (mode == 1){
                    timeStart.setText(selectDate);
                    PUSH_START_TIME = selectDate;

                }else if (mode == 2){
                    timeEnd.setText(selectDate);
                    PUSH_END_TIME = selectDate;
                }
                dateTimeDialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showRingtonePickerDialog() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone for notifications:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);

        startActivityForResult(intent, 999);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clickDate = null;
        selectDate = "";
        stopPlaying();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.frame_setting_1){
            editor.putInt("repeat_cycle",1);
            rd1.setChecked(true);
            rd2.setChecked(false);
            rd3.setChecked(false);
            rd4.setChecked(false);
            rd5.setChecked(false);
            rd0.setChecked(false);

            txt1.setTextColor(Color.parseColor("#333333"));
            txt2.setTextColor(Color.parseColor("#DEDCDE"));
            txt3.setTextColor(Color.parseColor("#DEDCDE"));
            txt4.setTextColor(Color.parseColor("#DEDCDE"));
            txt5.setTextColor(Color.parseColor("#DEDCDE"));
            txt0.setTextColor(Color.parseColor("#DEDCDE"));
        }else if(view.getId() == R.id.frame_setting_2){
            editor.putInt("repeat_cycle",2);
            rd1.setChecked(false);
            rd2.setChecked(true);
            rd3.setChecked(false);
            rd4.setChecked(false);
            rd5.setChecked(false);
            rd0.setChecked(false);

            txt1.setTextColor(Color.parseColor("#DEDCDE"));
            txt2.setTextColor(Color.parseColor("#333333"));
            txt3.setTextColor(Color.parseColor("#DEDCDE"));
            txt4.setTextColor(Color.parseColor("#DEDCDE"));
            txt5.setTextColor(Color.parseColor("#DEDCDE"));
            txt0.setTextColor(Color.parseColor("#DEDCDE"));
        }else if(view.getId() == R.id.frame_setting_3){
            editor.putInt("repeat_cycle",3);
            rd1.setChecked(false);
            rd2.setChecked(false);
            rd3.setChecked(true);
            rd4.setChecked(false);
            rd5.setChecked(false);
            rd0.setChecked(false);

            txt1.setTextColor(Color.parseColor("#DEDCDE"));
            txt2.setTextColor(Color.parseColor("#DEDCDE"));
            txt3.setTextColor(Color.parseColor("#333333"));
            txt4.setTextColor(Color.parseColor("#DEDCDE"));
            txt5.setTextColor(Color.parseColor("#DEDCDE"));
            txt0.setTextColor(Color.parseColor("#DEDCDE"));
        }else if(view.getId() == R.id.frame_setting_4){
            editor.putInt("repeat_cycle",4);
            rd1.setChecked(false);
            rd2.setChecked(false);
            rd3.setChecked(false);
            rd4.setChecked(true);
            rd5.setChecked(false);
            rd0.setChecked(false);

            txt1.setTextColor(Color.parseColor("#DEDCDE"));
            txt2.setTextColor(Color.parseColor("#DEDCDE"));
            txt3.setTextColor(Color.parseColor("#DEDCDE"));
            txt4.setTextColor(Color.parseColor("#333333"));
            txt5.setTextColor(Color.parseColor("#DEDCDE"));
            txt0.setTextColor(Color.parseColor("#DEDCDE"));
        }else if(view.getId() == R.id.frame_setting_5){
            editor.putInt("repeat_cycle",5);
            rd1.setChecked(false);
            rd2.setChecked(false);
            rd3.setChecked(false);
            rd4.setChecked(false);
            rd5.setChecked(true);
            rd0.setChecked(false);

            txt1.setTextColor(Color.parseColor("#DEDCDE"));
            txt2.setTextColor(Color.parseColor("#DEDCDE"));
            txt3.setTextColor(Color.parseColor("#DEDCDE"));
            txt4.setTextColor(Color.parseColor("#DEDCDE"));
            txt5.setTextColor(Color.parseColor("#333333"));
            txt0.setTextColor(Color.parseColor("#DEDCDE"));
        }else if (view.getId() == R.id.frame_setting_0){
            editor.putInt("repeat_cycle",6);
            rd1.setChecked(false);
            rd2.setChecked(false);
            rd3.setChecked(false);
            rd4.setChecked(false);
            rd5.setChecked(false);
            rd0.setChecked(true);

            txt1.setTextColor(Color.parseColor("#DEDCDE"));
            txt2.setTextColor(Color.parseColor("#DEDCDE"));
            txt3.setTextColor(Color.parseColor("#DEDCDE"));
            txt4.setTextColor(Color.parseColor("#DEDCDE"));
            txt5.setTextColor(Color.parseColor("#DEDCDE"));
            txt0.setTextColor(Color.parseColor("#333333"));
        } else if (view.getId() == R.id.frame_setting_sound){
            editor.putInt("setting_sound",1);
            rdoSound.setChecked(true);
            rdoVibration.setChecked(false);
            rdoSoundVibration.setChecked(false);
            rdoSound.setTextColor(Color.parseColor("#333333"));
            rdoVibration.setTextColor(Color.parseColor("#DEDCDE"));
            rdoSoundVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSound.setTextColor(Color.parseColor("#333333"));
            txtVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSoundAndVib.setTextColor(Color.parseColor("#DEDCDE"));
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Uri alarmSound;
            if(pref.getString("ringtone_path","").length()==0){
                alarmSound = Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_5s");
            }else{
                alarmSound = Uri.parse(pref.getString("ringtone_path",""));
            }
            if (pref.getBoolean("sound_on",true)){
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
            }else{
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(SettingActivity.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("G-tron System")
                    .setAutoCancel(true)
                    .setVibrate(null)
                    .setSound(alarmSound)
                    .setStyle(new NotificationCompat.InboxStyle());
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }else if (view.getId() == R.id.frame_setting_vibration){
            editor.putInt("setting_sound",2);
            rdoSound.setChecked(false);
            rdoVibration.setChecked(true);
            rdoSoundVibration.setChecked(false);
            rdoSound.setTextColor(Color.parseColor("#DEDCDE"));
            rdoVibration.setTextColor(Color.parseColor("#333333"));
            rdoSoundVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSound.setTextColor(Color.parseColor("#DEDCDE"));
            txtVibration.setTextColor(Color.parseColor("#333333"));
            txtSoundAndVib.setTextColor(Color.parseColor("#DEDCDE"));

            stopPlaying();
            long[] pattern = {500, 500};
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(SettingActivity.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("G-tron System")
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setStyle(new NotificationCompat.InboxStyle());
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if(pref.getBoolean("push_on",true)){
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                Log.i(TAG,"sendNotification - notify");
            }
        }else if (view.getId() == R.id.frame_setting_sound_vibration){
            editor.putInt("setting_sound",3);
            rdoSound.setChecked(false);
            rdoVibration.setChecked(false);
            rdoSoundVibration.setChecked(true);
            rdoSound.setTextColor(Color.parseColor("#DEDCDE"));
            rdoVibration.setTextColor(Color.parseColor("#DEDCDE"));
            rdoSoundVibration.setTextColor(Color.parseColor("#333333"));
            txtSound.setTextColor(Color.parseColor("#DEDCDE"));
            txtVibration.setTextColor(Color.parseColor("#DEDCDE"));
            txtSoundAndVib.setTextColor(Color.parseColor("#333333"));
            Uri alarmSound;
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            if(pref.getString("ringtone_path","").length()==0 ){
                alarmSound = Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_5s");
            }else{
                alarmSound = Uri.parse(pref.getString("ringtone_path",""));
            }
            if (pref.getBoolean("sound_on",true)){
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
            }else{
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioManager.getStreamVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
            }

            long[] pattern = {500, 500};
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(SettingActivity.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("G-tron System")
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setSound(alarmSound)
                    .setStyle(new NotificationCompat.InboxStyle());
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }else if (view.getId() == R.id.txt_setting_save){
            new SettingSaveNetworkTask().execute();
        }
        editor.commit();
    }

    public class SettingSaveNetworkTask extends AsyncTask<String, String, String> {

        public ProgressDialog progressDialog;
        String ssss = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SettingActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("saving...");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.updateSettingM());

            http.addOrReplace("userNo",String.valueOf(Utils.USER_NO));
            http.addOrReplace("pushStartTime",timeStart.getText().toString());
            http.addOrReplace("pushEndTime",timeEnd.getText().toString());
            if (rdo1s.isChecked()){
                ssss = "_1s";
            }else if (rdo5s.isChecked()){
                ssss = "_5s";
            }else if (rdo10s.isChecked()){
                ssss = "_10s";
            }else if (rdo20s.isChecked()){
                ssss = "_20s";
            }
            http.addOrReplace("sound",txtSelectAlarm.getText().toString() + ssss + ".m4a");
            http.addOrReplace("popupFlag","1");
            http.addOrReplace("channelFlag","2");
//            if (rdoSound.isChecked()){
//
//            }else if (rdoVibration.isChecked()){
//                http.addOrReplace("channelFlag","2");
//            }else if (rdoSoundVibration.isChecked()){
//                http.addOrReplace("channelFlag","3");
//            }

            HttpClient post = http.create();

            post.request();

            String body = post.getBody();

            Log.i(TAG,"SettingSaveNetworkTask = " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            new LoginNetworkTask(progressDialog).execute();
        }
    }

    public class LoginNetworkTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        public LoginNetworkTask(ProgressDialog progressDialog) {
            this.progressDialog = progressDialog;
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.LoginProcess());

            http.addOrReplace("status","android");
            http.addOrReplace("email",Utils.USER_EMAIL);
            http.addOrReplace("password",pref.getString("userPw",""));

            HttpClient post = http.create();

            post.request();

            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"LoginNetworkTask s = " + s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("result")){
                    if (jsonObject.getInt("LEVEL") == 1){

                        Utils.USER_EMAIL = jsonObject.getString("USER_EMAIL");
                        Utils.USER_PHONE = jsonObject.getString("PHONE");
                        Utils.USER_NAME = jsonObject.getString("USER_NAME");
                        Utils.USER_LEVEL = jsonObject.getInt("LEVEL");
                        Utils.USER_SITE_NO = jsonObject.getInt("SITE_NO");
                        Utils.USER_NO = jsonObject.getInt("USER_NO");
                        SMS_ENABLE = jsonObject.getInt("SMS_ENABLE");
                        Utils.SITE_SMS_ENABLE = jsonObject.getInt("SITE_SMS_ENABLE");
                        SMS_CERT = jsonObject.getInt("SMS_CERT");
                        Utils.SMS_RECEIVE = jsonObject.getInt("SMS_RECEIVE");
                        Utils.MONITORING = jsonObject.getInt("MONITORING");
                        Utils.LOGO_PATH = Utils.JsonIsNullCheck(jsonObject,"LOGO_PATH_1");
                        Utils.PUSH_START_TIME = Utils.JsonIsNullCheck(jsonObject,"PUSH_START_TIME");
                        Utils.PUSH_END_TIME = Utils.JsonIsNullCheck(jsonObject,"PUSH_END_TIME");
                        Utils.PUSH_FLAG = Utils.JsonIsNullCheck(jsonObject,"PUSH_FLAG");
                        Utils.POPUP_FLAG = Utils.JsonIsNullCheck(jsonObject,"POPUP_FLAG");
                        Utils.SOUND = Utils.JsonIsNullCheck(jsonObject,"SOUND");
                        Utils.CHANNEL_FLAG = Utils.JsonIsNullCheck(jsonObject,"CHANNEL_FLAG");

                        editor.putString("PUSH_START_TIME",Utils.PUSH_START_TIME);
                        editor.putString("PUSH_END_TIME",Utils.PUSH_END_TIME);
                        editor.putString("SOUND",Utils.SOUND);
                        editor.putString("CHANNEL_FLAG",Utils.CHANNEL_FLAG );
                        editor.apply();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialog.dismiss();
        }
    }
}
