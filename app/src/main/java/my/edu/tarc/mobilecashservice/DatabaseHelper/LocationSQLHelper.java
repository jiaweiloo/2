package my.edu.tarc.mobilecashservice.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import my.edu.tarc.mobilecashservice.Contract.LocationContract;
import my.edu.tarc.mobilecashservice.Entity.Location;

/**
 * Created by jiaweiloo on 4/1/2018.
 */

public class LocationSQLHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "location.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationContract.Location.TABLE_NAME + "(" +
                    LocationContract.Location.COLUMN_LOCATION_ID + " TEXT," +
                    LocationContract.Location.COLUMN_LOCATION_NAME + " TEXT," +
                    LocationContract.Location.COLUMN_LOCATION_X + " TEXT," +
                    LocationContract.Location.COLUMN_LOCATION_Y + " TEXT," +
                    LocationContract.Location.COLUMN_STATUS + " TEXT)";
    //int deposit_id, int user_id, double amount, int withdrawal_id, int location_id, String status
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationContract.Location.TABLE_NAME;
    private String[] allColumn = {
            LocationContract.Location.COLUMN_LOCATION_ID,
            LocationContract.Location.COLUMN_LOCATION_NAME,
            LocationContract.Location.COLUMN_LOCATION_X,
            LocationContract.Location.COLUMN_LOCATION_Y,
            LocationContract.Location.COLUMN_STATUS
    };

    public LocationSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Add a new record
    public void insertLocation(Location LocationRecord) {
        //Prepare record
        ContentValues values = new ContentValues();

        values.put(LocationContract.Location.COLUMN_LOCATION_ID, LocationRecord.getLocation_id());
        values.put(LocationContract.Location.COLUMN_LOCATION_NAME, LocationRecord.getLocation_name());
        values.put(LocationContract.Location.COLUMN_LOCATION_X, LocationRecord.getLocation_x());
        values.put(LocationContract.Location.COLUMN_LOCATION_Y, LocationRecord.getLocation_y());
        values.put(LocationContract.Location.COLUMN_STATUS, LocationRecord.getStatus());
        //Insert a row
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(LocationContract.Location.TABLE_NAME, null, values);
        //Close database connection
        database.close();
    }

    //Get all records
    public List<Location> getAllLocations() {
        List<Location> records = new ArrayList<Location>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(LocationContract.Location.TABLE_NAME, allColumn, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Location LocationRecord = new Location();
            LocationRecord.setLocation_id(Integer.parseInt(cursor.getString(0)));
            LocationRecord.setLocation_name(cursor.getString(1));
            LocationRecord.setLocation_x(Double.parseDouble(cursor.getString(2)));
            LocationRecord.setLocation_y(Double.parseDouble(cursor.getString(3)));
            LocationRecord.setStatus(cursor.getString(4));
            records.add(LocationRecord);
            cursor.moveToNext();
        }
        return records;
    }

    public Location getLastRecord() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + LocationContract.Location.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToLast();
        Location LocationRecord = new Location();
        if (cursor.getCount() > 0) {
            LocationRecord.setLocation_id(Integer.parseInt(cursor.getString(0)));
            LocationRecord.setLocation_name(cursor.getString(1));
            LocationRecord.setLocation_x(Double.parseDouble(cursor.getString(2)));
            LocationRecord.setLocation_y(Double.parseDouble(cursor.getString(3)));
            LocationRecord.setStatus(cursor.getString(4));
        }
        cursor.close();
        database.close();

        return LocationRecord;
    }

    public int getTotalRecords() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + LocationContract.Location.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor.getCount();
    }

    public Location getLocation(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(LocationContract.Location.TABLE_NAME,
                new String[]{
                        LocationContract.Location.COLUMN_LOCATION_ID,
                        LocationContract.Location.COLUMN_LOCATION_NAME,
                        LocationContract.Location.COLUMN_LOCATION_X,
                        LocationContract.Location.COLUMN_LOCATION_Y,
                        LocationContract.Location.COLUMN_STATUS},
                        LocationContract.Location.COLUMN_LOCATION_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        Location LocationRecord = new Location();

        if (cursor.getCount() > 0) {
            LocationRecord.setLocation_id(Integer.parseInt(cursor.getString(0)));
            LocationRecord.setLocation_name(cursor.getString(1));
            LocationRecord.setLocation_x(Double.parseDouble(cursor.getString(2)));
            LocationRecord.setLocation_y(Double.parseDouble(cursor.getString(3)));
            LocationRecord.setStatus(cursor.getString(4));
        }
        cursor.close();
        database.close();

        return LocationRecord;

    }

    public int updateLocation(Location LocationRecord) {
        SQLiteDatabase database = this.getWritableDatabase();

        //Prepare record
        ContentValues values = new ContentValues();

        values.put(LocationContract.Location.COLUMN_LOCATION_ID, LocationRecord.getLocation_id());
        values.put(LocationContract.Location.COLUMN_LOCATION_NAME, LocationRecord.getLocation_name());
        values.put(LocationContract.Location.COLUMN_LOCATION_X, LocationRecord.getLocation_x());
        values.put(LocationContract.Location.COLUMN_LOCATION_Y, LocationRecord.getLocation_y());
        values.put(LocationContract.Location.COLUMN_STATUS, LocationRecord.getStatus());

        // updating row
        return database.update(LocationContract.Location.TABLE_NAME, values, LocationContract.Location.COLUMN_LOCATION_ID + " = ?",
                new String[]{String.valueOf(LocationRecord.getLocation_id())});
    }

    // Deleting single contact
    public void deleteLocation(Location LocationRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LocationContract.Location.TABLE_NAME, LocationContract.Location.COLUMN_LOCATION_ID + " = ?",
                new String[]{String.valueOf(LocationRecord.getLocation_id())});

        db.close();
    }

    //delete all deposits
    public int deleteAllLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        int totaldeleted = db.delete(LocationContract.Location.TABLE_NAME, "1", null);
        db.close();
        return totaldeleted;
    }
}
