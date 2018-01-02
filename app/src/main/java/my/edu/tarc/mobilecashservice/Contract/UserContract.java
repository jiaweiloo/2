package my.edu.tarc.mobilecashservice.Contract;

import android.provider.BaseColumns;

/**
 * Created by Nan Fung Lim on 31/12/2017.
 */

public class UserContract {
    public UserContract(){}
    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME ="user";
        public static final String COLUMN_USERID = "user_id";
        public static final String COLUMN_USERNAME = "user_name";
        public static final String COLUMN_USERPASSWORD = "password";
        public static final String COLUMN_IC ="ic_number";
        public static final String COLUMN_EMAIL ="email";
        public static final String COLUMN_PHONE = "phone";
    }
}
