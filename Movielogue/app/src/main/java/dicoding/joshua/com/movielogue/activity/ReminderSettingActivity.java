package dicoding.joshua.com.movielogue.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.reminder.DailyReminderReceiver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class ReminderSettingActivity extends AppCompatActivity{

    private TextView tvDaily, tvRelease;
    private SwitchCompat swDaily, swRelease;
    private static String wakeTime = new String();
    private static String notifMsg = new String();


    private static String wakeTimeRelease = new String();
    private static String notifMsgRelase = new String();
    private int jobId = 10;

    private static DailyReminderReceiver dailyReminderReceiver;
    private static final String PREF_NAME = "PREF";
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public void setReminders(Context context){
        swDaily = findViewById(R.id.switch_daily);
        swRelease = findViewById(R.id.switch_release);
        prefs = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor = prefs.edit();
        swDaily.setChecked(loadStateSwDaily(this));
        swRelease.setChecked(loadStateSwRelease(this));
        notifMsg = getString(R.string.app_name)+context.getString(R.string.reminder_message);
        dailyReminderReceiver = new DailyReminderReceiver();
        Log.v("cobarelease",loadStateSwRelease(this)+"PREF");
        if(swDaily.isChecked()){
            dailyReminderReceiver.setRepeatingReminder(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING,notifMsg);
            Toast.makeText(context, context.getString(R.string.daily_reminder_set_up), Toast.LENGTH_SHORT).show();
            saveStateSwDaily(context,true);
        }else{
            dailyReminderReceiver.cancelAlarm(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING);
            saveStateSwDaily(context,false);
        }
        if(swRelease.isChecked()){
            dailyReminderReceiver.setRepeatingReleaseToday(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING_RELEASE_TODAY);
            Toast.makeText(context, context.getString(R.string.relase_reminder_set_up), Toast.LENGTH_SHORT).show();
            saveStateSwRelease(context,true);
        }else{
            dailyReminderReceiver.cancelAlarm(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING_RELEASE_TODAY);
            saveStateSwRelease(context,false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);

        tvDaily = findViewById(R.id.tv_daily_reminder);
        tvRelease = findViewById(R.id.tv_release_reminder);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setReminders(this);

        swRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    Log.v("cobarelease","true");
                    dailyReminderReceiver.setRepeatingReleaseToday(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING_RELEASE_TODAY);
                    Toast.makeText(getApplicationContext(), getString(R.string.relase_reminder_set_up), Toast.LENGTH_SHORT).show();
                    saveStateSwRelease(getApplicationContext(),true);
                }else{
                    // turn off
                    dailyReminderReceiver.cancelAlarm(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING_RELEASE_TODAY);
                    saveStateSwRelease(getApplicationContext(),false);
                }
            }
        });

        swDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    Log.v("cobaalarm","daily true");
                    dailyReminderReceiver.setRepeatingReminder(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING,notifMsg);
                    saveStateSwDaily(getApplicationContext(),true);
                    Toast.makeText(getApplicationContext(), getString(R.string.daily_reminder_set_up), Toast.LENGTH_SHORT).show();
                }else{
                    // turn off
                    dailyReminderReceiver.cancelAlarm(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING);
                    saveStateSwDaily(getApplicationContext(),false);
                }
            }
        });

    }

    private void saveStateSwDaily(Context context, boolean isToggled){
        editor.putBoolean("swdaily",isToggled).apply();
    }
    private Boolean loadStateSwDaily(Context context){
        return prefs.getBoolean("swdaily", true);
    }

    private void saveStateSwRelease(Context context, boolean isToggled) {
        Log.v("cobarelease","saveRelease"+isToggled);
        editor.putBoolean("swrelease",isToggled).apply();
        Log.v("cobarelease",loadStateSwRelease(this)+"TEST PREF");
    }
    private Boolean loadStateSwRelease(Context context){
        Log.v("cobarelease","loadRelease");
        return prefs.getBoolean("swrelease", true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
