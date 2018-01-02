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
    int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_security_code);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amount = bundle.getString("amount");
            areaCode = bundle.getString("areaCode");
            user_id = Integer.parseInt(bundle.getString("user_id"));
        }
    }

    public void goToWaitingScreen(View view) {
        Intent intent = new Intent(this, DepositWaitingPage.class);
        intent.putExtra("amount", amount);
        intent.putExtra("areaCode", areaCode);
        intent.putExtra("user_id", String.valueOf(user_id));
        startActivityForResult(intent, 2);
    }
}
