package my.edu.tarc.mobilecashservice;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;

import my.edu.tarc.mobilecashservice.JiaWei.AllDepositRecords;
import my.edu.tarc.mobilecashservice.KahHou.CheckRequest;

public class ManagementMenu extends HomePage {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_management_menu);
        //super.replaceContentLayout(R.layout.activity_management_menu, R.layout.content_home_page);
        super.replaceContentLayout(R.layout.activity_management_menu);
        setTitle("Management");
        /*
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_management_menu, null, false);

        drawer.addView(contentView, 0);

        ConstraintLayout main = findViewById(R.layout.content_home_page);

        drawer.closeDrawer(R.layout.content_home_page);
        */
    }

    public void goDepDatabase(View view) {
        Intent intent = new Intent(this, AllDepositRecords.class);
        startActivity(intent);
        this.finish();
    }

    public void goWitDatabase(View view) {
        Intent intent = new Intent(this, CheckRequest.class);
        startActivity(intent);
        this.finish();
    }

    public void goLocDatabase(View view) {
        Intent intent = new Intent(this, LocationModule.class);
        startActivity(intent);
        this.finish();
    }
}
