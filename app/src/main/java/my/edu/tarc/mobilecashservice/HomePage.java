package my.edu.tarc.mobilecashservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.JiaWei.AllDepositRecords;
import my.edu.tarc.mobilecashservice.JiaWei.DepositSelectCash;
import my.edu.tarc.mobilecashservice.KahHou.CheckRequest;
import my.edu.tarc.mobilecashservice.KahHou.RequestCash;
import my.edu.tarc.mobilecashservice.NanFung.*;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int user_id = 0;
    TextView txtViewUserID;
    TextView txtViewName;
    TextView txtViewBal;
    UserRecord user = new UserRecord();
    //boolean isLogin = false;
    UserSQLHelper userSQLHelper;
    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sidebar_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateTextField();
                checkLoadFinished();
                Snackbar.make(view, "Refresh !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtViewUserID = headerView.findViewById(R.id.txtViewUserID);
        txtViewName = headerView.findViewById(R.id.txtViewName);
        txtViewBal = findViewById(R.id.txtViewBal);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);
        userSQLHelper = new UserSQLHelper(this);

        if (user_id != 0) {
            user = userSQLHelper.getUser(user_id);

            Log.e("tag", "First time get request");

            final ProgressDialog mProgressDialog;

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMessage("Loading.... Please wait");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();

            new CountDownTimer(120000, 1000) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    //UpdateTextField();
                    if (checkLoadFinished()) {
                        this.onFinish();
                    }
                }

                public void onFinish() {
                    mProgressDialog.dismiss();
                    UpdateTextField();
                    this.cancel();
                }

            }.start();

            Log.i("tag", txtViewUserID.getText().toString() + " User ID: " + String.valueOf(user_id));
        } else {
            goToLogin();
        }
    }

    public void UpdateTextField() {

        txtViewUserID.setText("User ID: " + String.valueOf(user_id));
        txtViewName.setText(user.getUser_name());
        txtViewBal.setText(String.format("RM %.2f", user.getWallet_balance()));
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
        } else if (id == R.id.action_myaccount) {
            Intent intent = new Intent(this, MyAccount.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_logout) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPref.edit().clear().commit();
            //sharedPref.
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


        if (id == R.id.nav_Home) {
            if(this.getClass() != HomePage.class){
                Intent intent = new Intent(this, HomePage.class);
                startActivity(intent);
                this.finish();
            }

        } else if (id == R.id.nav_Deposit) {
            // Handle the deposit action

        } else if (id == R.id.nav_Withdrawal) {
            //Handle the withdrawal action

        } else if (id == R.id.nav_Wallet) {
            // handle the wallet action

        } else if (id == R.id.nav_CreditCard) {
            // handle the credit card action

        } else if (id == R.id.nav_mgmt_menu) {
            // handle the management menu
            Intent intent = new Intent(this, ManagementMenu.class);
            startActivity(intent);
        } else if (id == R.id.nav_MyAccount) {
            // Handle the deposit action
            Intent intentRegister = new Intent(this, MyAccount.class);
            startActivityForResult(intentRegister, 1);
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

    public boolean checkLoadFinished() {
        user = userSQLHelper.getUser(user_id);

        if (user.getUser_id() == 0)
            return false;
        else
            return true;
    }

    protected void replaceContentLayout(int sourceId) {

        View contentLayout = findViewById(R.id.includeContent);

        //contentLayout.set
        //contentLayout.
        ViewGroup parent = (ViewGroup) contentLayout.getParent();
        int index = parent.indexOfChild(contentLayout);

        parent.removeView(contentLayout);
        contentLayout = getLayoutInflater().inflate(sourceId, parent, false);
        parent.addView(contentLayout, index);
    }

}
