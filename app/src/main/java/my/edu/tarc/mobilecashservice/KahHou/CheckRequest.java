package my.edu.tarc.mobilecashservice.KahHou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.WithdrawalSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;


public class CheckRequest extends AppCompatActivity {
    ListView listViewRecords;
    WithdrawalSQLHelper userSQLHelper;
    int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_request);
        listViewRecords = findViewById(R.id.listViewRecords);
        userID = getIntent().getExtras().getInt("user_id");
        updateList();
    }


    private void updateList() {
        //Retrieve records from SQLite
        userSQLHelper = new WithdrawalSQLHelper(this);

        final List<Withdrawal> values = userSQLHelper.getAllUsers();

        if(values.isEmpty()){
            Toast.makeText(getApplicationContext(), "No records", Toast.LENGTH_SHORT).show();
        }

        for(int a=0;a<values.size();a++){
            //Log.i("hihihi",Integer.toString(values.get(a).getUser_id())+"+" +Integer.toString(userID));
            if(values.get(a).getUser_id()!=userID)
                values.remove(a);
        }

        WithdrawalRecordAdapter adapter = new WithdrawalRecordAdapter(this, R.layout.withdrawal_record, values);

        //Link adapter to ListView
        listViewRecords.setAdapter(adapter);
    }

    public void btnHomePage(View view) {
        Intent intent = new Intent(this, HomePage.class);
        intent.putExtra("user_id",userID);
        startActivity(intent);
    }
}
