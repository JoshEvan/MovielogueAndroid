package dicoding.joshua.com.movielogue.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import dicoding.joshua.com.movielogue.LoadFavoritesCallback;
import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.model.MainViewModel;
import dicoding.joshua.com.movielogue.model.Movie;
import dicoding.joshua.com.movielogue.pager.SectionsPagerAdapter;
import dicoding.joshua.com.movielogue.reminder.DailyReminderReceiver;

public class MainActivity extends AppCompatActivity implements LoadFavoritesCallback {

    private DailyReminderReceiver dailyReminderReceiver;
    private static String wakeTime = new String();
    private static String notifMsg = new String();
    private static String wakeTimeRelease = new String();
    private static String notifMsgRelase = new String();

    private static final String PREF_NAME = "PREF";
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;


    public void resetAdapter(){
        SectionsPagerAdapter sectionsPagerAdapter = new
                SectionsPagerAdapter(getApplication(),getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if(searchManager !=null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    MainViewModel.setSearchKeyword(s);
                    resetAdapter();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

        }
        MenuItem item = menu.findItem(R.id.search);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Log.v("coba","RESET SEARCH DISINI");
                MainViewModel.setSearchKeyword("");
                resetAdapter();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v("coba",item+" TESSSTTTT");
        if(item.toString() != getResources().getString(R.string.search) && item.toString() != getResources().getString(R.string.reminder_set)){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }else if(item.toString().equals(getResources().getString(R.string.reminder_set))){
            Intent intent = new Intent(this,ReminderSettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new
                SectionsPagerAdapter(this,getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs =  findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setElevation(0);

        notifMsg = getString(R.string.app_name)+" "+getString(R.string.reminder_message);
        dailyReminderReceiver = new DailyReminderReceiver();
        prefs = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        editor = prefs.edit();

        if(prefs.getBoolean("swdaily", false) == true){
            dailyReminderReceiver.setRepeatingReminder(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING,notifMsg);
        }else{
            dailyReminderReceiver.cancelAlarm(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING);
        }
        if(prefs.getBoolean("swrelease", false) == true){
            dailyReminderReceiver.setRepeatingReleaseToday(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING_RELEASE_TODAY);
        }else{
            dailyReminderReceiver.cancelAlarm(getApplicationContext(),dailyReminderReceiver.TYPE_REPEATING_RELEASE_TODAY);
        }

    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {

    }
}
