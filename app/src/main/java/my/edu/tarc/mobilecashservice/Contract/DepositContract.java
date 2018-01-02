package my.edu.tarc.mobilecashservice.Contract;

import android.provider.BaseColumns;

/**
 * Created by jiaweiloo on 29/12/2017.
 */

public final class DepositContract {
    public DepositContract() {    }
    public static abstract class Deposits implements BaseColumns {
        public static final String TABLE_NAME ="Deposit";
        public static final String COLUMN_DEPOSIT_ID = "deposit_id";
        public static final String COLUMN_USER_ID ="user_id";
        public static final String COLUMN_AMOUNT ="amount";
        public static final String COLUMN_WITHDRAWAL_ID ="withdrawal_id";
        public static final String COLUMN_LOCATION_ID ="location_id";
        public static final String COLUMN_STATUS ="status";

    }
}
