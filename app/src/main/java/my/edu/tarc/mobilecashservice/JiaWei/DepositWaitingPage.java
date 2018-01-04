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

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.concurrent.TimeUnit;

import my.edu.tarc.mobilecashservice.DatabaseHelper.DepositSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.R;

public class DepositWaitingPage extends AppCompatActivity {
    Double amount;
    int location_id;
    int user_id;
    TextView tViewTimer;
    TextView tViewStatus;
    TextView tviewUser;
    TextView tViewDetails;
    ProgressBar progressBar2;
    DepositSQLHelper depositDataSource;
    Button btnCancel;
    Button btnMatch;
    Deposit deposit = new Deposit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_waiting_page);

        setTitle("Waiting for a pair..");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        location_id = sharedPref.getInt("location_id", 0);
        amount = Double.parseDouble(sharedPref.getString("amount", "0.0"));


        /*
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amount = bundle.getString("amount");
            areaaa = bundle.getString("areaCode");
            user_id = Integer.parseInt(bundle.getString("user_id"));
        } */
        Log.i("[System]", "User :" + user_id);
        Log.i("[System]", "Amount :" + amount);
        Log.i("[System]", "location_id :" + location_id);


        if (amount != 0.0 && location_id != 0 && user_id != 0) {
            Log.i("[System]", "string not empty " + amount);
        } else {
            Log.i("[System]", "Error strings not match any if else");
            amount = 0.0;
            location_id = 400001;
            user_id = 888888;
        }

        depositDataSource = new DepositSQLHelper(this);
        tViewTimer = findViewById(R.id.tviewTimer);
        tViewStatus = findViewById(R.id.tViewStatus);
        progressBar2 = findViewById(R.id.progressBar2);
        tviewUser = findViewById(R.id.tviewUser);
        tViewDetails = findViewById(R.id.tViewDetails);
        btnCancel = findViewById(R.id.btnCancel);
        btnMatch = findViewById(R.id.btnMatch);


        //900000 ms for 15 minutes
        new CountDownTimer(9000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tViewTimer.setText("" + String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                deposit = depositDataSource.getDeposit(deposit.getDeposit_id());
                progressBar2.setVisibility(View.GONE);
                btnMatch.setEnabled(false);
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
            }
        }.start();
        //depositDataSource.getDeposit(deposit.deposit_id);

        //depositDataSource.getTotalRecords();
        tviewUser.setText(Integer.toString(depositDataSource.getTotalRecords()));
        addRecord();
        tViewDetails.setText("Deposit ID: " + deposit.getDeposit_id()
                + "\n" + "Amount: " + deposit.getAmount() + "\n" + "Location id: " + location_id);
    }

    public void goToCheckDatabase() {
        Intent intent = new Intent(this, AllDepositRecords.class);
        //intent.putExtra("message", txtAmount.getText().toString());
        startActivityForResult(intent, 2);
    }

    public void btnCancelAction(View view) {
        if (btnCancel.getText().equals("Finish")) {
            Intent intent = new Intent(this, DepositScanQRcode.class);
            intent.putExtra("deposit_id", deposit.getDeposit_id());
            startActivityForResult(intent, 2);
        } else {
            Intent intent = new Intent(this, DepositSelectCash.class);
            startActivityForResult(intent, 2);
        }
    }

    public void addRecord() {
        Deposit tempdep = new Deposit();

        if (depositDataSource.getLastRecord().getDeposit_id() != 0) {
            //Log.i("[System]", "Last records is NOT empty or null! ");
            tempdep = depositDataSource.getLastRecord();
        } else {
            tempdep.setDeposit_id(200000);
        }

        deposit.setDeposit_id(tempdep.getDeposit_id() + 1);
        deposit.setUser_id(user_id);
        deposit.setAmount(amount);
        deposit.setWithdrawal_id(0);
        deposit.setLocation_id(location_id);
        deposit.setStatus("pending");
        //Log.i("[System]", "Add records " + deposit.getDeposit_id());
        //Deposit dep = new Deposit(tempdep.getDeposit_id() + 1, 100001, 50, 300001, 400001, "pending");
        depositDataSource.insertDeposit(deposit);
    }

    public void pair(View view) {
        /*
        deposit.setWithdrawal_id(300001);
        deposit.setStatus("paired");
        depositDataSource.updateDeposit(deposit); */

        Intent intent = new Intent(this, DepositPairWithdrawal.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    int withdrawal_id = data.getExtras().getInt("withdrawal_id");
                    Log.i("tag", "Withdrawal ID: " + withdrawal_id);
                    tViewStatus.setText("Pair Success with " + withdrawal_id);
                    deposit.setWithdrawal_id(withdrawal_id);
                    deposit.setStatus("paired");
                    depositDataSource.updateDeposit(deposit);

                } else tViewStatus.setText("Pair Failure !");
            } else Log.i("tag", "R.string.barcode_error_format" +
                    CommonStatusCodes.getStatusCodeString(resultCode));
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }
}
