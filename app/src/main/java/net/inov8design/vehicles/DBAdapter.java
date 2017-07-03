package net.inov8design.vehicles;

/**
 * Created by allan on 31/03/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_DRIVER = "driver";
    static final String KEY_REGO = "rego";
    static final String KEY_STARTTIME = "starttime";
    static final String KEY_FB= "firstbreak";
    static final String KEY_SB = "secondbreak";
    static final String KEY_ENDTIME = "endtime";
    static final String KEY_VEHICLETYPE = "vehicletype";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "vehiclelog";
    static final String DATABASE_TABLE = "log";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
                    +  KEY_DRIVER + "  string not null,"+KEY_REGO+" string not null,"+KEY_STARTTIME+ " string not null,"+KEY_FB+ " string not null,"+KEY_SB+ " string not null,"+KEY_ENDTIME+ " string not null,"+KEY_VEHICLETYPE+ " integer not null);";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS log");
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
 public int getItemcount(){
     String countQuery = "SELECT * FROM " + DATABASE_TABLE;
     Cursor cursor = db.rawQuery(countQuery,null);
     int cnt = cursor.getCount();
     cursor.close();
     return cnt;

 }
    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertlog(String driver, String rego, String starttime, String firstbreak, String secondbreak,String endtime, Integer vehicletype)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DRIVER, driver);
        initialValues.put(KEY_REGO, rego);
        initialValues.put(KEY_STARTTIME,starttime);
        initialValues.put(KEY_FB, firstbreak);
        initialValues.put(KEY_SB, secondbreak);
        initialValues.put(KEY_ENDTIME, endtime);
        initialValues.put(KEY_VEHICLETYPE, vehicletype);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    //----Delete all items in the table
    public int deletealllogs(){
        int rows = db.delete(DATABASE_TABLE,"1", null);

        return rows;
    }
    //---deletes a particular contact---
    public boolean deletelog(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAlllogs()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_DRIVER,
                KEY_REGO,KEY_STARTTIME,KEY_FB,KEY_SB,KEY_ENDTIME,KEY_VEHICLETYPE}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getlog(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_DRIVER, KEY_REGO,KEY_STARTTIME,KEY_FB,KEY_SB,KEY_ENDTIME,KEY_VEHICLETYPE}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updatelog(long rowId, String driver, String rego, String starttime, String firstbreak,String secondbreak, String endtime, String vehicletype)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_DRIVER, driver);
        args.put(KEY_REGO, rego);
        args.put(KEY_STARTTIME, starttime);
        args.put(KEY_FB, firstbreak);
        args.put(KEY_SB, secondbreak);
        args.put(KEY_ENDTIME, endtime);
        args.put(KEY_VEHICLETYPE, vehicletype);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
