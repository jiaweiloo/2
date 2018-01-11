package my.edu.tarc.mobilecashservice.JiaWei;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.KahHou.CheckRequest;
import my.edu.tarc.mobilecashservice.NanFung.LoginPage;
import my.edu.tarc.mobilecashservice.R;

public class DepositSelectCash extends HomePage {
    int user_id = 0;
    TextView txtViewUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_deposit_select_cash);
        super.replaceContentLayout(R.layout.activity_deposit_select_cash);
        setTitle("Select cash...");

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


    public void goToSelectArea(View view) {
        double amount = 0.0;
        Spinner mySpinner = findViewById(R.id.spinnerCash);
        String cashAmount = mySpinner.getSelectedItem().toString();
        CheckBox checkBox = findViewById(R.id.checkBox);

        if (checkBox.isChecked()) {
            Intent intent = new Intent(this, DepositSelectArea.class);

            if (cashAmount.equals("") || cashAmount.isEmpty()) {
                Log.i("[System]", cashAmount.toString());
                amount = 0.01;
            } else {
                Log.i("[System]", "Null");
                amount = Integer.parseInt(cashAmount);
            }
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("amount", String.valueOf(amount));
            editor.commit();
            startActivity(intent);
            this.finish();
        } else {
            showDialog();
        }
    }

    public void goToLogin() {

        Intent intentLogin = new Intent(this, LoginPage.class);
        intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
        startActivity(intentLogin);
        this.finish();
    }

    public void showDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(DepositSelectCash.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(DepositSelectCash.this);
        }
        builder.setTitle("Fail")
                .setMessage("Please agree with terms and condition before proceeding.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
