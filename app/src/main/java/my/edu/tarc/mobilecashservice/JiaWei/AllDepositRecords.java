package my.edu.tarc.mobilecashservice.JiaWei;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.DepositSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.R;

public class AllDepositRecords extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView listViewRecords;
    DepositSQLHelper depositDataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_deposit_records);
        listViewRecords = (ListView) findViewById(R.id.listViewRecords);
        listViewRecords.setOnItemClickListener(this);

        updateList();
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
        //Retrieve records from SQLite
        depositDataSource = new DepositSQLHelper(this);

        final List<Deposit> values = depositDataSource.getAllDeposits();
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getStatus().equals("complete")) {
                values.remove(i);
            }
        }
        DepositRecordAdapter adapter = new DepositRecordAdapter(this,
                R.layout.deposit_record, values);
        //Link adapter to ListView
        listViewRecords.setAdapter(null);
        listViewRecords.setAdapter(adapter);
    }

    private void addDummyData() {
        Deposit dep = new Deposit(200001, 100001, 50, 300001, 400001, "pending");
        Deposit dep2 = new Deposit(200002, 100001, 60, 300002, 400001, "pending");
        Deposit dep3 = new Deposit(200003, 200001, 70, 300003, 400001, "pending");
        depositDataSource.insertDeposit(dep);
        depositDataSource.insertDeposit(dep2);
        depositDataSource.insertDeposit(dep3);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(this, "Position :" + position, Toast.LENGTH_SHORT).show();
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }

    protected void onPause() {
        depositDataSource.close();
        super.onPause();
    }

    public void deleteAll(View view) {
        depositDataSource = new DepositSQLHelper(this);
        int totaldeleted = depositDataSource.deleteAllDeposit();

        Snackbar.make(view, "Total records deleted :" + totaldeleted, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        updateList();
    }
}
