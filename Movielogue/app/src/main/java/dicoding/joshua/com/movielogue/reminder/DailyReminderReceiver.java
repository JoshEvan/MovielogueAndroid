package dicoding.joshua.com.movielogue.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.model.MainViewModel;

public class DailyReminderReceiver extends BroadcastReceiver {
    public static final String TYPE_REPEATING_RELEASE_TODAY = "RepeatingRelaseTodayReminder";
    public static final String TYPE_REPEATING = "RepeatingReminder";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private ArrayList<String> releasedMovies;

    String TIME_FORMAT = "HH:mm";

    private final int ID_REPEATING = 101;
    private final int ID_RELEASETODAY = 102;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.v("cobatime","trima broadcast");
        // TODO: This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String type = intent.getStringExtra(EXTRA_TYPE);
        MainViewModel mainViewModel = new MainViewModel();

        int notifId = ID_REPEATING;
        if(type.equals("release")){
            mainViewModel.getReleaseToday(new MainViewModel.MyJSONCallback() {
                @Override
                public void doneDownload(boolean success, ArrayList<String> titles) {
                    releasedMovies = titles;
                    Log.v("cobarelease",releasedMovies+"DISINI");
                    String title = context.getString(R.string.release_title);
                    int masuk = 0;
                    for(int i = 0;i<releasedMovies.size();i++){
                        showNotification(context,title,releasedMovies.get(i), i);
                        masuk = 1;
                    }
                    if(masuk == 0){
                        showNotification(context,title,context.getString(R.string.no_release), ID_REPEATING);
                    }
                }
            });

        }else{
            String title = context.getString(R.string.reminder_title);
            showNotification(context,title,message,notifId);
        }

    }

    public void setRepeatingReminder(Context context, String type, String message) {
        String time = "07:00";
        Log.v("time",time);
        if (isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, "daily");
        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);

        if (alarmManager != null) {
            int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int currMnt = Calendar.getInstance().get(Calendar.MINUTE);
            Log.v("cobatime",currHour+":"+currMnt);
            Log.v("cobatime",calendar.getTimeInMillis()+"");
            Log.v("cobatime",System.currentTimeMillis()+"");

            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                Log.v("cobatime","plus 24 hours");
                calendar.setTimeInMillis(calendar.getTimeInMillis()+(24*60*60*1000));
                // jika udah past alarm nya, set untuk besok aja
            }
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

    }

    public void setRepeatingReleaseToday(Context context, String type) {
        String time = "08:00";
        if (isDateInvalid(time, TIME_FORMAT)) return;
        Log.v("cobarelease","masuk kok");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        intent.putExtra(EXTRA_TYPE, "release");
        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASETODAY, intent, 0);

        if (alarmManager != null) {
            int currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int currMnt = Calendar.getInstance().get(Calendar.MINUTE);
            Log.v("cobatime",currHour+":"+currMnt);
            Log.v("cobatime",calendar.getTimeInMillis()+"");
            Log.v("cobatime",System.currentTimeMillis()+"");

            if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                Log.v("cobatime","plus 24 hours");
                calendar.setTimeInMillis(calendar.getTimeInMillis()+(24*60*60*1000));
                // jika udah past alarm nya, set untuk besok aja
            }
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


    public boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private void showNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "DailyReminder channel";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                Log.v("pushnotif","pushing "+message);
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_REPEATING) ? ID_REPEATING : ID_RELEASETODAY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        String message = (requestCode == ID_RELEASETODAY) ? context.getString(R.string.relase_reminder_turn_off) : context.getString(R.string.daily_reminder_turn_off);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



}
