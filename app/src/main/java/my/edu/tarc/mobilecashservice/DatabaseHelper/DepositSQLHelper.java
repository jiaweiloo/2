package my.edu.tarc.mobilecashservice.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        ContentValues values = new ContentValues();

        values.put(DepositContract.Deposits.COLUMN_DEPOSIT_ID, DepositRecord.getDeposit_id());
        values.put(DepositContract.Deposits.COLUMN_USER_ID, DepositRecord.getUser_id());
        values.put(DepositContract.Deposits.COLUMN_AMOUNT, DepositRecord.getAmount());
        values.put(DepositContract.Deposits.COLUMN_WITHDRAWAL_ID, DepositRecord.getWithdrawal_id());
        values.put(DepositContract.Deposits.COLUMN_LOCATION_ID, DepositRecord.getLocation_id());
        values.put(DepositContract.Deposits.COLUMN_STATUS, DepositRecord.getStatus());
        //Insert a row
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(DepositContract.Deposits.TABLE_NAME, null, values);
        //Close database connection
        database.close();
    }

    //Get all records
    public List<Deposit> getAllDeposits() {
        List<Deposit> records = new ArrayList<Deposit>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DepositContract.Deposits.TABLE_NAME, allColumn, null, null, null,
                null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Deposit DepositRecord = new Deposit();
            DepositRecord.setDeposit_id(Integer.parseInt(cursor.getString(0)));
            DepositRecord.setUser_id(Integer.parseInt(cursor.getString(1)));
            DepositRecord.setAmount(Double.parseDouble(cursor.getString(2)));
            DepositRecord.setWithdrawal_id(Integer.parseInt(cursor.getString(3)));
            DepositRecord.setLocation_id(Integer.parseInt(cursor.getString(4)));
            DepositRecord.setStatus(cursor.getString(5));
            records.add(DepositRecord);
            cursor.moveToNext();
        }
        return records;
    }

    public Deposit getLastRecord() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DepositContract.Deposits.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToLast();
        Deposit DepositRecord = new Deposit();
        if(cursor.getCount()>0) {
            DepositRecord.setDeposit_id(Integer.parseInt(cursor.getString(0)));
            DepositRecord.setUser_id(Integer.parseInt(cursor.getString(1)));
            DepositRecord.setAmount(Double.parseDouble(cursor.getString(2)));
            DepositRecord.setWithdrawal_id(Integer.parseInt(cursor.getString(3)));
            DepositRecord.setLocation_id(Integer.parseInt(cursor.getString(4)));
            DepositRecord.setStatus(cursor.getString(5));
        }
        cursor.close();
        database.close();

        return DepositRecord;
    }

    public int getTotalRecords() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DepositContract.Deposits.TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);
        return cursor.getCount();
    }

    public Deposit getDeposit(int id) {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(DepositContract.Deposits.TABLE_NAME,
                new String[]{
                        DepositContract.Deposits.COLUMN_DEPOSIT_ID,
                        DepositContract.Deposits.COLUMN_USER_ID,
                        DepositContract.Deposits.COLUMN_AMOUNT,
                        DepositContract.Deposits.COLUMN_WITHDRAWAL_ID,
                        DepositContract.Deposits.COLUMN_LOCATION_ID,
                        DepositContract.Deposits.COLUMN_STATUS},
                DepositContract.Deposits.COLUMN_DEPOSIT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        Deposit DepositRecord = new Deposit();

        if (cursor.getCount() > 0) {
            DepositRecord.setDeposit_id(Integer.parseInt(cursor.getString(0)));
            DepositRecord.setUser_id(Integer.parseInt(cursor.getString(1)));
            DepositRecord.setAmount(Double.parseDouble(cursor.getString(2)));
            DepositRecord.setWithdrawal_id(Integer.parseInt(cursor.getString(3)));
            DepositRecord.setLocation_id(Integer.parseInt(cursor.getString(4)));
            DepositRecord.setStatus(cursor.getString(5));
        }
        cursor.close();
        database.close();

        return DepositRecord;

    }

    public int updateDeposit(Deposit DepositRecord) {
        SQLiteDatabase database = this.getWritableDatabase();

        //Prepare record
        ContentValues values = new ContentValues();

        values.put(DepositContract.Deposits.COLUMN_DEPOSIT_ID, DepositRecord.getDeposit_id());
        values.put(DepositContract.Deposits.COLUMN_USER_ID, DepositRecord.getUser_id());
        values.put(DepositContract.Deposits.COLUMN_AMOUNT, DepositRecord.getAmount());
        values.put(DepositContract.Deposits.COLUMN_WITHDRAWAL_ID, DepositRecord.getWithdrawal_id());
        values.put(DepositContract.Deposits.COLUMN_LOCATION_ID, DepositRecord.getLocation_id());
        values.put(DepositContract.Deposits.COLUMN_STATUS, DepositRecord.getStatus());

        // updating row
        return database.update(DepositContract.Deposits.TABLE_NAME, values, DepositContract.Deposits.COLUMN_DEPOSIT_ID + " = ?",
                new String[] { String.valueOf(DepositRecord.getDeposit_id()) });
    }

    // Deleting single contact
    public void deleteDeposit(Deposit DepositRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DepositContract.Deposits.TABLE_NAME, DepositContract.Deposits.COLUMN_DEPOSIT_ID + " = ?",
                new String[] { String.valueOf(DepositRecord.getDeposit_id()) });

        db.close();
    }

    //delete all deposits
    public int deleteAllDeposit(){
        SQLiteDatabase db = this.getWritableDatabase();
        int totaldeleted= db.delete(DepositContract.Deposits.TABLE_NAME, "1", null);
        db.close();
        return totaldeleted;
    }
}