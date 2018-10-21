package com.russell.n6pdoubletap2wake;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Russell on 25/02/2016.
 */
public class CommandService extends IntentService {

    public static final String TRANSACTION_DONE = "com.russell.TRANSACTION_DONE";
    public final static String CHANNEL_ID = "1234";

    public CommandService()
    {
        super(CommandService.class.getName());
    }

    public CommandService(String name)
    {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(100, mBuilder.build());

        startForeground(100, mBuilder.build());
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        Log.e("CommandService", "Service Started");

        runCommand();

        Log.e("CommandService", "Service Stopped");

        Intent i = new Intent(TRANSACTION_DONE);

        CommandService.this.sendBroadcast(i);

    }

    protected void runCommand()
    {
        Log.e("CommandService", "Service called!");
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("active", false))
        {
            try
            {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                outputStream.writeBytes("echo 1 > /sys/devices/soc.0/f9924000.i2c/i2c-2/2-0070/input/input0/wake_gesture\n");
                outputStream.flush();

                outputStream.writeBytes("exit\n");
                outputStream.flush();
                su.waitFor();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            Log.e("CommandService", "Command Ran!");
        }
    }
}
