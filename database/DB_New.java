package com.nssce_cse.harikrishnanp.attendancebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by VishnuMidhun on 23-07-2016.
 */
public class DB_New {
    public static final String KEY_NEW_ID = "newid";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_VALUE = "value";

    private static final String DATABASE_NAME = "DB_VALUES";
    private static final String DATABASE_TABLE = "TABLE_VALUES";
    private static final int DATABASE_VERSION = 1;

    private final Context ourContext;
    private DbHelper dbh;
    private SQLiteDatabase odb;

    private static final String USER_MASTER_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "("
                    + KEY_NEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CATEGORY + " VARCHAR(50)," + KEY_VALUE +" VARCHAR(100) ) ";
    private static final String PRIMARY_ENTRY = "INSERT INTO " + DATABASE_TABLE + "(" + KEY_CATEGORY + " , " + KEY_VALUE  + ") VALUES ( 'subject','Software Engineering')";

    public DB_New(Context c) {
        ourContext = c;
        dbh = new DbHelper(ourContext);
    }

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(USER_MASTER_CREATE);
            db.execSQL(PRIMARY_ENTRY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // if DATABASE VERSION changes
            // Drop old tables and call super.onCreate()
        }
    }
    public DB_New open() throws SQLException {
        odb = dbh.getWritableDatabase();
        return this;
    }
    public void close() {
        dbh.close();
    }

    public long insertmaster(String col1,String col2){
        Log.d("", col1);
        Log.d("", col2);

        ContentValues IV = new ContentValues();
        IV.put(KEY_CATEGORY, col1);
        IV.put(KEY_VALUE, col2);

        return odb.insert(DATABASE_TABLE, null, IV);
    }
    public Cursor getValues(String cat) {
        // using simple SQL query
        return odb.rawQuery("select " + KEY_VALUE + " from " + DATABASE_TABLE + " where " + KEY_CATEGORY + " = '" +cat+ "'", null);
    }
    public boolean deleteRow(String cat, String val)
    {
        return odb.delete(DATABASE_TABLE, KEY_CATEGORY+ "= '" +cat+"' and "+KEY_VALUE+" = '"+val+"'", null)>0;
    }
}
