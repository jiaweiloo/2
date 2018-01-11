package my.edu.tarc.mobilecashservice.JiaWei;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import my.edu.tarc.mobilecashservice.DatabaseHelper.DepositSQLHelper;
import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;
import my.edu.tarc.mobilecashservice.barcode.BarcodeCaptureActivity;

public class DepositScanQRcode extends HomePage {
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    private TextView mResultTextView;
    Deposit deposit = new Deposit();
    DepositSQLHelper depositDataSource;
    WithdrawalSQLHelper withdrawalSQLHelper;
    UserSQLHelper userSQLHelper;
    TextView tviewShowInfo;
    Button scanBarcodeButton;
    int user_id = 0;
    double amount = 0.0;
    int withdrawal_id = 0;
    int deposit_id = 0;
    Withdrawal wit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_deposit_scan_qrcode);
        super.replaceContentLayout(R.layout.activity_deposit_scan_qrcode);
        setTitle("Scan QR Code");

        depositDataSource = new DepositSQLHelper(this);
        withdrawalSQLHelper = new WithdrawalSQLHelper(this);
        userSQLHelper = new UserSQLHelper(this);

        tviewShowInfo = findViewById(R.id.tviewShowInfo);
        mResultTextView = findViewById(R.id.result_textview);
        scanBarcodeButton = findViewById(R.id.scan_barcode_button);

        //Grab user_id and amount from SharedPreferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        amount = Double.parseDouble(sharedPref.getString("amount", "0.0"));
        deposit_id = sharedPref.getInt("deposit_id", 0);
        withdrawal_id = sharedPref.getInt("withdrawal_id", 0);
        final ProgressDialog mProgressDialog;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Loading.... Please wait");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        new CountDownTimer(2000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                //UpdateTextField();
            }
            public void onFinish() {
                mProgressDialog.dismiss();
                deposit = depositDataSource.getDeposit(deposit_id);
                tviewShowInfo.setText("Deposit id : " + deposit.getDeposit_id() +"\nWithdrawal ID: "+deposit.getWithdrawal_id());
            }
        }.start();


    }

    public void btnScanQR(View view) {
        if (scanBarcodeButton.getText().equals("Back to main menu")) {
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    //mResultTextView.setText(barcode.displayValue);
                    //pair with withdrawal id
                    pair(Integer.parseInt(barcode.displayValue));
                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e("tag", String.format("R.string.barcode_error_format",
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    public void pair(int withdrawalID) {
        if (deposit.getWithdrawal_id() == withdrawalID) {
            deposit.setStatus("complete");
            depositDataSource.updateDeposit(deposit);


            //update withdrawal status to complete
            wit  = withdrawalSQLHelper.getWithdrawal(withdrawalID);
            wit.setStatus("complete");
            withdrawalSQLHelper.updateWithdrawal(wit);

            UserRecord temp = userSQLHelper.getUser(deposit.getUser_id());
            //update user's wallet amount
            temp.setWallet_balance(temp.getWallet_balance() + deposit.getAmount());
            userSQLHelper.updateUser(temp);

            mResultTextView.setText("Scan code complete txn withdrawal id : " + withdrawalID);

            scanBarcodeButton.setText("Back to main menu");
        } else {
            mResultTextView.setText("QR code not matched, please try again! ");
            Toast.makeText(DepositScanQRcode.this,
                    "Withdrawal id :" + withdrawalID, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDial(View view) {
        int user_id = 0;
        int withdrawal_id = deposit.getWithdrawal_id();
        String phoneNumber = "0";
        Withdrawal withdrawal;
        withdrawal = withdrawalSQLHelper.getWithdrawal(withdrawal_id);
        user_id = withdrawal.getUser_id();
        phoneNumber = userSQLHelper.getUser(user_id).getPhone();
        /*Toast.makeText(DepositScanQRcode.this,
                "Contacting user : " + user_id + " w/ phone : " + phoneNumber, Toast.LENGTH_SHORT).show();
                */
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    public void onCancel(View view) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(DepositScanQRcode.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(DepositScanQRcode.this);
        }
        builder.setTitle("Please confirm")
                .setMessage("Are you sure you want to cancel? ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deposit.setStatus("cancelled");
                        depositDataSource.updateDeposit(deposit);

                        //update withdrawal status to complete
                        wit  = withdrawalSQLHelper.getWithdrawal(deposit.getWithdrawal_id());
                        wit.setStatus("cancelled");
                        withdrawalSQLHelper.updateWithdrawal(wit);

                        Toast.makeText(DepositScanQRcode.this,
                                "Transaction cancelled !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
