package gtron.com.gtronsystem.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class BroadcastVib extends BroadcastReceiver {

    String TAG = "BroadcastVib";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG,"aaaaaaasdasdas");

        long[] pattern = {1000  , 1000     , 1000  , 1000     , 1000  ,1000,1000,1000,1000};
        int[]  pattern1 ={10     , 50   , 0     , 50   , 10     , 0   , 10     , 50   ,0   };

        Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Utils.CHANNEL_FLAG.equals("2")){
//            vibrator.vibrate(pattern,-1);
            vibrator.vibrate(VibrationEffect.createWaveform(pattern,pattern1,-1));
        }else  if (Utils.CHANNEL_FLAG.equals("3")){
            vibrator.vibrate(VibrationEffect.createWaveform(pattern,pattern1,-1));
        }
    }
}
