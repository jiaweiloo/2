package my.edu.tarc.mobilecashservice.KahHou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

public class ConfirmCash extends AppCompatActivity {
    String[] texts = new String[]{"Name : James\nAge: 20\nGender: Male", "Name : Aisha\nAge: 22\nGender: Female", "Name : Emma\nAge: 30\nGender: Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_cash);

        TextView userInfo = (TextView) findViewById(R.id.userInfo);

        Random random = new Random();
        userInfo.setText(texts[random.nextInt(3)]);
    }

    public void btnAccept(View view) {
        Intent intent = new Intent(this, QRConfirm.class);
        //intent.putExtra("cashAmount",getIntent().getStringExtra("cashAmount"));
        intent.putExtra("withdraw",(Withdrawal)getIntent().getSerializableExtra("withdraw"));
        startActivity(intent);
    }

    public void btnCancel(View view) {
        WithdrawalSQLHelper userDataSource = new WithdrawalSQLHelper(this);
        userDataSource.insertWithdrawal((Withdrawal)getIntent().getSerializableExtra("withdraw"));
        this.finish();
        Intent intent = new Intent(this, RequestCash.class);
        startActivity(intent);
    }
}
