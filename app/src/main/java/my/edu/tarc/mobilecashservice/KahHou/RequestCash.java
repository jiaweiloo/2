package my.edu.tarc.mobilecashservice.KahHou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

public class RequestCash extends AppCompatActivity {
    Withdrawal withdraw;
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_cash);
        withdraw = new Withdrawal();
        withdraw.setWithdrawal_id(300001);
        //Log.i("System",Integer.toString(getIntent().getExtras().getInt("userID")));
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        withdraw.setUser_id(user_id);
        withdraw.setStatus("unsuccessful");
    }


    public void btnNext(View view) {
        Spinner mySpinner = (Spinner) findViewById(R.id.spinnerCash);
        String cashAmount = mySpinner.getSelectedItem().toString();

        Intent intent = new Intent(this, SelectTimeLocation.class);
        //intent.putExtra("cashAmount", cashAmount);
        withdraw.setAmount(Double.parseDouble(cashAmount));
        intent.putExtra("withdraw",withdraw);
        startActivity(intent);
    }
}
