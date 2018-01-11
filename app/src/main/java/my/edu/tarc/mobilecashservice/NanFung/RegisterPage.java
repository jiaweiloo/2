package my.edu.tarc.mobilecashservice.NanFung;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.xml.validation.Validator;

import my.edu.tarc.mobilecashservice.DatabaseHelper.UserSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.UserRecord;
import my.edu.tarc.mobilecashservice.R;

public class RegisterPage extends AppCompatActivity {
    private Validator nonempty_validate;
    private EditText editTextUserID, editTextUserName, editTextIC, editTextEmail, editTextPhone, editTextPass, editTextConPass;
    UserSQLHelper databaseSource;
    List<UserRecord> records;
    CheckBox chkNewPass, chkConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        setTitle(getResources().getString(R.string.title_register));
        databaseSource = new UserSQLHelper(this);
        records = new ArrayList<>();
        //createDummy();

        editTextPass = findViewById(R.id.tfPass);
        editTextConPass = findViewById(R.id.tfConfirmPass);
        chkNewPass = findViewById(R.id.RcheckBoxNewPass);
        chkConfirmPass = findViewById(R.id.RcheckBoxNewConPass);

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

    public void BtnRegister(View v) {
        editTextUserName = findViewById(R.id.tffullname);
        editTextIC = findViewById(R.id.tfIc);
        editTextEmail = findViewById(R.id.tfEmail);
        editTextPhone = findViewById(R.id.tfPhone);
        editTextPass = findViewById(R.id.tfPass);
        editTextConPass = findViewById(R.id.tfConfirmPass);

        String name, ic, email, pass, conpass, phone;

        conpass = editTextConPass.getText().toString();
        if (conpass.isEmpty()) {
            editTextConPass.setError("Please enter confirm password");
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
        if (phone.isEmpty()) {
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

        if (!conpass.equals(pass)) {
            Toast toastpass = Toast.makeText(RegisterPage.this, "Password not match !", Toast.LENGTH_SHORT);
            toastpass.show();
        } else {
            int user_id = 100001;
            UserRecord userRecord = new UserRecord();

            UserRecord temp = databaseSource.getLastRecord();

            if (temp.getUser_id() != 0) {
                user_id = temp.getUser_id() + 1;
            }

            records = databaseSource.getAllUsers();
            boolean success = true;
            if (!records.isEmpty()) {
                //check every records to compare its data
                for (int count = 0; count < records.size(); count++) {

                    //if else statement to check username, ic, and email exist or not
                    if (records.get(count).getUser_name().equals(name)) {
                        Toast.makeText(RegisterPage.this, "Username already exist", Toast.LENGTH_SHORT).show();
                        success = false;
                        break;
                    } else if (records.get(count).getIc_number().equals(ic)) {
                        Toast.makeText(RegisterPage.this, "This IC already registered", Toast.LENGTH_SHORT).show();
                        success = false;
                        break;
                    } else if (records.get(count).getEmail().equals(email)) {
                        Toast.makeText(RegisterPage.this, "This email already exist", Toast.LENGTH_SHORT).show();
                        success = false;
                        break;
                    }//end of if-else

                } // end of for-loop
                if (success) {
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
                }
            }

        }

    }

}
