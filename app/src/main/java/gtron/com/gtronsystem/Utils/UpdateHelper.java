package gtron.com.gtronsystem.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UpdateHelper {

    public static String KEY_UPDATE_ENABLE = "IS_UPDATE";
    public static String KEY_UPDATE_VERSION= "VERSION";

    public interface OnUpdateCheckListener{
        void onUpdateCheckListener(String urlApp);
    }

    public static Builder with(Context context){
        return new Builder(context);
    }

    private OnUpdateCheckListener onUpdateCheckListener;
    private Context context;

    public UpdateHelper(Context context, OnUpdateCheckListener onUpdateCheckListener) {
        this.onUpdateCheckListener = onUpdateCheckListener;
        this.context = context;
    }

    public void check(){
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if (remoteConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            String cureentVersion = remoteConfig.getString(KEY_UPDATE_VERSION);
            String appVersion = getAppVersion(context);

            Log.i("UpdateHelper","version : " + cureentVersion);

            if (!TextUtils.equals(cureentVersion,appVersion) && onUpdateCheckListener != null){
                if (Integer.parseInt(cureentVersion.replace(".","")) > Integer.parseInt(appVersion.replace(".",""))){
                    onUpdateCheckListener.onUpdateCheckListener("true");
                }else{
                    onUpdateCheckListener.onUpdateCheckListener("false");
                }
            }else {
                onUpdateCheckListener.onUpdateCheckListener("false");
            }
        }else{
            onUpdateCheckListener.onUpdateCheckListener("false");
        }
    }

    private String getAppVersion(Context context){
        String result = "";
        try{
            result = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            result = result.replaceAll("[a-zA-Z] | -","");
        }catch (PackageManager.NameNotFoundException e){

        }

        return result;
    }

    public static class Builder{
        private Context context;
        private OnUpdateCheckListener onUpdateCheckListener;

        public Builder(Context context){
            this.context = context;
        }

        public Builder onUpdateCheck(OnUpdateCheckListener onUpdateCheckListener){
            this.onUpdateCheckListener = onUpdateCheckListener;
            return this;
        }

        public UpdateHelper build(){
            return new UpdateHelper(context,onUpdateCheckListener);
        }

        public UpdateHelper check(){
            UpdateHelper updateHelper = build();
            updateHelper.check();;

            return updateHelper;
        }
    }
}
