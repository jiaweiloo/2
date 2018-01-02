package my.edu.tarc.mobilecashservice.NanFung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;

public class LoginPage extends AppCompatActivity {
    UserSQLHelper database;
    UserRecord user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        database = new UserSQLHelper(this);
    }

    public void BtnLogin(View view) {
        EditText username = (EditText) findViewById(R.id.editTextUserName);
        String usernamestr = username.getText().toString();
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        String passwordstr = password.getText().toString();


        user = database.searchPass(usernamestr);
        if (user != null) {
            if (passwordstr.equals(user.getPassword())) {
                Toast temp = Toast.makeText(LoginPage.this, "Login Successfully", Toast.LENGTH_SHORT);
                temp.show();
                Intent intentLogin = new Intent(this, HomePage.class);
                intentLogin.putExtra("user_id", user.getUser_id());
                startActivityForResult(intentLogin, 1);
            } else {
                Toast.makeText(LoginPage.this, "Password not match", Toast.LENGTH_SHORT).show();
            }
        }//check if user is null
        else {
            Toast.makeText(LoginPage.this, "Username not match", Toast.LENGTH_SHORT).show();
        }
    }

    public void BtnToRegister(View view) {
        Intent intentRegister = new Intent(this, RegisterPage.class);
        startActivityForResult(intentRegister, 1);
    }
}
