package gtron.com.gtronsystem.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kmw on 2017. 11. 5..
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Intent service_intent = new Intent(context,AlarmService.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            context.startForegroundService(service_intent);
        }else{
            context.startService(service_intent);
        }
    }
}
