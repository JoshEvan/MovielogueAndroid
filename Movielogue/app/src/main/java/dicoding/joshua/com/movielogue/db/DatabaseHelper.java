package dicoding.joshua.com.movielogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "movielogue";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAVORITES = String.format(
        "CREATE TABLE %s" + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
        " %s TEXT NOT NULL,"+" %s TEXT NOT NULL,"+" %s TEXT NOT NULL,"+" %s TEXT NOT NULL,"+" %s TEXT NOT NULL,"+" %s TEXT NOT NULL,"+" %s TEXT NOT NULL,"+" %s TEXT NOT NULL,"+ " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_NAME,
            DatabaseContract.FavoriteColumns._ID,
            DatabaseContract.FavoriteColumns.mID,
            DatabaseContract.FavoriteColumns.PHOTO,
            DatabaseContract.FavoriteColumns.TIITLE,
            DatabaseContract.FavoriteColumns.DESCRIPTION,
            DatabaseContract.FavoriteColumns.DURATION,
            DatabaseContract.FavoriteColumns.FAV_STATE,
            DatabaseContract.FavoriteColumns.GENRES,
            DatabaseContract.FavoriteColumns.SCORE,
            DatabaseContract.FavoriteColumns.TYPE
            );

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
