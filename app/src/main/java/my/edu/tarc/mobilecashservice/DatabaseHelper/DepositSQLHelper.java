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

/**
 * Created by jiaweiloo on 29/12/2017.
 */

import my.edu.tarc.mobilecashservice.Contract.DepositContract;
import my.edu.tarc.mobilecashservice.Entity.Deposit;


public class DepositSQLHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "deposits.db";

    List<Deposit> records = new ArrayList<>();
    DatabaseReference dbref;
    boolean isInitialise = false;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DepositContract.Deposits.TABLE_NAME + "(" +
                    DepositContract.Deposits.COLUMN_DEPOSIT_ID + " TEXT," +
                    DepositContract.Deposits.COLUMN_USER_ID + " TEXT," +
                    DepositContract.Deposits.COLUMN_AMOUNT + " TEXT," +
                    DepositContract.Deposits.COLUMN_WITHDRAWAL_ID + " TEXT," +
                    DepositContract.Deposits.COLUMN_LOCATION_ID + " TEXT," +
                    DepositContract.Deposits.COLUMN_STATUS + " TEXT)";
    //int deposit_id, int user_id, double amount, int withdrawal_id, int location_id, String status
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DepositContract.Deposits.TABLE_NAME;
    private String[] allColumn = {
            DepositContract.Deposits.COLUMN_DEPOSIT_ID,
            DepositContract.Deposits.COLUMN_USER_ID,
            DepositContract.Deposits.COLUMN_AMOUNT,
            DepositContract.Deposits.COLUMN_WITHDRAWAL_ID,
            DepositContract.Deposits.COLUMN_LOCATION_ID,
            DepositContract.Deposits.COLUMN_STATUS
    };

    public DepositSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbref = FirebaseDatabase.getInstance().getReference("deposit");
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
                    Deposit depositRecord = postSnapshot.getValue(Deposit.class);
                    //adding depositRecord to the list
                    Log.e("Information", "Deposit record deposit_id added: " + depositRecord.getWithdrawal_id());
                    records.add(depositRecord);
                    itemAdded++;
                    isInitialise = true;
                }
                if (itemAdded != 0) {
                    Log.i("Deposit", "Total " + itemAdded + " item(s) in the  list");
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
    public void insertDeposit(Deposit DepositRecord) {
        //Prepare record
        dbref.child(String.valueOf(DepositRecord.getDeposit_id())).setValue(DepositRecord.toMap(), new DatabaseReference.CompletionListener() {

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
    public List<Deposit> getAllDeposits() {
        return records;
    }

    public Deposit getLastRecord() {
        Deposit DepositRecord = new Deposit();
        if (!records.isEmpty()) {
            DepositRecord = records.get((records.size() - 1));
        }
        return DepositRecord;
    }

    public int getTotalRecords() {
        return records.size();
    }

    public Deposit getDeposit(int id) {
        Deposit DepositRecord = new Deposit();

        if (!records.isEmpty()) {

            for (int count = 0; count < records.size(); count++) {
                if (records.get(count).getDeposit_id() == id) {
                    DepositRecord = records.get(count);
                    Log.i("Check", "Request of deposit !" + DepositRecord.getDeposit_id());
                    break;
                } else {
                    Log.e("NO", "Un match user record!");
                }
            }//end of for loop
        } else {
            Log.e("Information", "records is empty !");
        }

        return DepositRecord;

    }

    public boolean updateDeposit(Deposit DepositRecord) {

        dbref = FirebaseDatabase.getInstance().getReference("deposit").child(String.valueOf(DepositRecord.getDeposit_id()));
        dbref.setValue(DepositRecord);
        Log.i("Info", "Method updateDeposit: Update  UserRecord successful");
        return true;
    }

    // Deleting single contact
    public void deleteDeposit(Deposit DepositRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DepositContract.Deposits.TABLE_NAME, DepositContract.Deposits.COLUMN_DEPOSIT_ID + " = ?",
                new String[]{String.valueOf(DepositRecord.getDeposit_id())});

        db.close();
    }

    //delete all deposits
    public int deleteAllDeposit() {
        SQLiteDatabase db = this.getWritableDatabase();
        int totaldeleted = db.delete(DepositContract.Deposits.TABLE_NAME, "1", null);
        db.close();
        return totaldeleted;
    }
}