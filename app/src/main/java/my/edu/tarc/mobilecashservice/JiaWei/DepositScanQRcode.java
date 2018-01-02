package my.edu.tarc.mobilecashservice.JiaWei;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import my.edu.tarc.mobilecashservice.DatabaseHelper.DepositSQLHelper;
import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.HomePage;
import my.edu.tarc.mobilecashservice.R;
import my.edu.tarc.mobilecashservice.barcode.BarcodeCaptureActivity;

public class DepositScanQRcode extends AppCompatActivity {
private static final int BARCODE_READER_REQUEST_CODE = 1;

    private TextView mResultTextView;
    Deposit deposit = new Deposit();
    DepositSQLHelper depositDataSource;
    TextView tviewDepositID;
    Button scanBarcodeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_scan_qrcode);

        depositDataSource = new DepositSQLHelper(this);
        tviewDepositID = findViewById(R.id.tviewDepositID);

        Bundle bundle = getIntent().getExtras();
        int depositid = 0;
        if (getIntent().getIntExtra("deposit_id", 0) != 0) {
            depositid = bundle.getInt("deposit_id");
        }
        deposit = depositDataSource.getDeposit(depositid);
        if (deposit.getDeposit_id() != 0) {
            tviewDepositID.setText("Deposit id : " + deposit.getDeposit_id());
        }
        mResultTextView = findViewById(R.id.result_textview);

        scanBarcodeButton = findViewById(R.id.scan_barcode_button);

    }

    public void btnScanQR(View view) {
        if(scanBarcodeButton.getText().equals("Back to main menu")){
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivityForResult(intent, 1);
        }else {
            Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
            Log.i("[tag]", "before starting");
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            Log.i("[tag]", "after starting");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Point[] p = barcode.cornerPoints;
                    mResultTextView.setText(barcode.displayValue);
                    //pair with withdrawal id
                    pair(Integer.parseInt(barcode.displayValue));
                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e("tag", String.format("R.string.barcode_error_format",
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    public void pair(int withdrawalID) {
        if (deposit.getWithdrawal_id() == withdrawalID) {
            deposit.setStatus("complete");
            depositDataSource.updateDeposit(deposit);
            mResultTextView.setText("Scan code complete txn withdrawal id : "+ withdrawalID);
            scanBarcodeButton.setText("Back to main menu");
        } else {
            mResultTextView.setText("QR code not matched, please try again! ");
            Toast.makeText(DepositScanQRcode.this,
                    "Withdrawal id :" + withdrawalID, Toast.LENGTH_SHORT).show();
        }
    }
}
