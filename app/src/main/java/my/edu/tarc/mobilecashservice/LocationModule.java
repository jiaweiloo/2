package my.edu.tarc.mobilecashservice;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import my.edu.tarc.mobilecashservice.DatabaseHelper.LocationSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Location;
import my.edu.tarc.mobilecashservice.JiaWei.LocationAdapter;

public class LocationModule extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listViewRecordsLoc;
    LocationSQLHelper locationDataSource;
    TextView txtxViewLocAdd;

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
        setContentView(R.layout.activity_location_module);

        //listViewRecordsLoc = findViewById(R.id.listViewRecordsLoc);
        //listViewRecordsLoc.setOnItemClickListener(this);

        locationDataSource = new LocationSQLHelper(this);


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

    public void addRecord(View v) {
        Location temploc = null;
        if (locationDataSource.getLastRecord() != null) {
            temploc = locationDataSource.getLastRecord();
        } else {
            temploc.setLocation_id(400000);
        }
        Location loc = new Location(temploc.getLocation_id() + 1, "Klang", 3.12, 101.74, "available");
        locationDataSource.insertLocation(loc);
        updateList();
    }

    private void updateList() {
        //Retrieve records from SQLite
        //locationDataSource = new DepositSQLHelper(this);

        final List<Location> values = locationDataSource.getAllLocations();
        /*
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getStatus().equals("complete")) {
                values.remove(i);
            }
        } */
        LocationAdapter adapter = new LocationAdapter(this,
                R.layout.location_record, values);
        //Link adapter to ListView
        listViewRecordsLoc.setAdapter(null);
        listViewRecordsLoc.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(this, "Position :" + position, Toast.LENGTH_SHORT).show();
    }

    public void deleteAll(View view) {
        //depositDataSource = new DepositSQLHelper(this);
        int totaldeleted = locationDataSource.deleteAllLocation();

        Snackbar.make(view, "Total records deleted :" + totaldeleted, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        updateList();
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
                    location_tab_records ltr = new location_tab_records();
                    //   listViewRecordsLoc = ltr.listViewRecordsLoc;
//                    listViewRecordsLoc.setOnItemClickListener(LocationModule.this);
                    return ltr;
                case 1:
                    location_tab_add lta = new location_tab_add();
                    return lta;
                default:
                    return null;
            }

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
    public static class location_tab_records extends Fragment implements AdapterView.OnItemClickListener{
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
            updateList();
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

            final List<Location> values = locationDataSource.getAllLocations();
        /*
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getStatus().equals("complete")) {
                values.remove(i);
            }
        } */
            LocationAdapter adapter = new LocationAdapter(getActivity(),
                    R.layout.location_record, values);
            //Link adapter to ListView
            listViewRecordsLoc.setAdapter(null);
            listViewRecordsLoc.setAdapter(adapter);
        }

    }

    /**
     * A placeholder location_tab_add containing a simple view.
     */
    public static class location_tab_add extends Fragment {
        TextView txtxViewLocAdd;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView2 = inflater.inflate(R.layout.location_tab_add, container, false);
            txtxViewLocAdd = rootView2.findViewById(R.id.txtxViewLocAdd);
            txtxViewLocAdd.setText("Location Add !");
            return rootView2;
        }
    }

}
