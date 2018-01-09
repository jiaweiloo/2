package my.edu.tarc.mobilecashservice.KahHou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;

public class QRConfirm extends AppCompatActivity {
    WithdrawalSQLHelper withdrawalDataSource;
    int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrconfirm);

        withdrawalDataSource = new WithdrawalSQLHelper(QRConfirm.this);
        //Intent in serializable
        Withdrawal withdraw = (Withdrawal) getIntent().getSerializableExtra("withdraw");

        int withdrawal_id = 300001;
        Withdrawal temp = new Withdrawal();

        if (temp.getWithdrawal_id() != 0) {
            withdrawal_id = temp.getWithdrawal_id() + 1;
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);

        //withdraw.setWithdrawal_id(300001);
        //withdraw.setStatus("SUCCESSFUL");

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        Withdrawal wt = new Withdrawal(300002, user_id, 0.0, 0,   300001, formattedDate, "pending");
        withdrawalDataSource.insertWithdrawal(wt);
        //this.finish();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QRConfirm.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QRConfirm.this, CheckRequest.class);
                //intent.putExtra("user_id", withdraw.getUser_id());
                startActivity(intent);
            }
        }, 5000);
    }
}