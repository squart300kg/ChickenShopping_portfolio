import android.provider.BaseColumns;

public final class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String USERID = "id";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String _TABLENAME0 = "UserInfo";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +USERID+" text not null , "
                +PASSWORD+" text not null , "
                +NAME+" text not null, "
                +EMAIL+" integer not null , "
                +ADDRESS+" text not null);";
    }

}
