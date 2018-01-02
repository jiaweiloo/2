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
    int user_id = 0;
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_select_area);
        //onMapReady();
        //MapView mapView = (MapView)findViewById(R.id.map);
        //onMapReady((GoogleMap)mapView);
        txtArea = findViewById(R.id.txtArea);
        txtView = findViewById(R.id.txtAmt);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            amount = bundle.getString("amount");
            user_id = Integer.parseInt(bundle.getString("user_id"));
        }
        txtView.setText(amount);
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
        intent.putExtra("user_id", String.valueOf(user_id));
        startActivityForResult(intent, 2);
    }
}
