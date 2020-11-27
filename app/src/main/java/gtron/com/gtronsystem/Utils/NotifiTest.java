package gtron.com.gtronsystem.Utils;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotifiTest extends NotificationListenerService {
    public final static String TAG = "MyNotificationListener";

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

        Log.d(TAG, "onNotificationRemoved ~ " +
                " packageName: " + sbn.getPackageName() +
                " id: " + sbn.getId());
    }

}
