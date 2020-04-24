package dicoding.joshua.com.movielogue.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dicoding.joshua.com.movielogue.R;
import dicoding.joshua.com.movielogue.adapter.FavoriteAdapter;
import dicoding.joshua.com.movielogue.db.FavoriteHelper;
import dicoding.joshua.com.movielogue.model.Movie;
import dicoding.joshua.com.movielogue.widget.FavoriteMovieWidget;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Bitmap> pictures = new ArrayList<>();
    private final Context mContext;

    public StackRemoteViewsFactory(Context context) {
        this.mContext = context;
//        for(int i = 0;i<pictures.size();i++) getViewAt(i);
    }

    @Override
    public void onCreate() {
//        pictures.clear();
//        onDataSetChanged();
    }

    @Override
    public void onDataSetChanged() {

        final long identityToken = Binder.clearCallingIdentity();
        FavoriteHelper fh = FavoriteHelper.getInstance(mContext);
        Cursor cursor = null;
        if(fh!=null){
            fh.open();
            cursor = fh.queryAll();
        }
        if(cursor == null) return;
        Log.v("cobacursor","ga null");

        pictures.clear();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            Log.v("cobawidget",cursor.getString(2));
            try {
                Bitmap bitmap = Glide.with(mContext.getApplicationContext())
                        .asBitmap().load(cursor.getString(2))
                        .submit().get();
                pictures.add(bitmap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Binder.restoreCallingIdentity(identityToken);
        Log.v("cobawidget",pictures.size()+"PICS SIZE");
        Log.v("cobawidget","done ONDATASETCHANGED\n=====================================================");
    }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return pictures.size();
    }


    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

//        Log.v("cobawidget","get view at disini set image nya, image "+movieWidgetItems.get(i).getTitle());

        rv.setImageViewBitmap(R.id.iv_poster_widget,pictures.get(i));

//        Bundle extras = new Bundle();
//        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, i);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(FavoriteMovieWidget.EXTRA_ITEM, i);

        rv.setOnClickFillInIntent(R.id.iv_poster_widget,fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}