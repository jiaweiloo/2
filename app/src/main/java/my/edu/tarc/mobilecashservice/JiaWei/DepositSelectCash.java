package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import my.edu.tarc.mobilecashservice.KahHou.CheckRequest;
import my.edu.tarc.mobilecashservice.NanFung.LoginPage;
import my.edu.tarc.mobilecashservice.R;

public class DepositSelectCash extends AppCompatActivity {
    int user_id = 0;
    TextView txtViewUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_select_cash);

        txtViewUserID = findViewById(R.id.txtViewUserID);

        // The intent will not be null here.
        Intent intent = getIntent();

        // Get the extras (if there are any)
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getString("user_id") != null) {
                user_id = Integer.parseInt(bundle.getString("user_id"));
                txtViewUserID.setText("User id > " + String.valueOf(user_id));
            } else {
                txtViewUserID.setText("User id > null");
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_checkDatabase) {
            Intent intent = new Intent(this, AllDepositRecords.class);
            //intent.putExtra("message", txtAmount.getText().toString());
            startActivityForResult(intent, 2);
            return true;
        } else if (id == R.id.action_checkWithdraw) {
            Intent intent = new Intent(this, CheckRequest.class);
            intent.putExtra("userID", user_id);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            goToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToSelectArea(View view) {
        double amount = 0.0;
        EditText txtAmount = findViewById(R.id.txtAmount);
        Intent intent = new Intent(this, DepositSelectArea.class);

        if (txtAmount.getText().toString().equals("") || txtAmount.getText().toString().isEmpty()) {
            Log.i("[System]", txtAmount.getText().toString());
            amount = 0.01;
        } else {
            Log.i("[System]", "Null");
            amount = Integer.parseInt(txtAmount.getText().toString());
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("amount", String.valueOf(amount));
        editor.commit();
        startActivity(intent);
    }

    public void goToLogin() {

        Intent intentLogin = new Intent(this, LoginPage.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
        startActivity(intentLogin);
        this.finish();
    }


}
