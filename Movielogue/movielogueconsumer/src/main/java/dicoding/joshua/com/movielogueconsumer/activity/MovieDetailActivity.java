package dicoding.joshua.com.movielogueconsumer.activity;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import dicoding.joshua.com.movielogueconsumer.R;
import dicoding.joshua.com.movielogueconsumer.db.DatabaseContract;
import dicoding.joshua.com.movielogueconsumer.model.Movie;


public class MovieDetailActivity extends AppCompatActivity {


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
        text = mov.getScore() + "%";
        tvScore.setText(text);
        text = getString(R.string.duration) +'\n'+ mov.getDuration();
        tvDuration.setText(text);
        tvDescription.setText(mov.getDescription());
        tvGenre.setText(mov.getGenres());
        tbFav.setChecked(true);
        // spy fav button g bisa dipencet
        tbFav.setEnabled(false);
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
