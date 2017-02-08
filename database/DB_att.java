package com.nssce_cse.harikrishnanp.attendancebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;


/**
 * Created by VishnuMidhun on 26-07-2016.
 */
public class DB_att {
    public static final String KEY_ATT_ID = "attid";
    public static final String KEY_DEPT = "department";
    public static final String KEY_CLASS = "class";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_DATE = "date";
    public static final String KEY_HOUR = "hour";

    private static final String DATABASE_NAME = "DB_ATTENDANCE";
    private static final String DATABASE_TABLE = "TABLE_ATTENDANCE";
    private static final int DATABASE_VERSION = 1;

    private final Context ourContext;
    private DbHelper dbh;
    private SQLiteDatabase odb;

    private static final String USER_MASTER_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "("
                    + KEY_ATT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DEPT+ " VARCHAR(100)," + KEY_CLASS +" VARCHAR(50),"+ KEY_SUBJECT+ " VARCHAR(100),"+KEY_DATE+" VARCHAR(20),"+KEY_HOUR+" VARCHAR(20)  ) ";

    public DB_att(Context c) {
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
           // db.execSQL(PRIMARY_ENTRY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // if DATABASE VERSION changes
            // Drop old tables and call super.onCreate()
        }
    }
    public DB_att open() throws SQLException {
        odb = dbh.getWritableDatabase();
        return this;
    }
    public void close() {
        dbh.close();
    }
    public long insertmaster(String col1,String col2,String col3,String col4,String col5){
        Log.d("", col1);
        Log.d("", col2);

        ContentValues IV = new ContentValues();
        IV.put(KEY_DEPT, col1);
        IV.put(KEY_CLASS, col2);
        IV.put(KEY_SUBJECT, col3);
        IV.put(KEY_DATE, col4);
        IV.put(KEY_HOUR, col5);

        return odb.insert(DATABASE_TABLE, null, IV);
    }
    public Cursor getallCols(String id) throws SQLException {
        Cursor mCursor = odb.query(DATABASE_TABLE, new String[] {  KEY_ATT_ID, KEY_DEPT, KEY_CLASS,KEY_SUBJECT,KEY_DATE,KEY_HOUR }, null, null, null, null, null);
        Log.e("getallcols zmv", "opening successfull");
        return mCursor;
    }
   /* public void export()
    {
        File dbFile=getDatabasePath("MyDBName.db");
        DBHelper dbhelper = new DBHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM contacts",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }*/
}
