package dicoding.joshua.com.movielogue.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import dicoding.joshua.com.movielogue.db.DatabaseContract;
import dicoding.joshua.com.movielogue.db.FavoriteHelper;

import static dicoding.joshua.com.movielogue.db.DatabaseContract.AUTHORITY;
import static dicoding.joshua.com.movielogue.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static dicoding.joshua.com.movielogue.db.DatabaseContract.TABLE_NAME;

public class FavoriteProvider extends ContentProvider {

//    int sebagai identifier untuk bedain antara select all sama select by id
    private static final int FAV = 1;
    private static final int FAV_ID = 2;
    private FavoriteHelper favoriteHelper;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//    Uri matcher untuk mempermudah identifier dengan menggunakan integer
//    misal
//    uri dicoding.joshua.com.movielogue dicocokan dengan integer 1
//    uri dicoding.joshua.com.movielogue/# dicocokan dengan integer 2
    static {
        // content://dicoding.joshua.com.movielogue/fav
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV); // dari DatabaseContract
        // content://dicoding.joshua.com.movielogue/fav/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                FAV_ID);
    }
    public FavoriteProvider() { }
    @Override
    public boolean onCreate() {
        // Implement this to initialize your content provider on startup.
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();
        return true;
    }
    // untuk select
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Implement this to handle query requests from clients.
        Log.v("cobacontent","query masuk");
        Cursor cursor;
        switch(sUriMatcher.match(uri)){
            case FAV:
                cursor = favoriteHelper.queryAll();
                break;
            case FAV_ID:
                cursor = favoriteHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                Log.v("cobacontent","masuk default");
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Implement this to handle requests to insert a new row.
        long added;
        switch(sUriMatcher.match(uri)){
            case FAV:
                added = favoriteHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null);
        return Uri.parse(DatabaseContract.FavoriteColumns.CONTENT_URI+"/"+added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Implement this to handle requests to update one or more rows.
        int updated;
        switch(sUriMatcher.match(uri)){
            case FAV_ID:
                updated = favoriteHelper.update(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int deleted;
        switch (sUriMatcher.match(uri)){
            case FAV_ID:
                deleted = (int)favoriteHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(DatabaseContract.FavoriteColumns.CONTENT_URI,null);
        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        // Implement this to handle requests for the MIME type of the data at the given URI.
        return null;
    }
}
