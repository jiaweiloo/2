package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import my.edu.tarc.mobilecashservice.R;

public class DepositSecurityCode extends AppCompatActivity {

    String amount;
    String areaCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_security_code);
        Bundle bundle = getIntent().getExtras();
        amount = bundle.getString("amount");
        areaCode = bundle.getString("areaCode");
        if (amount != null && areaCode != null) {
            Log.i("[System]", amount);
        } else {
            Log.i("[System]", "Null");
        }
    }

    public void goToWaitingScreen(View view) {
        Intent intent = new Intent(this, DepositWaitingPage.class);
        intent.putExtra("amount", amount);
        intent.putExtra("areaCode", areaCode);
        startActivityForResult(intent, 2);
    }
}
