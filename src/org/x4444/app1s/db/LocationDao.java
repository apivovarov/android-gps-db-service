
package org.x4444.app1s.db;

import static org.x4444.app1s.db.LocationTable.COLUMN_NAME_KEY;
import static org.x4444.app1s.db.LocationTable.COLUMN_NAME_VALUE;
import static org.x4444.app1s.db.LocationTable.TABLE_NAME;

import java.util.List;

import org.json.JSONObject;
import org.x4444.app1s.App1sApp;
import org.x4444.app1s.Utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

public class LocationDao {

    public synchronized void saveLocation(Location loc) {
        JSONObject o = Utils.getLocationInJson(loc);

        String json = o.toString();
        Log.d("gps", "json: " + json);
        Log.i("gps", "loc.time: " + loc.getTime());
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_KEY, loc.getTime());
        values.put(COLUMN_NAME_VALUE, json);

        SQLiteDatabase db = App1sApp.dbHelper.getWritableDatabase();
        try {
            long rowId = db.insert(TABLE_NAME, null, values);
            Log.i("gps", "rowId: " + rowId);
        } finally {
            db.close();
            Log.d("gps", "db closed");
        }
    }

    /**
     * Adds up to N rown to ArrayList.
     * 
     * @param res
     * @param n
     * @return true is more data available
     */
    public synchronized boolean getFirstNLocations(List<String> res, int n) {
        Log.i("gps", "getFirstNLocations, n: " + n);
        SQLiteDatabase db = App1sApp.dbHelper.getWritableDatabase();
        try {
            String[] columns = new String[] {
                COLUMN_NAME_VALUE
            };

            Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, COLUMN_NAME_KEY);
            boolean exist = c.moveToFirst();
            if (exist) {
                Log.i("gps", "first row exists");
            }
            while (exist && res.size() < n) {
                String v = c.getString(0);
                res.add(v);
                exist = c.moveToNext();
            }
            return exist;
        } finally {
            db.close();
            Log.d("gps", "db closed");
        }
    }

    public synchronized int delLocations(long firstId, long lastId) {
        SQLiteDatabase db = App1sApp.dbHelper.getWritableDatabase();
        String firstIdStr = String.valueOf(firstId);
        String lastIdStr = String.valueOf(lastId);
        String where = COLUMN_NAME_KEY + " >= ? and " + COLUMN_NAME_KEY + " <= ?";
        String[] whereValues = new String[] {
                firstIdStr, lastIdStr
        };
        try {
            int cnt = db.delete(TABLE_NAME, where, whereValues);
            Log.i("gps", "deleted " + cnt);
            return cnt;
        } finally {
            db.close();
            Log.d("gps", "db closed");
        }
    }

    public synchronized int getCount() {
        SQLiteDatabase db = App1sApp.dbHelper.getReadableDatabase();
        try {
            Log.i("gps", "select count");
            Cursor c = db.rawQuery("select " + COLUMN_NAME_KEY + " from " + TABLE_NAME, null);
            if (c != null) {
                int cnt = c.getCount();
                Log.i("gps", "count: " + cnt);
                return cnt;
            }
            return 0;
        } finally {
            db.close();
            Log.d("gps", "db closed");
        }
    }
}
