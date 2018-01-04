package my.edu.tarc.mobilecashservice.JiaWei;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import my.edu.tarc.mobilecashservice.Entity.Location;
import my.edu.tarc.mobilecashservice.R;

/**
 * Created by jiaweiloo on 4/1/2018.
 */

public class LocationAdapter extends ArrayAdapter<Location> {

    public LocationAdapter(Activity context, int resource, List<Location>
            list) {
        super(context, resource, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Location locationRecord = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.location_record,
                            parent,
                            false);
        }

        TextView textViewLocate_id, textViewLocate_Name, textViewLocate_x, textViewLocate_y, textViewStatus;
        textViewLocate_id = (TextView)convertView.findViewById(R.id.textViewLocate_id);
        textViewLocate_Name = (TextView)convertView.findViewById(R.id.textViewLocate_Name);
        textViewLocate_x = (TextView)convertView.findViewById(R.id.textViewLocate_x);
        textViewLocate_y = (TextView)convertView.findViewById(R.id.textViewLocate_y);
        textViewStatus = (TextView)convertView.findViewById(R.id.textViewStatus);

        textViewLocate_id.setText("Location id:"+locationRecord.getLocation_id());
        textViewLocate_Name.setText("Location Name:"+locationRecord.getLocation_name());
        textViewLocate_x.setText("Coordinate X:" + locationRecord.getLocation_x());
        textViewLocate_y.setText("Coordinate Y:"+locationRecord.getLocation_y());
        textViewStatus.setText("Status:" + locationRecord.getStatus());

        return convertView;
    }
}
