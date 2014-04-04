
package org.x4444.app1s;

import org.x4444.app1s.LocationService.MyLocationListener;
import org.x4444.app1s.db.LocationDao;
import org.x4444.app1s.db.PlateDbHelper;

import android.app.Application;
import android.content.Context;

public class App1sApp extends Application {

    public static Context context;

    public static int gpsCnt;

    public static MyLocationListener myLocationListener;

    public static PlateDbHelper dbHelper;

    public static LocationDao locationDao;

    /*
     * (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        App1sApp.context = getApplicationContext();

        dbHelper = new PlateDbHelper(context);
        locationDao = new LocationDao();
    }

    public static Context getAppContext() {
        return App1sApp.context;
    }
}
