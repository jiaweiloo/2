package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import my.edu.tarc.mobilecashservice.DatabaseHelper.DepositSQLHelper;
import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

public class DepositWaitingPage extends AppCompatActivity {
    Double amount;
    int location_id;
    int user_id;
    int withdrawal_id;
    TextView tViewTimer, tViewStatus, tviewUser, tViewDetails;
    ProgressBar progressBar2;
    DepositSQLHelper depositDataSource;
    WithdrawalSQLHelper withdrawalSQLHelper;
    List<Withdrawal> values = new ArrayList<>();
    Button btnCancel;
    Deposit deposit = new Deposit();
    boolean isFound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_waiting_page);
        setTitle("Waiting for a pair..");


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        location_id = sharedPref.getInt("location_id", 0);
        amount = Double.parseDouble(sharedPref.getString("amount", "0.0"));

        depositDataSource = new DepositSQLHelper(this);
        withdrawalSQLHelper = new WithdrawalSQLHelper(this);

        tViewTimer = findViewById(R.id.tviewTimer);
        tViewStatus = findViewById(R.id.tViewStatus);
        progressBar2 = findViewById(R.id.progressBar2);
        tviewUser = findViewById(R.id.tviewUser);
        tViewDetails = findViewById(R.id.tViewDetails);
        btnCancel = findViewById(R.id.btnCancel);


        //900000 ms for 15 minutes
        new CountDownTimer(10000, 1000) { // adjust the milli seconds here
            int count = 0;
            boolean isFound = false;

            public void onTick(long millisUntilFinished) {
                tViewTimer.setText("" + String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                if (count > 2 && count < 5) {
                    Log.e("Error", "Now count is more than 2 but still less than 4");
                    values = withdrawalSQLHelper.getAllWithdrawals();
                }
                if (count > 3) {
                    isFound = findWithdrawal();
                }
                if (isFound)
                    this.onFinish();

                count++;
            }

            public void onFinish() {
                //deposit = depositDataSource.getDeposit(deposit.getDeposit_id());
                progressBar2.setVisibility(View.GONE);
                if (deposit.getWithdrawal_id() == 0) {
                    tViewTimer.setText("Pairing fail!");
                    tViewStatus.setText("Please try again in a moment !");
                    btnCancel.setText("Retry");

                } else {
                    tViewTimer.setText("Pairing success!");
                    tViewStatus.setText("Successful matched with " + deposit.getWithdrawal_id());
                    btnCancel.setText("Finish");
                    //btnCancel.set
                }
                this.cancel();
            }
        }.start();
        //depositDataSource.getDeposit(deposit.deposit_id);

        //depositDataSource.getTotalRecords();
        tviewUser.setText(Integer.toString(depositDataSource.getTotalRecords()));

        tViewDetails.setText("Deposit ID: " + deposit.getDeposit_id()
                + "\n" + "Amount: " + amount + "\n" + "Location id: " + location_id);
    }

    public void btnCancelAction(View view) {
        if (btnCancel.getText().equals("Finish")) {
            Intent intent = new Intent(this, DepositScanQRcode.class);
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("deposit_id", deposit.getDeposit_id());
            editor.putInt("withdrawal_id", deposit.getWithdrawal_id());
            editor.commit();

            startActivityForResult(intent, 2);
        } else {
            Intent intent = new Intent(this, DepositSelectCash.class);
            startActivity(intent);
        }
    }

    public void addRecord(int withdrawal_id, String status) {
        Deposit tempdep = new Deposit();

        //if else statement show that if database is empty, initialise first row deposit id to 200001
        if (depositDataSource.getLastRecord().getDeposit_id() != 0) {
            tempdep = depositDataSource.getLastRecord();
        } else {
            tempdep.setDeposit_id(200000);
        }

        //when pair successfully, add data into database
        deposit.setDeposit_id(tempdep.getDeposit_id() + 1);
        deposit.setUser_id(user_id);
        deposit.setAmount(amount);
        deposit.setWithdrawal_id(withdrawal_id);
        deposit.setLocation_id(location_id);
        deposit.setStatus(status);
        depositDataSource.insertDeposit(deposit);
        tViewDetails.setText("Deposit ID: " + deposit.getDeposit_id()
                + "\n" + "Amount: " + deposit.getAmount()
                + "\n" + "Location id: " + location_id
                + "\n" + "Withdrawal id: " + withdrawal_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    //get data from the pairing activity
                    withdrawal_id = data.getExtras().getInt("withdrawal_id");
                    Log.i("tag", "Withdrawal ID onActivityResult: " + withdrawal_id);

                    tViewStatus.setText("Pair Success with " + withdrawal_id);
                    tViewDetails.setText("Deposit ID: " + deposit.getDeposit_id()
                            + "\n" + "Amount: " + deposit.getAmount()
                            + "\n" + "Location id: " + location_id
                            + "\n" + "Withdrawal id: " + withdrawal_id);

                    addRecord(withdrawal_id, "paired");
                    //depositDataSource.updateDeposit(deposit);

                } else tViewStatus.setText("Withdrawal pairing Failure !");
            } else Log.i("tag", "R.string.barcode_error_format" +
                    CommonStatusCodes.getStatusCodeString(resultCode));
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean findWithdrawal() {
        values = withdrawalSQLHelper.getAllWithdrawals();
        Log.i("Values ", "at findWithdrawal : Withdrawal ID onActivityResult: " + values.get(0).getLocation_id());
        for (int a = 0; a < values.size(); a++) {
            Withdrawal temp = values.get(a);
            if (temp.getLocation_id() == location_id && temp.getStatus().equals("pending")) {
                withdrawal_id = temp.getWithdrawal_id();
                Log.i("tag", "Withdrawal ID onActivityResult: " + withdrawal_id);

                tViewStatus.setText("Pair Success with " + withdrawal_id);

                addRecord(withdrawal_id, "paired");
                isFound = true;
                temp.setDeposit_id(deposit.getDeposit_id());
                withdrawalSQLHelper.updateWithdrawal(temp);
                break;
            }
        }
        return isFound;
    }
}
