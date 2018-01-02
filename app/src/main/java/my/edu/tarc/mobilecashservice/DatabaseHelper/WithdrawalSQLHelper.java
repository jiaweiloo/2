package my.edu.tarc.mobilecashservice.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WithdrawalRecord.TABLE_NAME + "(" +
                    WithdrawalRecord.COLUMN_WITHDRAWAL_ID + " TEXT," +
                    WithdrawalRecord.COLUMN_DATE + " TEXT," +
                    WithdrawalRecord.COLUMN_USER_ID + " TEXT," +
                    WithdrawalRecord.COLUMN_AMOUNT + " TEXT," +
                    WithdrawalRecord.COLUMN_DEPOSIT_ID + " TEXT," +
                    WithdrawalRecord.COLUMN_LOCATION_X + " TEXT," +
                    WithdrawalRecord.COLUMN_LOCATION_Y + " TEXT," +
                    WithdrawalRecord.COLUMN_STATUS + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WithdrawalRecord.TABLE_NAME;
    private String[] allColumn = {
            WithdrawalRecord.COLUMN_WITHDRAWAL_ID,
            WithdrawalRecord.COLUMN_DATE,
            WithdrawalRecord.COLUMN_USER_ID,
            WithdrawalRecord.COLUMN_AMOUNT,
            WithdrawalRecord.COLUMN_DEPOSIT_ID,
            WithdrawalRecord.COLUMN_LOCATION_X,
            WithdrawalRecord.COLUMN_LOCATION_Y,
            WithdrawalRecord.COLUMN_STATUS,
    };

    public WithdrawalSQLHelper(Context context) {
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
    public void insertWithdrawal(Withdrawal userRecord) {
        //Prepare record
        ContentValues values = new ContentValues();

        values.put(WithdrawalRecord.COLUMN_WITHDRAWAL_ID,userRecord.getWithdrawal_id());
        values.put(WithdrawalRecord.COLUMN_DATE,userRecord.getDateTime());
        values.put(WithdrawalRecord.COLUMN_USER_ID, userRecord.getUser_id());
        values.put(WithdrawalRecord.COLUMN_AMOUNT, userRecord.getAmount());
        values.put(WithdrawalRecord.COLUMN_DEPOSIT_ID, userRecord.getDeposit_id());
        values.put(WithdrawalRecord.COLUMN_LOCATION_X, userRecord.getLocation_x());
        values.put(WithdrawalRecord.COLUMN_LOCATION_Y, userRecord.getLocation_y());
        values.put(WithdrawalRecord.COLUMN_STATUS, userRecord.getStatus());

        //Insert a row
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(WithdrawalRecord.TABLE_NAME, null, values);

        //Close database connection
        database.close();
    }

    //Get all records
    public List<Withdrawal> getAllUsers() {
        List<Withdrawal> records = new ArrayList<Withdrawal>();

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(WithdrawalRecord.TABLE_NAME, allColumn, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Withdrawal userRecord = new Withdrawal();
            userRecord.setWithdrawal_id(cursor.getInt(0));
            userRecord.setDateTime(cursor.getString(1));
            userRecord.setUser_id(cursor.getInt(2));
            userRecord.setAmount(cursor.getDouble(3));
            userRecord.setDeposit_id(cursor.getInt(4));
            userRecord.setLocation_x(cursor.getDouble(5));
            userRecord.setLocation_y(cursor.getDouble(6));
            userRecord.setStatus(cursor.getString(7));
            records.add(userRecord);
            cursor.moveToNext();
        }

        return records;
    }
}
