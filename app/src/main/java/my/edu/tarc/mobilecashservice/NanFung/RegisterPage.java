package my.edu.tarc.mobilecashservice.NanFung;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import javax.xml.validation.Validator;

import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.R;

public class RegisterPage extends AppCompatActivity {
    private Validator nonempty_validate;
    private EditText editTextUserID, editTextUserName, editTextIC, editTextEmail, editTextPhone, editTextPass, editTextConPass;
    UserSQLHelper databaseSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        databaseSource = new UserSQLHelper(this);
        //createDummy();
    }

    public void BtnRegister(View v) {
        editTextUserName = findViewById(R.id.tffullname);
        editTextIC = findViewById(R.id.tfIc);
        editTextEmail = findViewById(R.id.tfEmail);
        editTextPhone = findViewById(R.id.tfPhone);
        editTextPass = findViewById(R.id.tfPass);
        editTextConPass = findViewById(R.id.tfConfirmPass);

        String name, ic, email, pass, conpass;
        String phone;

        conpass = editTextConPass.getText().toString();
        if (conpass.isEmpty()) {
            editTextConPass.setError(getString(R.string.error_conpass));
            return;
        }
        pass = editTextPass.getText().toString();
        if (pass.isEmpty()) {
            editTextPass.setError(getString(R.string.error_pass));
            return;
        }
        ic = editTextIC.getText().toString();
        if (ic.isEmpty()) {
            editTextIC.setError(getString(R.string.error_ic));
            return;
        }
        phone = editTextPhone.getText().toString();
        if (phone == null) {
            editTextPhone.setError(getString(R.string.error_phone));
            return;
        }
        name = editTextUserName.getText().toString();
        if (name.isEmpty()) {
            editTextUserName.setError(getString(R.string.error_name));
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

        //confirm password field must equal to password field
        if (!conpass.equals(pass)) {
            Toast.makeText(RegisterPage.this, "Password not match !", Toast.LENGTH_SHORT).show();

        } else {

            int user_id = 100001;
            UserRecord userRecord = new UserRecord();

            UserRecord temp = databaseSource.getLastRecord();
            if (temp.getUser_id() != 0) {
                user_id = temp.getUser_id() + 1;
            }

            boolean searchResult = databaseSource.searchUsername(name);
            searchResult = true;
            if (searchResult) {
                userRecord.setUser_id(user_id);
                userRecord.setUser_name(name);
                userRecord.setPassword(pass);
                userRecord.setIc_number(ic);
                userRecord.setEmail(email);
                userRecord.setPhone(phone);
                userRecord.setWallet_balance(0.0);

                databaseSource.insertUser(userRecord);
                Toast.makeText(RegisterPage.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                this.finish(); //Terminate this Activity

            } else {
                Toast.makeText(RegisterPage.this, "Username already exist", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
