package dicoding.joshua.com.movielogue.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import dicoding.joshua.com.movielogue.widget.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v("cobawidget","ongetviewstackviewfactory kepanggil");
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
