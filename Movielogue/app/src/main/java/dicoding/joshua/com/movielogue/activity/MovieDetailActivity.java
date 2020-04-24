package dicoding.joshua.com.movielogue.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import dicoding.joshua.com.movielogue.widget.FavoriteMovieWidget;
import dicoding.joshua.com.movielogue.adapter.FavoriteAdapter;
import dicoding.joshua.com.movielogue.model.Movie;
import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.db.DatabaseContract;
import dicoding.joshua.com.movielogue.db.FavoriteHelper;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {

    private FavoriteHelper favoriteHelper;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public static final String EXTRA_MOVIE = "extra_movie";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvDuration = findViewById(R.id.tv_duration);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvGenre = findViewById(R.id.tv_genre);
        ImageView ivPoster = findViewById(R.id.iv_movie_poster);
        final ToggleButton tbFav = findViewById(R.id.tb_fav);

        final Movie mov = getIntent().getParcelableExtra(EXTRA_MOVIE);
        Log.v("coba",mov.getId());


        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();



        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        final int pos = getIntent().getExtras().getInt("pos");
        final String type = getIntent().getExtras().getString("type");
        if(type.equals("movie")){
            setTitle(getResources().getString(R.string.movie_detail_titles));
        }else
            setTitle(getResources().getString(R.string.tvshow_detail_title));

        String text = mov.getTitle();

        ivPoster.setImageURI(Uri.parse(mov.getPhoto()));
        Glide.with(this).load(mov.getPhoto()).into(ivPoster);

        tvTitle.setText(text);
        text = mov.getScore() + "/10";
        tvScore.setText(text);
        text = getString(R.string.duration) +'\n'+ mov.getDuration();
        tvDuration.setText(text);
        tvDescription.setText(mov.getDescription());
        tvGenre.setText(mov.getGenres());

        final Boolean[] checkDB = {favoriteHelper.checkIfExists(mov.getId())};
        tbFav.setChecked(checkDB[0]);

        tbFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkDB[0]){
                    // DB
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.FavoriteColumns.TYPE,type);
                    values.put(DatabaseContract.FavoriteColumns.mID,mov.getId());
                    values.put(DatabaseContract.FavoriteColumns.DESCRIPTION,mov.getDescription());
                    values.put(DatabaseContract.FavoriteColumns.DURATION,mov.getDuration());
                    values.put(DatabaseContract.FavoriteColumns.FAV_STATE,mov.getFav_state());
                    values.put(DatabaseContract.FavoriteColumns.GENRES,mov.getGenres());
                    values.put(DatabaseContract.FavoriteColumns.PHOTO,mov.getPhoto());
                    values.put(DatabaseContract.FavoriteColumns.SCORE,mov.getScore());
                    values.put(DatabaseContract.FavoriteColumns.TIITLE,mov.getTitle());

//                    long saved = favoriteHelper.insert(values);
                    // UNTUK CONTENT PROVIDER
                    getContentResolver().insert(DatabaseContract.FavoriteColumns.CONTENT_URI, values);


                    // UNTUK WIDGET
                    FavoriteAdapter fa = new FavoriteAdapter();
                    fa.addItemmDataFav(mov);

//                    Log.v("coba","Saved to DB with id "+saved+", values id : "+(mov.getId()));

//                    if(saved > 0){
                        Toast.makeText(getApplicationContext(),mov.getTitle()+" has been added to favorite "+type+" list",Toast.LENGTH_SHORT).show();
                        // add ke widget
//                        StackRemoteViewsFactory srvf = new StackRemoteViewsFactory(getApplicationContext());
//                        srvf.onDataSetChanged();

                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                                new ComponentName(getApplicationContext(), FavoriteMovieWidget.class));
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);

                        checkDB[0] = true;
//                    }
                }else{
                    // DB
                    long deleted = favoriteHelper.deleteById(mov.getId());
                    Log.v("coba","DELETE from DB with id "+deleted+", values id : "+(mov.getId()));


                    // UNTUK CONTENT PROVIDER
                    getContentResolver().delete(Uri.parse(DatabaseContract.FavoriteColumns.CONTENT_URI+"/"+mov.getId()),null, null);


                    if(deleted > 0){
                        Toast.makeText(getApplicationContext(),mov.getTitle()+" has been removed from favorite "+type+" list",Toast.LENGTH_SHORT).show();
                        checkDB[0] = false;
                    }
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
    }
}
