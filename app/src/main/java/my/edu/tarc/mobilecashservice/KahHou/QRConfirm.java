package my.edu.tarc.mobilecashservice.KahHou;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;

public class QRConfirm extends AppCompatActivity {
    WithdrawalSQLHelper withdrawalDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrconfirm);

        withdrawalDataSource = new WithdrawalSQLHelper(QRConfirm.this);
        //Intent in serializable
        final Withdrawal withdraw = (Withdrawal) getIntent().getSerializableExtra("withdraw");

        int withdrawal_id = 300001;
        Withdrawal temp = withdrawalDataSource.getLastRecord();
        if (temp.getWithdrawal_id() != 0) {
            withdrawal_id = temp.getWithdrawal_id() + 1;
        }

        withdraw.setWithdrawal_id(withdrawal_id);
        withdraw.setStatus("SUCCESSFUL");
        withdrawalDataSource.insertWithdrawal(withdraw);
        //this.finish();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QRConfirm.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QRConfirm.this, CheckRequest.class);
                intent.putExtra("user_id", withdraw.getUser_id());
                startActivity(intent);
            }
        }, 5000);
    }
}