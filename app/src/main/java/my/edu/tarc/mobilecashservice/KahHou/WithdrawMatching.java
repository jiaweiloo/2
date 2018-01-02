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
    TextView countTime;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_matching);
        countTime = (TextView)findViewById(R.id.countTime);
        //String sWaitingPeriod = waitingPeriod.getText().toString();
        new CountDownTimer(Integer.parseInt(getIntent().getStringExtra("waitingPeriod"))*60000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                countTime.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                countTime.setText("done!");
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
                intent.putExtra("withdraw",(Withdrawal)getIntent().getSerializableExtra("withdraw"));
                startActivity(intent);
            }
        }, 5000);
    }

    public void btnStop(View view) {
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(this, RequestCash.class);
        startActivity(intent);
    }

}
