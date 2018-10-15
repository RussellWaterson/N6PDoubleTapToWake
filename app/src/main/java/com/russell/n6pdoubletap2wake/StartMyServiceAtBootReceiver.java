package com.russell.n6pdoubletap2wake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Russell on 27/02/2016.
 */
public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e("BootReceiver", "Boot method");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Log.e("BootReceiver", "Boot command start");
            Intent serviceIntent = new Intent(context, CommandService.class);
            context.startService(serviceIntent);
        }
    }
}
