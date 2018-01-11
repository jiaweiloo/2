package my.edu.tarc.mobilecashservice.JiaWei;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.LocationSQLHelper;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;
import my.edu.tarc.mobilecashservice.Entity.Location;

public class DepositSelectArea extends HomePage implements AdapterView.OnItemClickListener {


    double amount = 0.0;
    int user_id = 0;
    TextView txtView;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double x;
    double y;
    LocationSQLHelper locationDataSource;
    ListView listViewRecords;
    int locationID = 0;
    List<Location> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_deposit_select_area);
        super.replaceContentLayout(R.layout.activity_deposit_select_area);
        setTitle("Select location");

        listViewRecords = (ListView) findViewById(R.id.listViewRecords);
        listViewRecords.setOnItemClickListener(this);


        txtView = findViewById(R.id.txtAmt);
        Bundle bundle = getIntent().getExtras();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        amount = Double.parseDouble(sharedPref.getString("amount", "0.0"));

        txtView.setText(String.format("%.2f", amount));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationDataSource = new LocationSQLHelper(this);
        getLocation();


        final ProgressDialog mProgressDialog;

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMessage(getResources().getString(R.string.wait));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        new CountDownTimer(120000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                //UpdateTextField();
                if(checkLoadFinished()){
                    this.onFinish();
                }
            }

            public void onFinish() {
                mProgressDialog.dismiss();
                updateList();
                this.cancel();
            }
        }.start();
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                x = Double.parseDouble(String.format("%.2f", location.getLatitude()));
                y = Double.parseDouble(String.format("%.2f", location.getLongitude()));
                Toast.makeText(this, "X : " + Double.toString(x) + " Y: " + Double.toString(y), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Unable to find correct location", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void updateList() {

        for (int i = values.size() - 1; i >= 0; i--) {
            //Log.i("System", i +" X coordinate : "+ Double.toString(x) + " "+ values.get(i).getLocation_x());
            if (values.get(i).getLocation_x() != x || values.get(i).getLocation_y() != y) {
                //Log.i("System", "remove :" + i);
                values.remove(i);
                Log.e("System", "Values removed at location " + i);
            }
        }

        LocationAdapter adapter = new LocationAdapter(this,
                R.layout.location_record, values);

        //Link adapter to ListView
        listViewRecords.setAdapter(null);
        listViewRecords.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        //Log.i("System", "Value size :" + values.size());
        Toast.makeText(this, "Location Name :" + values.get(position).getLocation_name(), Toast.LENGTH_SHORT).show();
        Location loc = values.get(position);

        Intent intent = new Intent(this, DepositSecurityCode.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("location_id", loc.getLocation_id());
        editor.commit();
        startActivity(intent);
        this.finish();
    }

    public void refresh(View v) {
        updateList();
    }

    /*
    protected void onPause() {
        locationDataSource.close();
        super.onPause();
    } */

    public boolean checkLoadFinished() {
        values = locationDataSource.getAllLocations();

        if (values.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public void finish() {
        super.finish();
    }
}
