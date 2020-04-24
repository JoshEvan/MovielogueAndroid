package dicoding.joshua.com.movielogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = DatabaseContract.TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    private FavoriteHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context){
        // init DB
        if(INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if(INSTANCE == null){
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        // open connection ke DB
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        // close connection ke DB
        databaseHelper.close();;
        if(database.isOpen()){
            database.close();
        }
    }

    // CRUD
    // READ
    public Cursor queryAll(){
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID +" ASC"
                );
    }

    public Cursor queryById(String id){
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }



    public static boolean checkIfExists(String id) {

        Cursor cursor = database.query(
                DATABASE_TABLE,
                null,
                DatabaseContract.FavoriteColumns.mID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
        if(cursor.getCount() <= 0){

            Log.v("coba","check -> NOT exist");
            cursor.close();
            return false;
        }
        cursor.close();
        Log.v("coba","check -> exist");
        return true;
    }

    // SAVE DATA
    public long insert(ContentValues values){

        return  database.insert(DATABASE_TABLE, null, values);
    }

    // UPDATE DATA
    public int update(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values, _ID+ " = ?", new String[]{id});
    }

    // DELETE DATA
    public long deleteById(String id){

        return database.delete(DATABASE_TABLE, DatabaseContract.FavoriteColumns.mID + " = ?", new String[]{id});
    }
}
