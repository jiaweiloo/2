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

import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.LocationSQLHelper;
import my.edu.tarc.mobilecashservice.R;
import my.edu.tarc.mobilecashservice.Entity.Location;

public class DepositSelectArea extends AppCompatActivity implements AdapterView.OnItemClickListener {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_select_area);
        setTitle("Select location");

        listViewRecords = (ListView) findViewById(R.id.listViewRecords);
        listViewRecords.setOnItemClickListener(this);


        txtView = findViewById(R.id.txtAmt);
        Bundle bundle = getIntent().getExtras();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        amount = Double.parseDouble(sharedPref.getString("amount", "0.0"));

        Log.i("DepositSelectArea.java", "User ID: " + String.valueOf(user_id) +" " + String.valueOf(amount));

        txtView.setText(String.valueOf(amount));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        getLocation();

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

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            android.location.Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                x = Double.parseDouble(String.format("%.2f", location.getLatitude()));
                y = Double.parseDouble(String.format("%.2f", location.getLongitude()));
                Toast.makeText(this, "X : " +Double.toString(x) +" Y: "+Double.toString(y), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Unable to find correct location", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

    private void updateList() {
        //Retrieve records from SQLite
        locationDataSource = new LocationSQLHelper(this);

        List<Location> values = locationDataSource.getAllLocations();

        for (int i = values.size() - 1; i >= 0; i--) {
            //Log.i("System", i +" X coordinate : "+ Double.toString(x) + " "+ values.get(i).getLocation_x());
            if (values.get(i).getLocation_x() != x || values.get(i).getLocation_y() != y) {
                //Log.i("System", "remove :" + i);
                values.remove(i);
            }
        }
        //Log.i("System", "Value size :" + values.size());

        LocationAdapter adapter = new LocationAdapter(this,
                R.layout.location_record, values);
        //Link adapter to ListView
        listViewRecords.setAdapter(null);
        listViewRecords.setAdapter(adapter);
    }

    public void deleteAll(View view) {
        locationDataSource = new LocationSQLHelper(this);
        int totaldeleted = locationDataSource.deleteAllLocation();

        Snackbar.make(view, "Total records deleted :" + totaldeleted, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        updateList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Location loc = null;
        //Retrieve records from SQLite
        locationDataSource = new LocationSQLHelper(this);
        List<Location> values = locationDataSource.getAllLocations();
        for (int i = values.size() - 1; i >= 0; i--) {
            if (values.get(i).getLocation_x() != x || values.get(i).getLocation_y() != y) {
                values.remove(i);
            }
        }
        //Log.i("System", "Value size :" + values.size());
        Toast.makeText(this, "Location Name :" + values.get(position).getLocation_name(), Toast.LENGTH_SHORT).show();
        loc = values.get(position);

        Intent intent = new Intent(this, DepositSecurityCode.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("location_id", loc.getLocation_id());
        editor.commit();
        startActivity(intent);
    }

    public void addRecord(View v) {
        Location temploc;

        temploc = locationDataSource.getLastRecord();
        if (temploc.getLocation_id() == 0) {
            temploc.setLocation_id(400000);
        }

        Location loc = new Location(
                temploc.getLocation_id() + 1,
                "WANGSA METROVIEW" + (temploc.getLocation_id() + 1),
                3.21,
                101.74,
                "AVAILABLE");
        locationDataSource.insertLocation(loc);
        updateList();
    }

    /*
    protected void onPause() {
        locationDataSource.close();
        super.onPause();
    } */
}
