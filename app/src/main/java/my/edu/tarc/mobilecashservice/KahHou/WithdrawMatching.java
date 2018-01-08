package my.edu.tarc.mobilecashservice.KahHou;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

public class WithdrawMatching extends AppCompatActivity {
    TextView tViewcountTime;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_matching);
        tViewcountTime = findViewById(R.id.countTime);

        int waitingPeriod = Integer.parseInt(getIntent().getStringExtra("waitingPeriod"));

        waitingPeriod = 1;
        // Timer start
        // adjust the milli seconds here
        new CountDownTimer(waitingPeriod * 60000, 1000) {

            public void onTick(long millisUntilFinished) {

                //getTimeDifference method calculate remaining time and return string
                tViewcountTime.setText(getTimeDifference(millisUntilFinished));
            }

            public void onFinish() {
                tViewcountTime.setText("done!");
            }
        }.start();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WithdrawMatching.this, "User Found", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WithdrawMatching.this, ConfirmCash.class);
                //intent.putExtra("cashAmount",getIntent().getStringExtra("cashAmount"));
                //intent.putExtra("location",getIntent().getStringExtra("location"));
                intent.putExtra("withdraw", (Withdrawal) getIntent().getSerializableExtra("withdraw"));
                startActivity(intent);
            }
        }, 5000);

    }

    public void btnStop(View view) {
        handler.removeCallbacksAndMessages(null);
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
}
