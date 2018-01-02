package my.edu.tarc.mobilecashservice.KahHou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

public class RequestCash extends AppCompatActivity {
    Withdrawal withdraw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_cash);
        withdraw = new Withdrawal();
        //Log.i("HIHIHIHIHIH",Integer.toString(getIntent().getExtras().getInt("userID")));
        withdraw.setUser_id(getIntent().getExtras().getInt("userID"));
        withdraw.setStatus("Unsucessful");
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
