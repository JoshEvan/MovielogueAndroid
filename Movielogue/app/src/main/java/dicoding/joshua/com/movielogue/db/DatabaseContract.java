package dicoding.joshua.com.movielogue.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_NAME = "favorites";

    public static final String AUTHORITY = "dicoding.joshua.com.movielogue";
    private static final String SCHEME = "content";


    public static final class FavoriteColumns implements BaseColumns{
//        public static String FAV_ID = "fav_id";
        public static String TYPE = "type";
        public static String mID = "id";
        public static String PHOTO = "photo";
        public static String TIITLE = "title";
        public static String DESCRIPTION = "description";
        public static String GENRES = "genres";
        public static String SCORE = "score";
        public static String DURATION = "duration";
        public static String FAV_STATE = "fav_state";


        // buat URI content://dicoding.joshua.com.movielogue/favorites
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

}
