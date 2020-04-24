package dicoding.joshua.com.movielogueconsumer.helper;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import dicoding.joshua.com.movielogueconsumer.db.DatabaseContract;
import dicoding.joshua.com.movielogueconsumer.model.Movie;

public class MappingHelper {
    public ArrayList<Movie> mapCursorToArrayListMovie (Cursor favoriteCursor){
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        while(favoriteCursor.moveToNext()){
            String type = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TYPE));
            Log.v("coba","get from query all type: "+type);
            if(!type.equals("movie")) continue;
            String id = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.mID));
            String photo = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.PHOTO));
            String desc = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DESCRIPTION));
            String dur = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DURATION));
            String fav_state = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FAV_STATE));
            String genres = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.GENRES));
            String score = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.SCORE));
            String title = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TIITLE));

            Log.v("coba","get from query all id: "+id);
            // get id and req api
            Movie m = new Movie();
            m.setId(id);
            m.setTitle(title);
            m.setPhoto(photo);
            m.setDescription(desc);
            m.setDuration(dur);
            m.setGenres(genres);
            m.setScore(score);
            m.setFav_state(fav_state);
            movieArrayList.add(m);
        }

        Log.v("coba","data di mapping helper");
        for(int i = 0;i<movieArrayList.size();i++){
            Log.v("coba",movieArrayList.get(i).getTitle()+"title");
        }

        Log.v("coba","return array List movie");
        return movieArrayList;
    }

    public ArrayList<Movie> mapCursorToArrayListTVShow (Cursor favoriteCursor){
            ArrayList<Movie> tvShowArrayList = new ArrayList<>();
            while(favoriteCursor.moveToNext()){
                String type = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TYPE));
                Log.v("coba","get from query all type: "+type);
                if(!type.equals("tvshow")) continue;
                String id = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.mID));
                String photo = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.PHOTO));
                String desc = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DESCRIPTION));
                String dur = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DURATION));
                String fav_state = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FAV_STATE));
                String genres = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.GENRES));
                String score = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.SCORE));
                String title = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TIITLE));

                Log.v("coba","get from query all id: "+id);
                // get id and req api
                Movie m = new Movie();
                m.setId(id);
                m.setTitle(title);
                m.setPhoto(photo);
                m.setDescription(desc);
                m.setDuration(dur);
                m.setGenres(genres);
                m.setScore(score);
                m.setFav_state(fav_state);
                tvShowArrayList.add(m);
            }

            Log.v("coba","data di mapping helper");
            for(int i = 0;i<tvShowArrayList.size();i++){
                Log.v("coba",tvShowArrayList.get(i).getTitle()+"title");
            }

            Log.v("coba","return array List tvshow");
            return tvShowArrayList;
        }

    public static Movie mapCursorToObject(Cursor cursor){
        cursor.moveToFirst();
        String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.mID));
        String photo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.PHOTO));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DESCRIPTION));
        String dur = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DURATION));
        String fav_state = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FAV_STATE));
        String genres = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.GENRES));
        String score = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.SCORE));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TIITLE));
        Movie m = new Movie();
        m.setId(id);
        m.setTitle(title);
        m.setPhoto(photo);
        m.setDescription(desc);
        m.setDuration(dur);
        m.setGenres(genres);
        m.setScore(score);
        m.setFav_state(fav_state);
        return m;
    }
}
