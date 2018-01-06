package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.KahHou.WithdrawalRecordAdapter;
import my.edu.tarc.mobilecashservice.R;

public class DepositPairWithdrawal extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listViewRecordsWd;
    WithdrawalSQLHelper withdrawalSQLHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_pair_withdrawal);
        setTitle("Select withdrawal");

        listViewRecordsWd = findViewById(R.id.listViewRecordsWd);
        DepositPairWithdrawal DPW = new DepositPairWithdrawal();
        listViewRecordsWd.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("System", "onItemClick");
                complete(position);

            }
        });
        Log.i("System", "onCreate");
        updateList();
    }


    private void updateList() {
        //Retrieve records from SQLite
        withdrawalSQLHelper = new WithdrawalSQLHelper(this);

        List<Withdrawal> values = withdrawalSQLHelper.getAllUsers();

        if(values.isEmpty()){
            Toast.makeText(getApplicationContext(), "No records", Toast.LENGTH_SHORT).show();
        }

        WithdrawalRecordAdapter adapter = new WithdrawalRecordAdapter(this, R.layout.withdrawal_record, values);

        //Link adapter to ListView
        listViewRecordsWd.setAdapter(adapter);
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i("System", "onItemClick");
        Withdrawal withdrawal = null;
        //Retrieve records from SQLite
        withdrawalSQLHelper = new WithdrawalSQLHelper(this);
        List<Withdrawal> values = withdrawalSQLHelper.getAllUsers();

        //Log.i("System", "Value size :" + values.size());
        Toast.makeText(this, "Withdrawal ID :" + values.get(position).getWithdrawal_id(), Toast.LENGTH_SHORT).show();
        withdrawal = values.get(position);

        Intent intent = new Intent(this, DepositSecurityCode.class);
        intent.putExtra("withdrawal_id", withdrawal.getWithdrawal_id());
        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();
    }

    public void Toast(View view){
        Toast.makeText(this, "Button Toast : Pressed", Toast.LENGTH_SHORT).show();
    }

    public void complete(int position){
        Withdrawal withdrawal = null;
        //Retrieve records from SQLite
        withdrawalSQLHelper = new WithdrawalSQLHelper(this);
        List<Withdrawal> values = withdrawalSQLHelper.getAllUsers();

        //Log.i("System", "Value size :" + values.size());
        Toast.makeText(this, "Withdrawal ID :" + values.get(position).getWithdrawal_id(), Toast.LENGTH_SHORT).show();
        withdrawal = values.get(position);


        Intent intent = new Intent(this, DepositSecurityCode.class);
        intent.putExtra("withdrawal_id", withdrawal.getWithdrawal_id());
        setResult(CommonStatusCodes.SUCCESS, intent);
        finish();
    }
}
