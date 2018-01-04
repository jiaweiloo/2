package my.edu.tarc.mobilecashservice.Contract;

import android.provider.BaseColumns;

/**
 * Created by jiaweiloo on 4/1/2018.
 */

public class LocationContract {
    public LocationContract() {    }
    public static abstract class Location implements BaseColumns {
        public static final String TABLE_NAME ="Location";
        public static final String COLUMN_LOCATION_ID = "location_id";
        public static final String COLUMN_LOCATION_NAME = "location_name";
        public static final String COLUMN_LOCATION_X ="location_x";
        public static final String COLUMN_LOCATION_Y ="location_y";
        public static final String COLUMN_STATUS ="status";
    }
}
