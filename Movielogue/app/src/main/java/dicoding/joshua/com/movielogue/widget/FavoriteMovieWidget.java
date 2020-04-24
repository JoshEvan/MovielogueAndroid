package dicoding.joshua.com.movielogue.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import dicoding.joshua.com.movielogue.R;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMovieWidget extends AppWidgetProvider {
    private static final String TOAST_ACTION = "dicoding.joshua.com.TOAST_ACTION";
    public static final String EXTRA_ITEM = "dicoding.joshua.com.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId) {
        Log.v("cobawidget","update widget CALLED >>>>>>>>>>>>>>>>");
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
        remoteViews.setRemoteAdapter(R.id.stack_view, intent);
        remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteMovieWidget.class);
        toastIntent.setAction(FavoriteMovieWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        // Instruct the widget manager to update the widget

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
//        Log.v("test","onUpdate");

//        StackRemoteViewsFactory stackRemoteViewsFactory = new StackRemoteViewsFactory(context);
//        stackRemoteViewsFactory.onDataSetChanged();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
//        StackRemoteViewsFactory stackRemoteViewsFactory = new StackRemoteViewsFactory(context);
//        stackRemoteViewsFactory.onDataSetChanged();
        StackRemoteViewsFactory srvf = new StackRemoteViewsFactory(context);
        if(intent.getAction() != null){
            if(intent.getAction().equals(TOAST_ACTION)){
                int viewIdx = intent.getIntExtra(EXTRA_ITEM, 0);
                Toast.makeText(context,context.getString(R.string.banner)+" "+context.getString(R.string.app_name),Toast.LENGTH_SHORT).show();
        }
        }
    }
}

