package my.edu.tarc.mobilecashservice.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import my.edu.tarc.mobilecashservice.Contract.UserContract;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;

/**
 * Created by Nan Fung Lim on 31/12/2017.
 */

public class UserSQLHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "users.db";

    List<UserRecord> records;
    DatabaseReference dbref;
    boolean isInitialise = false;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserContract.User.TABLE_NAME + "(" +
                    UserContract.User.COLUMN_USERID + " bigint," +
                    UserContract.User.COLUMN_USERNAME + " TEXT," +
                    UserContract.User.COLUMN_USERPASSWORD + " TEXT," +
                    UserContract.User.COLUMN_IC + " TEXT," +
                    UserContract.User.COLUMN_EMAIL + " TEXT," +
                    UserContract.User.COLUMN_PHONE + " bigint," +
                    UserContract.User.COLUMN_WALLET_BALANCE + " DOUBLE)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.User.TABLE_NAME;
    private String[] allColumn = {
            UserContract.User.COLUMN_USERID,
            UserContract.User.COLUMN_USERNAME,
            UserContract.User.COLUMN_USERPASSWORD,
            UserContract.User.COLUMN_IC,
            UserContract.User.COLUMN_EMAIL,
            UserContract.User.COLUMN_PHONE,
            UserContract.User.COLUMN_WALLET_BALANCE
    };

    public UserSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        records = new ArrayList<>();

        dbref = FirebaseDatabase.getInstance().getReference("user");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();


                records.clear();

                int itemAdded = 0;
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    UserRecord userRecord = postSnapshot.getValue(UserRecord.class);
                    //adding artist to the list
                    Log.i("Information", "user record user_id added: " + userRecord.getWallet_balance());
                    records.add(userRecord);
                    itemAdded++;
                    isInitialise = true;
                }
                if (itemAdded != 0) {
                    Log.i("Information", "Total " + itemAdded + " item(s) in the  list");
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
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insertUser(UserRecord userRecord) {
        //Prepare record

        //Insert a row
        dbref = FirebaseDatabase.getInstance().getReference("user");
        dbref.child(String.valueOf(userRecord.getUser_id())).setValue(userRecord.toMap(), new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.i("Info", "Save successful");
                } else {
                    Log.i("Info", "Save failed");
                }
            }
        });
    }


    public UserRecord searchPass(String usernamestr) {
        UserRecord userrecord = new UserRecord();

        if (!records.isEmpty()) {

            for (int count = 0; count < records.size(); count++) {
                if (records.get(count).getUser_name().equals(usernamestr)) {
                    userrecord = records.get(count);
                    Log.e("888", "Request of user_name : " + userrecord.getUser_name());
                    break;
                }else{
                    Log.e("888", "Un match user record!");
                }
            }
        } else {
            Log.e("Information", "records is empty !");
        }

        return userrecord;
    }

    public UserRecord getUser(int id) {

        UserRecord userrecord = new UserRecord();

        if (!records.isEmpty()) {

            for (int count = 0; count < records.size(); count++) {
                if (records.get(count).getUser_id() == id) {
                    userrecord = records.get(count);
                    Log.e("888", "Request of user !" + userrecord.getUser_id());
                    break;
                }else{
                    Log.e("888", "Un match user record!");
                }
            }
        } else {
            Log.e("Information", "records is empty !");
        }


        return userrecord;
    }

    public UserRecord getLastRecord() {

        UserRecord userrecord = new UserRecord();
        if (!records.isEmpty()) {
            userrecord = records.get((records.size()-1));
        }

        return userrecord;
    }

    public boolean updateUser(UserRecord UserRecord) {
        dbref = FirebaseDatabase.getInstance().getReference("user").child(String.valueOf(UserRecord.getUser_id()));

        dbref.setValue(UserRecord);
        Log.i("Info", "Method updateUser: Update  UserRecord successful");
        return true;
    }

    public boolean searchUsername(String usernamestr) {
        if (!records.isEmpty()) {

        }
        SQLiteDatabase database = this.getReadableDatabase();
        String query = " select user_name from " + UserContract.User.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);
        String username, result;
        result = "not found";
        if (cursor.moveToFirst()) {
            do {
                username = cursor.getString(0);
                if (username.equals(usernamestr)) {
                    return false;
                }
            } while (cursor.moveToNext());
        }
        return true;
    }

    public List<UserRecord> getAllUsers() {
        return records;
    }
}
