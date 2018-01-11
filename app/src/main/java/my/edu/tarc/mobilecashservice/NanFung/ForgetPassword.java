package my.edu.tarc.mobilecashservice.NanFung;

import android.content.Intent;
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
import my.edu.tarc.mobilecashservice.R;

public class ForgetPassword extends AppCompatActivity {

    private EditText editTextUsername, editTextIC, editTextPhone, editTextNewPass, editTextNewConfirmPass;
    UserSQLHelper databaseSource;
    CheckBox chkNewPass, chkConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setTitle(getResources().getString(R.string.title_forgetpassword));

        databaseSource = new UserSQLHelper(this);

        editTextNewPass = findViewById(R.id.tfNewPass);
        editTextNewConfirmPass = findViewById(R.id.tfNewConfirmPass);
        chkNewPass = (CheckBox) findViewById(R.id.checkBoxNewPass);
        chkConfirmPass = (CheckBox) findViewById(R.id.checkBoxNewConPass);

        chkNewPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    editTextNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        chkConfirmPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextNewConfirmPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    editTextNewConfirmPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    public void BtnForget(View v){
        editTextUsername = findViewById(R.id.tfUsername);
        editTextIC = findViewById(R.id.tfIC);
        editTextPhone = findViewById(R.id.tfPhoneNum);
        editTextNewPass = findViewById(R.id.tfNewPass);
        editTextNewConfirmPass = findViewById(R.id.tfNewConfirmPass);

        String username, ic , newpass, newconfirmpass,phone;
        ic = editTextIC.getText().toString();
        if (ic.isEmpty()) {
            editTextIC.setError(getString(R.string.error_ic));
            return;
        }
        phone = editTextPhone.getText().toString();
        if (phone.isEmpty()) {
            editTextPhone.setError(getString(R.string.error_phone));
            return;
        }
        username = editTextUsername.getText().toString();
        if (username.isEmpty()) {
            editTextUsername.setError(getString(R.string.error_name));
            return;
        }
        newpass = editTextNewPass.getText().toString();
        if (newpass.isEmpty()) {
            editTextNewPass.setError("Please enter new password");
            return;
        }
        newconfirmpass = editTextNewConfirmPass.getText().toString();
        if (newconfirmpass.isEmpty()) {
            editTextNewConfirmPass.setError("Please enter new confirm password");
            return;
        }
        if (!newconfirmpass.equals(newpass)) {
            Toast toastpass = Toast.makeText(ForgetPassword.this, "New Password not match !", Toast.LENGTH_SHORT);
            toastpass.show();
        }else {
            UserRecord usr = databaseSource.searchUsername(username);

            if(usr.getUser_id() != 0) {
                if (!usr.getIc_number().equals(ic)) {
                    Toast.makeText(ForgetPassword.this, "Incorrect IC number", Toast.LENGTH_SHORT).show();
                } else if (!usr.getPhone().equals(phone)) {
                    Toast.makeText(ForgetPassword.this, "Incorrect Phone Number", Toast.LENGTH_SHORT).show();
                }  else {
                    usr.setPassword(newpass);
                    databaseSource.updateUser(usr);
                    Toast.makeText(ForgetPassword.this, "Password has been changed !", Toast.LENGTH_SHORT).show();
                    Intent intentRegister = new Intent(this, LoginPage.class);
                    startActivityForResult(intentRegister, 1);
                }
            }else{
                Toast.makeText(ForgetPassword.this, "User not found", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
