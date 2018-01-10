package my.edu.tarc.mobilecashservice.KahHou;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import my.edu.tarc.mobilecashservice.Entity.Withdrawal;
import my.edu.tarc.mobilecashservice.R;

/**
 * Created by Loi Kah Hou on 12/30/2017.
 */

public class WithdrawalRecordAdapter extends ArrayAdapter<Withdrawal> {
    public WithdrawalRecordAdapter(Activity context, int resource, List<Withdrawal> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Withdrawal withdrawalRecord = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.withdrawal_record,
                            parent,
                            false);
        }

        TextView textViewDateTime, textViewAmount, textViewLocation, textViewStatus, textViewWithID;

        textViewWithID = convertView.findViewById(R.id.textViewWithID);
        textViewDateTime = convertView.findViewById(R.id.textViewDateTime);
        textViewAmount = convertView.findViewById(R.id.textViewAmount);
        textViewLocation = convertView.findViewById(R.id.textViewLocation);
        textViewStatus = convertView.findViewById(R.id.textViewStatus);

        textViewWithID.setText("Withdrawal ID: " + withdrawalRecord.getWithdrawal_id());
        textViewDateTime.setText("Date Time : " + withdrawalRecord.getDateTime());
        textViewAmount.setText("Amount : " + withdrawalRecord.getAmount());
        textViewLocation.setText("Location ID : " + withdrawalRecord.getLocation_id());
        textViewStatus.setText("Status : " + withdrawalRecord.getStatus());


        return convertView;

    }
}
