package my.edu.tarc.mobilecashservice.KahHou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;


public class CheckRequest extends AppCompatActivity {
    ListView listViewRecords;
    WithdrawalSQLHelper WSH;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_request);
        setTitle("CheckRequest");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userID = sharedPref.getInt("user_id", 0);

        listViewRecords = findViewById(R.id.listViewRecords);
        //userID = getIntent().getExtras().getInt("user_id");

        listViewRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Toast.makeText(CheckRequest.this, "Position " + position, Toast.LENGTH_SHORT).show();
            }
        });
        WSH = new WithdrawalSQLHelper(this);

        WSH.getWithdrawal(300001);
        Log.e("tag","First time get request");
        final ProgressDialog mProgressDialog;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage("Loading.... Please wait");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        new CountDownTimer(2000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                //UpdateTextField();
            }
            public void onFinish() {
                mProgressDialog.dismiss();
                updateList();
            }
        }.start();

    }


    private void updateList() {
        //Retrieve records from SQLite

        final List<Withdrawal> values = WSH.getAllWithdrawals();

        if (values.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No records", Toast.LENGTH_SHORT).show();
        }

        for (int a = 0; a < values.size(); a++) {

            if (values.get(a).getUser_id() != userID)
                values.remove(a);
        }

        WithdrawalRecordAdapter adapter = new WithdrawalRecordAdapter(this, R.layout.withdrawal_record, values);

        //Link adapter to ListView
        listViewRecords.setAdapter(adapter);
    }

    public void btnHomePage(View view) {
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("user_id", userID);
        startActivity(intent);
    }
}
