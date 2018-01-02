package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import my.edu.tarc.mobilecashservice.R;

public class DepositSelectArea extends AppCompatActivity {

    TextView txtArea;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_select_area);
        //onMapReady();
        //MapView mapView = (MapView)findViewById(R.id.map);
        //onMapReady((GoogleMap)mapView);
        txtArea = findViewById(R.id.txtArea);
        Bundle bundle = getIntent().getExtras();
        amount = bundle.getString("amount");
        if (amount != null) {
            Log.i("[System]", amount);
        } else {
            Log.i("[System]", "Null");
        }
        TextView txtview = findViewById(R.id.txtAmt);
        txtview.setText(amount);
        //String message;
    }

    public void goToEnterTacNumber(View view) {
        Intent intent = new Intent(this, DepositSecurityCode.class);
        /*
        Bundle extras = new Bundle();
        extras.putString("amount",amount);
        extras.putString("areaCode",txtArea.getText().toString());
        intent.putExtras(extras); */

        intent.putExtra("amount", amount);
        intent.putExtra("areaCode", txtArea.getText().toString());
        startActivityForResult(intent, 2);
    }
}
