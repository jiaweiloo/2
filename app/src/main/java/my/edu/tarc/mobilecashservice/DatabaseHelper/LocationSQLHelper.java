package my.edu.tarc.mobilecashservice.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    List<Location> records = new ArrayList<>();
    DatabaseReference dbref;

    boolean isInitialise = false;
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

        dbref = FirebaseDatabase.getInstance().getReference("location");
        Log.i("Information", "onCreate ");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                records.clear();
                int itemAdded = 0;
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting depositRecord
                    Location locationRecord = postSnapshot.getValue(Location.class);
                    //adding depositRecord to the list
                    Log.e("Information", "Location record location_id added: " + locationRecord.getLocation_id());
                    records.add(locationRecord);
                    itemAdded++;
                    isInitialise = true;
                }
                if (itemAdded != 0) {
                    Log.i("Location", "Total " + itemAdded + " item(s) in the  list");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        dbref.child(String.valueOf(LocationRecord.getLocation_id())).setValue(LocationRecord.toMap(), new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.e("Info", "Save successful");
                } else {
                    Log.i("Info", "Save failed");
                }
            }
        });
    }

    //Get all records
    public List<Location> getAllLocations() {
        return records;
    }

    public Location getLastRecord() {

        Location LocationRecord = new Location();

        if (!records.isEmpty()) {
            LocationRecord = records.get((records.size() - 1));
        }

        return LocationRecord;
    }

    public int getTotalRecords() {
        return records.size();
    }

    public Location getLocation(int id) {


        Location LocationRecord = new Location();

        if (!records.isEmpty()) {

            for (int count = 0; count < records.size(); count++) {
                if (records.get(count).getLocation_id() == id) {
                    LocationRecord = records.get(count);
                    Log.i("Check", "Request of location !" + LocationRecord.getLocation_id());
                    break;
                } else {
                    Log.e("NO", "Un match record!");
                }
            }//end of for loop
        } else {
            Log.e("Information", "records is empty !");
        }

        return LocationRecord;

    }

    public boolean updateLocation(Location LocationRecord) {
        dbref = FirebaseDatabase.getInstance().getReference("location").child(String.valueOf(LocationRecord.getLocation_id()));
        dbref.setValue(LocationRecord);
        Log.i("Info", "Method updateLocation: Update  LocationRecord successful");
        return true;
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
        Log.i("No function","delete function not available");
        return 0;
    }
}
