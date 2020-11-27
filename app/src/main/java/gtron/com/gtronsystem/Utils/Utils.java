package gtron.com.gtronsystem.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import gtron.com.gtronsystem.Activity.SplashActivity;

/**
 * Created by euibosim on 2017. 10. 23..
 */

public class Utils {

    public static String CalTime(int hour, int minute){
        String timeStr = "";
        if(hour<10){
            timeStr = "0"+hour+":";
            if(minute<10){
                timeStr += "0"+minute;
            }else{
                timeStr += minute;
            }
        }else{
            timeStr = hour+":";
            if(minute<10){
                timeStr += "0"+minute;
            }else{
                timeStr += minute;
            }
        }
        return timeStr;
    }

    public static String JsonIsNullCheck(JSONObject object, String value){
        try{
            if (object.isNull(value)){
                return "";
            }else{
                return object.getString(value);
            }
        }catch (JSONException e){

        }
        return "";
    }

    public static int JsonIntIsNullCheck(JSONObject object, String value){
        try{
            if (object.isNull(value)){
                return 0;
            }else{
                return object.getInt(value);
            }
        }catch (JSONException e){

        }
        return 0;
    }

    public static String USER_EMAIL = "";
    public static String USER_PHONE = "";
    public static String USER_NAME = null;
    public static String TILE_NO = "";
    public static int USER_LEVEL;
    public static int USER_SITE_NO;
    public static int USER_NO;
    public static int SMS_ENABLE;
    public static int SITE_SMS_ENABLE;
    public static int SMS_CERT;
    public static int SMS_RECEIVE;
    public static int MONITORING;
    public static String SMS_CALLBACK = "";
    public static String LOGO_PATH = "";
    public static String PUSH_START_TIME = "";
    public static String PUSH_END_TIME = "";
    public static String PUSH_FLAG = "";
    public static String POPUP_FLAG = "";
    public static String SOUND = "";
    public static String TOKEN = "";
    public static String CHANNEL_FLAG = "";

    public static void NullCheck(Context context){
        if (USER_NAME == null){
            Intent i = new Intent(context, SplashActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity)context).finish();
            context.startActivity(i);
        }
    }
}
