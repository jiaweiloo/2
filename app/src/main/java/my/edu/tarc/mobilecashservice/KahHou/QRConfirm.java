package my.edu.tarc.mobilecashservice.KahHou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;

public class QRConfirm extends AppCompatActivity {
    WithdrawalSQLHelper withdrawalDataSource;
    private ImageView QR;
    int user_id = 0;
    int withdrawal_id = 300001;
    int location_id = 400001;
    double amount = 0.0;
    TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrconfirm);
        QR = findViewById(R.id.imageView);
        textView4 = findViewById(R.id.textView4);
        withdrawalDataSource = new WithdrawalSQLHelper(QRConfirm.this);
        //Intent in serializable
        //Withdrawal withdraw =  (Withdrawal) getIntent().getSerializableExtra("withdraw");

        Withdrawal temp = withdrawalDataSource.getLastRecord();

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Loading.... Please wait");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();


        new CountDownTimer(120000, 1000) { // adjust the milli seconds here
            int timer = 0;
            boolean isFinished = false;

            public void onTick(long millisUntilFinished) {
                //UpdateTextField();

                //Splash loading screen before proceed to set QR Code
                if (timer > 2 && timer < 4) {
                    mProgressDialog.dismiss();
                    setQRcode();
                }
                if (timer > 4 && !isFinished)
                    isFinished = checkWithdrawalStatus();
                if (isFinished) {
                    this.cancel();
                }
                timer++;
            }

            public void onFinish() {
                textView4.setText("pairing fail");
            }
        }.start();


        //Delay 6 seconds before navigating to finish screen
        //this.finish();
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(QRConfirm.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QRConfirm.this, CheckRequest.class);
                startActivity(intent);
            }
        }, 6000);
        */
    }

    public void setQRcode() {
        Withdrawal temp = withdrawalDataSource.getLastRecord();

        Log.e("Check QRCONFIRM", "Withdrawal ID :" + temp.getWithdrawal_id());
        if (temp.getWithdrawal_id() != 0) {
            withdrawal_id = temp.getWithdrawal_id() + 1;
        }

        //Print QR Code to screen
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(Integer.toString(withdrawal_id), BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
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
        Log.i("Hi", Integer.toString(wt.getWithdrawal_id()));

        withdrawalDataSource.insertWithdrawal(wt);
    }

    public Boolean checkWithdrawalStatus() {
        Log.e("Check WITHDRAWAL_ID", "Withdrawal ID :" + withdrawal_id);

        Withdrawal temp = withdrawalDataSource.getWithdrawal(withdrawal_id);
        if (temp != null) {
            if (temp.getStatus().equals("complete")) {
                Toast.makeText(QRConfirm.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                this.finish();
                Intent intent = new Intent(QRConfirm.this, CheckRequest.class);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }
}