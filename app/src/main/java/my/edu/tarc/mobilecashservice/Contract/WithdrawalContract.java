package my.edu.tarc.mobilecashservice.Contract;

import android.provider.BaseColumns;

/**
 * Created by Loi Kah Hou on 12/30/2017.
 */

public class WithdrawalContract {
    public WithdrawalContract(){}

    public static abstract class WithdrawalRecord implements BaseColumns {
        public static final String TABLE_NAME ="WithdrawalRecordHistory";
        public static final String COLUMN_WITHDRAWAL_ID = "withdrawal_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_AMOUNT ="amount";
        public static final String COLUMN_DEPOSIT_ID ="deposit_id";
        public static final String COLUMN_LOCATION_X = "location_x";
        public static final String COLUMN_LOCATION_Y = "location_y";
        public static final String COLUMN_STATUS = "status";
    }
}
