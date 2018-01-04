package my.edu.tarc.mobilecashservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.tarc.mobilecashservice.JiaWei.AllDepositRecords;
import my.edu.tarc.mobilecashservice.JiaWei.DepositSelectCash;
import my.edu.tarc.mobilecashservice.KahHou.CheckRequest;
import my.edu.tarc.mobilecashservice.KahHou.RequestCash;
import my.edu.tarc.mobilecashservice.NanFung.LoginPage;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int user_id = 0;
    TextView txtViewUserID;
    //boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Available in the future", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtViewUserID = headerView.findViewById(R.id.txtViewUserID);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);

        Log.i("tag", txtViewUserID.getText().toString() + " User ID: " + String.valueOf(user_id));

        if (user_id != 0) {
            txtViewUserID.setText(String.valueOf(user_id));

        } else {
            goToLogin();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_checkDatabase) {
            Intent intent = new Intent(this, AllDepositRecords.class);
            //intent.putExtra("message", txtAmount.getText().toString());
            startActivityForResult(intent, 2);
            return true;
        } else if (id == R.id.action_checkWithdraw) {
            Intent intent = new Intent(this, CheckRequest.class);
            intent.putExtra("userID", user_id);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            goToLogin();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToDeposit(View view) {

        Toast.makeText(HomePage.this, "Picture pressed!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DepositSelectCash.class);
        intent.putExtra("user_id", String.valueOf(user_id));
        startActivityForResult(intent, 1);
    }

    public void gotToWithdraw(View view) {
        Intent intent = new Intent(this, RequestCash.class);
        intent.putExtra("userID", user_id);
        startActivity(intent);
    }

    public void goToLogin() {
        getIntent().removeExtra("user_id");
        Intent intentLogin = new Intent(this, LoginPage.class);
        startActivityForResult(intentLogin, 1);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);

        Log.i("tag", txtViewUserID.getText().toString() + " User ID: " + String.valueOf(user_id));

        if (user_id != 0) {
            txtViewUserID.setText(String.valueOf(user_id));
        } else {
            goToLogin();
        }
    }
}
