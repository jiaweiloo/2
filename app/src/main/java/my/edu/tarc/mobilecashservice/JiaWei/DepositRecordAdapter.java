package my.edu.tarc.mobilecashservice.JiaWei;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import my.edu.tarc.mobilecashservice.Entity.Deposit;
import my.edu.tarc.mobilecashservice.R;

/**
 * Created by jiaweiloo on 29/12/2017.
 */


public class DepositRecordAdapter extends ArrayAdapter<Deposit> {

    public DepositRecordAdapter(Activity context, int resource, List<Deposit>
            list) {
        super(context, resource, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Deposit depositRecord = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.deposit_record,
                            parent,
                            false);
        }
        if(convertView == null) {
            Log.i("[error]", "null");
        }
        else{
            Log.i("[error]", "not null");
        }
        TextView textViewDeposit_id, textViewUser_id, textViewAmount, textViewWithdrawal_id, textViewLocation_id, textViewStatus;
        textViewDeposit_id = (TextView)convertView.findViewById(R.id.textViewDeposit_id);
        textViewUser_id = (TextView)convertView.findViewById(R.id.textViewUser_id);
        textViewAmount = (TextView)convertView.findViewById(R.id.textViewAmount);
        textViewWithdrawal_id = (TextView)convertView.findViewById(R.id.textViewWithdrawal_id);
        textViewLocation_id = (TextView)convertView.findViewById(R.id.textViewLocation_id);
        textViewStatus = (TextView)convertView.findViewById(R.id.textViewStatus);

        textViewDeposit_id.setText("Deposit id:"+depositRecord.getDeposit_id());
        textViewUser_id.setText("User id:"+depositRecord.getUser_id());
        textViewAmount.setText("Amount:" + depositRecord.getAmount());
        textViewWithdrawal_id.setText("Withdrawal id:"+depositRecord.getWithdrawal_id());
        textViewLocation_id.setText("Location id:"+depositRecord.getLocation_id());
        textViewStatus.setText("Status:" + depositRecord.getStatus());

        return convertView;
    }
}