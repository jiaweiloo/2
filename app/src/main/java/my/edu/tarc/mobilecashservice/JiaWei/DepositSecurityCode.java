package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.R;

public class DepositSecurityCode extends AppCompatActivity {

    String amount;
    String areaCode;
    int user_id = 0;
    TextView txtPassword;
    UserSQLHelper userSQLHelper;
    UserRecord user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_security_code);

        txtPassword = findViewById(R.id.txtPassword);
        userSQLHelper = new UserSQLHelper(this);
    }

    public void goToWaitingScreen(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        user = userSQLHelper.getUser(user_id);
        if(user.getPassword().equals(txtPassword.getText().toString())) {
            Intent intent = new Intent(this, DepositWaitingPage.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Password not match, please try again!" , Toast.LENGTH_SHORT).show();
        }
    }
}
