package my.edu.tarc.mobilecashservice.DatabaseHelper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import my.edu.tarc.mobilecashservice.Contract.WithdrawalContract;
import my.edu.tarc.mobilecashservice.Contract.WithdrawalContract.WithdrawalRecord;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loi Kah Hou on 12/30/2017.
 */

public class WithdrawalSQLHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "withdraw.db";

    List<Withdrawal> records = new ArrayList<>();
    DatabaseReference dbref;
    boolean isInitialise = false;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WithdrawalRecord.TABLE_NAME + "(" +
                    WithdrawalRecord.COLUMN_WITHDRAWAL_ID + " TEXT," +
                    WithdrawalRecord.COLUMN_DATE + " TEXT," +
                    WithdrawalRecord.COLUMN_USER_ID + " TEXT," +
                    WithdrawalRecord.COLUMN_AMOUNT + " TEXT," +
                    WithdrawalRecord.COLUMN_DEPOSIT_ID + " TEXT," +
                    WithdrawalRecord.COLUMN_LOCATION_ID + " TEXT," +
                    WithdrawalRecord.COLUMN_STATUS + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WithdrawalRecord.TABLE_NAME;
    private String[] allColumn = {
            WithdrawalRecord.COLUMN_WITHDRAWAL_ID,
            WithdrawalRecord.COLUMN_DATE,
            WithdrawalRecord.COLUMN_USER_ID,
            WithdrawalRecord.COLUMN_AMOUNT,
            WithdrawalRecord.COLUMN_DEPOSIT_ID,
            WithdrawalRecord.COLUMN_LOCATION_ID,
            WithdrawalRecord.COLUMN_STATUS,
    };

    public WithdrawalSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbref = FirebaseDatabase.getInstance().getReference("withdrawal");
        Log.i("Information", "onCreate ");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();

                records.clear();
                int itemAdded = 0;
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting withdrawalRecord
                    Withdrawal withdrawalRecord = postSnapshot.getValue(Withdrawal.class);
                    //adding withdrawalRecord to the list
                    Log.e("Information", "withdrawal record user_id added: " + withdrawalRecord.getWithdrawal_id());
                    records.add(withdrawalRecord);
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
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Add a new record
    public void insertWithdrawal(Withdrawal withdrawal) {
        dbref.child(String.valueOf(withdrawal.getWithdrawal_id())).setValue(withdrawal.toMap(), new DatabaseReference.CompletionListener() {

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

    //Get all records
    public List<Withdrawal> getAllWithdrawals() {
        return records;
    }

    public Withdrawal getLastRecord() {
        Withdrawal withdrawal = new Withdrawal();

        if (!records.isEmpty()) {
            withdrawal = records.get((records.size() - 1));
        }

        return withdrawal;
    }

    public Withdrawal getWithdrawal(int id) {

        Withdrawal withdrawal = new Withdrawal();

        if (!records.isEmpty()) {

            for (int count = 0; count < records.size(); count++) {
                if (records.get(count).getWithdrawal_id() == id) {
                    withdrawal = records.get(count);
                    Log.e("Check", "Request of withdrawal !" + withdrawal.getWithdrawal_id());
                    break;
                } else {
                    Log.e("NO", "Un match user record!");
                }
            }
        } else {
            Log.e("Information", "records is empty !");
        }

        return withdrawal;
    }

    public boolean updateWithdrawal(Withdrawal withdrawalRecord) {

        dbref = FirebaseDatabase.getInstance().getReference("withdrawal").child(String.valueOf(withdrawalRecord.getWithdrawal_id()));
        dbref.setValue(withdrawalRecord);
        Log.i("Info", "Method updateWithdrawal: Update  withdrawalRecord successful");
        return true;
    }
}
