package my.edu.tarc.mobilecashservice;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.LocationSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Location;
import my.edu.tarc.mobilecashservice.JiaWei.LocationAdapter;

public class LocationModule extends HomePage implements AdapterView.OnItemClickListener {

    LocationSQLHelper locationDataSource;
    double x;
    double y;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_location_module);
        super.replaceContentLayout(R.layout.activity_location_module);
        setTitle("Location Module");
        //listViewRecordsLoc = findViewById(R.id.listViewRecordsLoc);
        //listViewRecordsLoc.setOnItemClickListener(this);

        locationDataSource = new LocationSQLHelper(this);

        //Get location to display
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    public void dataChanged() {
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(this, "Position :" + position, Toast.LENGTH_SHORT).show();
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position) {
                case 0:

                    Log.i("tag", "tab record called!");
                    location_tab_records ltr = new location_tab_records();
                    return ltr;
                case 1:
                    location_tab_add lta = new location_tab_add();
                    return lta;
                default:
                    return null;
            }

        }


        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            //LocationModule.this.updateList();
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }


    }

    /**
     * A placeholder location_tab_records containing a simple view.
     */
    public static class location_tab_records extends Fragment implements AdapterView.OnItemClickListener {
        TextView txtxViewLocTitle;
        ListView listViewRecordsLoc;
        LocationSQLHelper locationDataSource;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            locationDataSource = new LocationSQLHelper(getActivity());

            View rootView = inflater.inflate(R.layout.location_tab_records, container, false);
            txtxViewLocTitle = rootView.findViewById(R.id.txtxViewLocTitle);
            listViewRecordsLoc = rootView.findViewById(R.id.listViewRecordsLoc);
            listViewRecordsLoc.setOnItemClickListener(location_tab_records.this);

            txtxViewLocTitle.setText("Location Database ! ");

            final ProgressDialog mProgressDialog;

            mProgressDialog = new ProgressDialog(getActivity());
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
            return rootView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Toast.makeText(getActivity(), "Position :" + position, Toast.LENGTH_SHORT).show();
            // Toast.makeText(location_tab_records.this, "", Toast.LENGTH_SHORT).show();
        }

        private void updateList() {
            //Retrieve records from SQLite
            //locationDataSource = new DepositSQLHelper(this);
            List<Location> values = null;

            if (locationDataSource.getAllLocations() != null) {
                values = locationDataSource.getAllLocations();
                LocationAdapter adapter = new LocationAdapter(getActivity(),
                        R.layout.location_record, values);
                //Link adapter to ListView
                listViewRecordsLoc.setAdapter(null);
                listViewRecordsLoc.setAdapter(adapter);
            }

        }

    }

    /**
     * A placeholder location_tab_add containing a simple view.
     */
    public static class location_tab_add extends Fragment implements View.OnClickListener {
        TextView txtxViewLocAdd, txtxViewLocXY;
        EditText etLocX, etLocY, etName, etStatus;
        LocationSQLHelper locationDataSource;
        Button btnDelAll;
        Button btnAdd;
        Double location_x, location_y;
        String location_name, location_status;
        double x;
        double y;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            locationDataSource = new LocationSQLHelper(getActivity());


            View rootView2 = inflater.inflate(R.layout.location_tab_add, container, false);
            txtxViewLocAdd = rootView2.findViewById(R.id.txtxViewLocAdd);
            txtxViewLocXY = rootView2.findViewById(R.id.txtxViewLocXY);
            etLocX = rootView2.findViewById(R.id.etLocX);
            etLocY = rootView2.findViewById(R.id.etLocY);
            etName = rootView2.findViewById(R.id.etName);
            etStatus = rootView2.findViewById(R.id.etStatus);
            btnDelAll = rootView2.findViewById(R.id.btnDelAll);
            btnAdd = rootView2.findViewById(R.id.btnAdd);
            btnDelAll.setOnClickListener(this);
            btnAdd.setOnClickListener(this);

            txtxViewLocAdd.setText("Location Add !");
            ((LocationModule) getActivity()).getLocation();
            x = ((LocationModule) getActivity()).x;
            y = ((LocationModule) getActivity()).y;
            txtxViewLocXY.setText("X: " + x + " Y: " + y);
            etLocX.setText(String.valueOf(x));
            etLocY.setText(String.valueOf(y));
            return rootView2;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnDelAll:
                    int totaldeleted = locationDataSource.deleteAllLocation();

                    Snackbar.make(view, "Total records deleted :" + totaldeleted, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
                case R.id.btnAdd:
                    Location temploc;

                    temploc = locationDataSource.getLastRecord();
                    if (temploc.getLocation_id() == 0) {
                        temploc.setLocation_id(400000);
                    }

                    location_x = Double.parseDouble(etLocX.getText().toString());
                    location_y = Double.parseDouble(etLocY.getText().toString());
                    location_name = etName.getText().toString();
                    location_status = etStatus.getText().toString();

                    Location loc = new Location(
                            temploc.getLocation_id() + 1,
                            location_name,
                            location_x,
                            location_y,
                            location_status);

                    locationDataSource.insertLocation(loc);
                    Snackbar.make(view, "Record added :" + loc.getLocation_id(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    break;
                default:
                    break;
            }
            ((LocationModule) getActivity()).dataChanged();
        }


    }

}
