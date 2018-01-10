package my.edu.tarc.mobilecashservice.KahHou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import my.edu.tarc.mobilecashservice.DatabaseHelper.DepositSQLHelper;
import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

public class ConfirmCash extends AppCompatActivity {
    String[] texts = new String[]{"Name : James\nAge: 20\nGender: Male", "Name : Aisha\nAge: 22\nGender: Female", "Name : Emma\nAge: 30\nGender: Female"};
    WithdrawalSQLHelper withdrawalDataSource;
    DepositSQLHelper depositDataSource;
    UserSQLHelper userDataSource;

    int withdrawal_id = 0;
    int user_id = 0;
    int location_id = 0;
    double amount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_cash);
        setTitle("Please proceed...");

        withdrawalDataSource = new WithdrawalSQLHelper(this);
        depositDataSource = new DepositSQLHelper(this);
        userDataSource = new UserSQLHelper(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
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
                updateTextField();
            }
        }.start();
    }

    public void btnAccept(View view) {
        Intent intent = new Intent(this, QRConfirm.class);
        //intent.putExtra("cashAmount",getIntent().getStringExtra("cashAmount"));
        intent.putExtra("withdraw", (Withdrawal) getIntent().getSerializableExtra("withdraw"));
        startActivity(intent);
    }

    public void btnCancel(View view) {
        WithdrawalSQLHelper userDataSource = new WithdrawalSQLHelper(this);
        userDataSource.insertWithdrawal((Withdrawal) getIntent().getSerializableExtra("withdraw"));
        this.finish();
        Intent intent = new Intent(this, RequestCash.class);
        startActivity(intent);
    }

    public void updateTextField(){
        Withdrawal wit = withdrawalDataSource.getWithdrawal(withdrawal_id);
        Deposit dep = depositDataSource.getDeposit(wit.getDeposit_id());
        UserRecord user = userDataSource.getUser(dep.getUser_id());

        TextView userInfo = findViewById(R.id.userInfo);
        Random random = new Random();
        userInfo.setText("User matched with:\n"+
                "Location at :" + dep.getLocation_id()+
                "\n User name: " + user.getUser_name()+
                "\n User phone number: " + user.getPhone()
        );

    }
}
