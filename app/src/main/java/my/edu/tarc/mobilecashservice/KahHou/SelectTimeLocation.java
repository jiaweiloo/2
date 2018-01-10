package my.edu.tarc.mobilecashservice.KahHou;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.LocationSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.JiaWei.DepositSecurityCode;
import my.edu.tarc.mobilecashservice.JiaWei.LocationAdapter;
import my.edu.tarc.mobilecashservice.R;

public class SelectTimeLocation extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double x;
    double y;
    LocationSQLHelper locationDataSource;
    ListView listViewRecords;

    List<my.edu.tarc.mobilecashservice.Entity.Location> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time_location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        listViewRecords = findViewById(R.id.listViewRecords);
        listViewRecords.setOnItemClickListener(this);
        locationDataSource = new LocationSQLHelper(this);
        //Log.i("System", Double.toString(x));
        //Log.i("System", Double.toString(y));
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

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        my.edu.tarc.mobilecashservice.Entity.Location loc = null;

        //Log.i("System", "Value size :" + values.size());
        Toast.makeText(this, "Location Name :" + values.get(position).getLocation_name(), Toast.LENGTH_SHORT).show();
        loc = values.get(position);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("location_id", loc.getLocation_id());
        editor.commit();


        EditText waitingPeriod = findViewById(R.id.editText2);
        //String sWaitingPeriod = waitingPeriod.getText().toString();
        if (waitingPeriod.getText() == null || waitingPeriod.getText().toString().equals("")) {
            showDialog();
            waitingPeriod.setText("1");
        } else {
            Intent intent = new Intent(this, WithdrawMatching.class);
            intent.putExtra("waitingPeriod", waitingPeriod.getText().toString());
            startActivity(intent);
        }
    }

    private void updateList() {

        //Toast.makeText(this, "Update list called!", Toast.LENGTH_SHORT).show();
        values = locationDataSource.getAllLocations();
        Toast.makeText(this, "Values size " + values.size(), Toast.LENGTH_SHORT).show();

        for (int i = values.size()-1 ; i >= 0 ; i--) {

            if (values.get(i).getLocation_x() != x || values.get(i).getLocation_y() != y) {
                Log.e("System", "remove :" + i);
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

    public void showDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SelectTimeLocation.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SelectTimeLocation.this);
        }
        builder.setTitle("Fail")
                .setMessage("Please filled in all details before proceeding.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

