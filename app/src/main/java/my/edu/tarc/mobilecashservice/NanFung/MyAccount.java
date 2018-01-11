package my.edu.tarc.mobilecashservice.NanFung;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;

public class MyAccount extends HomePage {
    int user_id = 0;
    UserRecord userrecord = new UserRecord();
    CheckBox chkNewPass, chkConfirmPass;
    private EditText editTextUserID, editTextUserName, editTextIC, editTextEmail, editTextPhone, editTextPass, editTextConPass;
    UserSQLHelper userSQLHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_account);
        super.replaceContentLayout(R.layout.activity_my_account);
        setTitle(getResources().getString(R.string.title_myaccount));

        editTextUserID = findViewById(R.id.tfAccUserID);
        editTextUserName = findViewById(R.id.tfAccUsername);
        editTextIC = findViewById(R.id.tfAccIC);
        editTextEmail = findViewById(R.id.tfAccEmail);
        editTextPhone = findViewById(R.id.tfAccPhoneNum);
        editTextPass = findViewById(R.id.tfAccNewPass);
        editTextConPass = findViewById(R.id.tfAccNewConfirmPass);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);

        userSQLHelper = new UserSQLHelper(this);


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
                updateFields();
            }

        }.start();

        chkNewPass = (CheckBox) findViewById(R.id.checkBoxNewPass);
        chkConfirmPass = (CheckBox) findViewById(R.id.checkBoxNewConPass);

        chkNewPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    editTextPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        chkConfirmPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextConPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    editTextConPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    public void BtnUpdateAcc(View v){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = sharedPref.getInt("user_id", 0);

        String userid, phone,email,newpass,newconfirmpass;
        userid = editTextUserID.getText().toString();

        phone = editTextPhone.getText().toString();
        if (phone.isEmpty()) {
            editTextPhone.setError("Please enter phone number");
            return;
        }
        email = editTextEmail.getText().toString();
        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.error_email));
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            return;
        }
        newpass = editTextPass.getText().toString();
        if (newpass.isEmpty()) {
            editTextPass.setError("Please enter new password");
            return;
        }
        newconfirmpass = editTextConPass.getText().toString();
        if (newconfirmpass.isEmpty()) {
            editTextConPass.setError("Please enter new confirm password");
            return;
        }
        if (!newconfirmpass.equals(newpass)) {
            Toast toastpass = Toast.makeText(MyAccount.this, "New Password not match !", Toast.LENGTH_SHORT);
            toastpass.show();
        }else {
            userrecord.setPassword(newpass);
            userrecord.setEmail(email);
            userrecord.setPhone(phone);
            //databaseSource.changeNewPass(newpass, userid);
            userSQLHelper.insertUser(userrecord);
            Toast.makeText(MyAccount.this, "Update Successfully !", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateFields(){
        userrecord = userSQLHelper.getUser(user_id);

        editTextUserID.setText(String.valueOf(user_id));
        editTextUserName.setText(userrecord.getUser_name());
        editTextIC.setText(userrecord.getIc_number());
        editTextPhone.setText(userrecord.getPhone());
        editTextEmail.setText(userrecord.getEmail());
        editTextPass.setText(userrecord.getPassword());
        editTextConPass.setText(userrecord.getPassword());
    }
}
