package my.edu.tarc.mobilecashservice.KahHou;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

public class SelectTimeLocation extends AppCompatActivity{
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double x;
    double y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time_location);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        Log.i("System",Double.toString(x));
        Log.i("System",Double.toString(y));
    }

    public void btnMatch(View view) {
        EditText waitingPeriod = (EditText) findViewById(R.id.editText2);
        String sWaitingPeriod = waitingPeriod.getText().toString();
        Spinner mySpinner = (Spinner) findViewById(R.id.spinnerLocation);
        String location = mySpinner.getSelectedItem().toString();
        Withdrawal withdraw = (Withdrawal) getIntent().getSerializableExtra("withdraw");
        withdraw.setLocation_x(x);
        withdraw.setLocation_y(y);

        if (sWaitingPeriod.matches("")) {
            Toast.makeText(this, "Please fill in waiting period", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(sWaitingPeriod) > 30) {
            Toast.makeText(this, "Waiting period cannot more than 30 minutes", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location.equals("Tunku Abdul Rahman University College")) {
            if (x != 3.21 || y != 101.72){
                Toast.makeText(this, "You are not locate at Tunku Tunku Abdul Rahman University College", Toast.LENGTH_SHORT).show();
                return;
            }else{
                Intent intent = new Intent(this, WithdrawMatching.class);
                //intent.putExtra("cashAmount",getIntent().getStringExtra("cashAmount"));
                intent.putExtra("waitingPeriod", waitingPeriod.getText().toString());
                //intent.putExtra("location",((Spinner) findViewById(R.id.spinnerLocation)).getSelectedItem().toString());
                intent.putExtra("withdraw", withdraw);
                startActivity(intent);
            }
        } else if (location.equals("Setapak Central")) {
            if (x != 3.20 || y != 101.72){
                Toast.makeText(this, "You are not located at Setapak Central", Toast.LENGTH_SHORT).show();
                return;
            }else{
                Intent intent = new Intent(this, WithdrawMatching.class);
                //intent.putExtra("cashAmount",getIntent().getStringExtra("cashAmount"));
                intent.putExtra("waitingPeriod", waitingPeriod.getText().toString());
                //intent.putExtra("location",((Spinner) findViewById(R.id.spinnerLocation)).getSelectedItem().toString());
                intent.putExtra("withdraw", withdraw);
                startActivity(intent);
            }
        } else if (location.equals("Jalan Genting Klang")) {
            if (x != 3.20 || y != 101.71){
                Toast.makeText(this, "You are not located at Jalan Genting Klang", Toast.LENGTH_SHORT).show();
                return;
            }else{
                Intent intent = new Intent(this, WithdrawMatching.class);
                //intent.putExtra("cashAmount",getIntent().getStringExtra("cashAmount"));
                intent.putExtra("waitingPeriod", waitingPeriod.getText().toString());
                //intent.putExtra("location",((Spinner) findViewById(R.id.spinnerLocation)).getSelectedItem().toString());
                intent.putExtra("withdraw", withdraw);
                startActivity(intent);
            }
        }
    }


    void getLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                x = Double.parseDouble(String.format("%.2f",location.getLatitude()));
                y = Double.parseDouble(String.format("%.2f",location.getLongitude()));
            } else {
                Toast.makeText(this, "Unable to find correct location", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }
}

