package com.russell.n6pdoubletap2wake;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean commandRan = false;	//on boot always revert commandRan to false

    public final static String CHANNEL_ID = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(CommandService.TRANSACTION_DONE);

        registerReceiver(commandReceiver, intentFilter);

        createNotificationChannel();

        final SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final TextView text = (TextView) findViewById(R.id.textView);
        final Button button = (Button) findViewById(R.id.button);
        final TextView text2 = (TextView) findViewById(R.id.textView3);

        if (sharedPreferences.getBoolean("active", false))
        {
            text.setText(getString(R.string.active));
            text.setTextColor(Color.GREEN);
            button.setText(getString(R.string.button_off));
        }
        else
        {
            text.setText(getString(R.string.inactive));
            text.setTextColor(Color.RED);
            button.setText(getString(R.string.button_on));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                if (sharedPreferences.getBoolean("active", false))
                {
                    text.setText(getString(R.string.inactive));
                    text.setTextColor(Color.RED);
                    button.setText(getString(R.string.button_on));
                    editor.putBoolean("active", false);
                    editor.commit();
                }
                else
                {
                    text.setText(getString(R.string.active));
                    text.setTextColor(Color.GREEN);
                    button.setText(getString(R.string.button_off));
                    editor.putBoolean("active", true);
                    editor.commit();
                    if (!commandRan)
                    {
//                        enableDT2W();
                        startCommandService(view);
                        commandRan = true;
                    }
                }
            }
        });

        text2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"rwatersondev@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "N6P Double Tap to Wake");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_source_code) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RussellWaterson/N6PDoubleTapToWake"));
            startActivity(browserIntent);

        } else if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.action_about)
                    .setMessage(R.string.about_message)
                    .show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void startCommandService(View view) {

        Intent intent = new Intent(this, CommandService.class);

        this.startService(intent);
    }

    private BroadcastReceiver commandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
//            Log.e("CommandService", "Service Received");
        }
    };

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
