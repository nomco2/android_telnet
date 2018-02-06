package database.database;

import android.provider.BaseColumns;

// DataBase Table
public final class DataBases {
	
	public static final class CreateDB implements BaseColumns {
		public static final String NAME = "name";
		public static final String CONTACT = "contact";
		public static final String EMAIL = "email";
		public static final String _TABLENAME = "address";
		public static final String _CREATE =
			"create table "+_TABLENAME+"(" 
					+_ID+" integer primary key autoincrement, " 	
					+NAME+" text not null , " 
					+CONTACT+" text not null , " 
					+EMAIL+" text not null );";
	}

	public static final class Project_list implements BaseColumns {
		public static final String PROJECT_NAME = "name";
		public static final String DATE_TIME = "contact";
		public static final String MAKER_NAME = "email";
		public static final String _TABLENAME = "address";
		public static final String _CREATE =
				"create table "+_TABLENAME+"("
						+_ID+" integer primary key autoincrement, "
						+PROJECT_NAME+" text not null , "
						+DATE_TIME+" text not null , "
						+MAKER_NAME+" text not null );";
	}

}
