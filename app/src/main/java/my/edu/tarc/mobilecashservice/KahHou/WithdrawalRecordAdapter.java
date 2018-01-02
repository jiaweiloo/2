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

       Withdrawal userRecord = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.withdrawal_record,
                            parent,
                            false);
        }

        TextView textViewDateTime, textViewAmount, textViewLocation,textViewStatus;

        textViewDateTime = (TextView) convertView.findViewById(R.id.textViewDateTime);
        textViewAmount = (TextView) convertView.findViewById(R.id.textViewAmount);
        textViewLocation = (TextView) convertView.findViewById(R.id.textViewLocation);
        textViewStatus = (TextView) convertView.findViewById(R.id.textViewStatus);
        textViewDateTime.setText(textViewDateTime.getText() + " : " + userRecord.getDateTime());
        textViewAmount.setText(textViewAmount.getText() + " : " + userRecord.getAmount());
        textViewLocation.setText(textViewLocation.getText() + "X : " + userRecord.getLocation_x()+", Y : "+userRecord.getLocation_y());
        textViewStatus.setText(textViewStatus.getText() + " : " + userRecord.getStatus());


        return convertView;

    }
}
