package dicoding.joshua.com.movielogueconsumer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import dicoding.joshua.com.movielogueconsumer.LoadFavoritesCallback;
import dicoding.joshua.com.movielogueconsumer.R;
import dicoding.joshua.com.movielogueconsumer.model.Movie;
import dicoding.joshua.com.movielogueconsumer.pager.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements LoadFavoritesCallback {

    public void resetAdapter(){
        SectionsPagerAdapter sectionsPagerAdapter = new
                SectionsPagerAdapter(getApplication(),getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v("coba",item+" TESSSTTTT");
        if(item.toString() != getResources().getString(R.string.search) && item.toString() != getResources().getString(R.string.reminder_set)){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = findViewById(R.id.view_pager);
        SectionsPagerAdapter sectionsPagerAdapter = new
                SectionsPagerAdapter(this,getSupportFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs =  findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setTitle("Movielogue Consumer");
        }

    }

    @Override
    public void preExecute() {

    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {

    }
}
