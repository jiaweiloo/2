package my.edu.tarc.mobilecashservice.JiaWei;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.DepositSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;

public class AllDepositRecords extends HomePage implements AdapterView.OnItemClickListener{
    ListView listViewRecords;
    DepositSQLHelper depositDataSource;
    List<Deposit> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_all_deposit_records);
        super.replaceContentLayout(R.layout.activity_all_deposit_records);
        setTitle("All Deposit Records");

        listViewRecords = findViewById(R.id.listViewRecords);
        listViewRecords.setOnItemClickListener(this);
        depositDataSource = new DepositSQLHelper(this);

        final ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage(getResources().getString(R.string.wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        new CountDownTimer(120000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                if (checkLoadFinished()) {
                    this.onFinish();
                }
            }

            public void onFinish() {
                mProgressDialog.dismiss();
                updateList();
                this.cancel();
            }
        }.start();
        //addDummyData();
    }

    public void addRecord(View v) {
        Deposit tempdep = null;
        if (depositDataSource.getLastRecord() != null) {
            tempdep = depositDataSource.getLastRecord();
        } else {
            tempdep.setDeposit_id(100000);
        }
        Deposit dep = new Deposit(tempdep.getDeposit_id() + 1, 100001, 50, 300001, 400001, "pending");
        depositDataSource.insertDeposit(dep);
        updateList();
    }

    private void updateList() {

        values = depositDataSource.getAllDeposits();
        /*
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getStatus().equals("complete")) {
                values.remove(i);
            }
        } */
        DepositRecordAdapter adapter2 = new DepositRecordAdapter(this,
                R.layout.deposit_record, values);
        //Link adapter to ListView
        listViewRecords.setAdapter(null);
        listViewRecords.setAdapter(adapter2);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(this, "Position :" + position, Toast.LENGTH_SHORT).show();
    }


    protected void onPause() {
        depositDataSource.close();
        super.onPause();
    }

    public boolean checkLoadFinished() {
        values = depositDataSource.getAllDeposits();

        if (values.isEmpty())
            return false;
        else
            return true;
    }
}
