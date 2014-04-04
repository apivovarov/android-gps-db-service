
package org.x4444.app1s.db;

import static org.x4444.app1s.db.LocationTable.COLUMN_NAME_KEY;
import static org.x4444.app1s.db.LocationTable.COLUMN_NAME_VALUE;
import static org.x4444.app1s.db.LocationTable.TABLE_NAME;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlateDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "plate.db";

    private static final String SQL_CREATE_LOCATION = "create table " + TABLE_NAME + " ("
            + COLUMN_NAME_KEY + " INTEGER PRIMARY KEY, " + COLUMN_NAME_VALUE + " TEXT " + ")";

    private static final String SQL_DROP_LOCATION = "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_LOCATION);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public PlateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
