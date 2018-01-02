package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import my.edu.tarc.mobilecashservice.R;

public class DepositSelectCash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_select_cash);

        // The intent will not be null here.
        Intent intent = getIntent();

        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras == null) {
            //if (extras.containsKey("isNewItem")) {
            //boolean isNew = extras.getBoolean("isNewItem", false);
            //}
            // TODO: Do something with the value of isNew.
            Log.i("[i]","NULL");
        }


    }

    public void goToSelectArea(View view) {
        EditText txtAmount = findViewById(R.id.txtAmount);
        Intent intent = new Intent(this, DepositSelectArea.class);
        intent.putExtra("amount", txtAmount.getText().toString());
        if (txtAmount.getText().toString() != null) {
            Log.i("[System]", txtAmount.getText().toString());
        } else {
            Log.i("[System]", "Null");
        }
        startActivityForResult(intent, 2);
    }

    public void goToCheckDatabase(){
        Intent intent = new Intent(this, AllDepositRecords.class);
        //intent.putExtra("message", txtAmount.getText().toString());
        startActivityForResult(intent, 2);
    }
}
