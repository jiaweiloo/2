package my.edu.tarc.mobilecashservice.KahHou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;

public class WithdrawMatching extends HomePage {
    TextView tViewcountTime;
    Handler handler;
    WithdrawalSQLHelper withdrawalDataSource;
    int withdrawal_id = 0;
    int user_id = 0;
    int location_id = 0;
    double amount = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_withdraw_matching);
        super.replaceContentLayout(R.layout.activity_withdraw_matching);
        setTitle(getResources().getString(R.string.title_matching));

        tViewcountTime = findViewById(R.id.countTime);
        withdrawalDataSource = new WithdrawalSQLHelper(this);

        int waitingPeriod = Integer.parseInt(getIntent().getStringExtra("waitingPeriod"));

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Loading.... Please wait");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        //waitingPeriod = 1;
        // Timer start
        // adjust the milli seconds here
        new CountDownTimer(waitingPeriod * 60000, 1000) {
            int timer = 0;
            boolean isFinished = false;

            public void onTick(long millisUntilFinished) {
                //getTimeDifference method calculate remaining time and return string
                tViewcountTime.setText(getTimeDifference(millisUntilFinished));
                //Splash loading screen before proceed to set QR Code
                if (timer == 3) {
                    mProgressDialog.dismiss();
                    newRow();
                }
                if (timer > 4 && !isFinished)
                    isFinished = checkWithdrawalStatus();

                if (isFinished) {
                    this.cancel();
                }
                timer++;
            }

            public void onFinish() {
                tViewcountTime.setText("Pairing fail. Please try again later.");

            }
        }.start();

    }

    public void btnStop(View view) {
        //handler.removeCallbacksAndMessages(null);
        Withdrawal temp  = withdrawalDataSource.getWithdrawal(withdrawal_id);
        temp.setStatus("cancelled");
        withdrawalDataSource.updateWithdrawal(temp);
        Intent intent = new Intent(this, RequestCash.class);
        startActivity(intent);
    }

    public String getTimeDifference(long millisUntilFinished) {
        String diff = "" + String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
        return diff;
    }

    public Boolean checkWithdrawalStatus() {
        Log.e("Check WITHDRAWAL_ID", "Withdrawal ID :" + withdrawal_id);

        Withdrawal temp = withdrawalDataSource.getWithdrawal(withdrawal_id);
        if (temp != null) {
            if (temp.getDeposit_id() != 0) {
                Toast.makeText(this, "Found a deposit users", Toast.LENGTH_SHORT).show();
                this.finish();
                Intent intent = new Intent(this, ConfirmCash.class);
                intent.putExtra("withdraw", (Withdrawal) getIntent().getSerializableExtra("withdraw"));
                startActivity(intent);
                return true;
            }
        }
        return false;
    }

    //create new row in database
    public void newRow() {

        Withdrawal temp = withdrawalDataSource.getLastRecord();
        if (withdrawal_id == 0) {

            if (temp.getWithdrawal_id() != 0) {
                withdrawal_id = temp.getWithdrawal_id() + 1;
                Log.i("QRConfirm: ", "Withdrawal id not empty :" + temp.getWithdrawal_id());
            } else {
                withdrawal_id = 300001;
                Log.i("QRConfirm: ", "Withdrawal id is empty set to default > " + withdrawal_id);
            }

            //Create an Withdrawal object and insert into database by using data from sharedPreferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            user_id = sharedPref.getInt("user_id", 0);
            location_id = sharedPref.getInt("location_id", 400000);
            amount = Double.parseDouble(sharedPref.getString("amount", "0.0"));

            String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            Withdrawal wt = new Withdrawal(
                    withdrawal_id,
                    user_id,
                    amount,
                    0,
                    location_id,
                    formattedDate,
                    "pending");

            withdrawalDataSource.insertWithdrawal(wt);

            //insert withdrawal_id
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("withdrawal_id", withdrawal_id);
            editor.commit();
        }
    }

    public void Refresh(View view) {
        newRow();
        checkWithdrawalStatus();
    }

    public boolean checkLoadFinished() {
        Withdrawal temp = withdrawalDataSource.getLastRecord();

        if (temp.getWithdrawal_id() != 0 )
            return false;
        else
            return true;
    }
}
