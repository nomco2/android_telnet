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
		public static final String project_name = "project_name";
		public static final String button_name = "button_name";
		public static final String date = "date";
		public static final String x_location = "x_location";
		public static final String y_location = "y_location";
		public static final String coding_contents = "coding_contents";




		public static final String _CREATE =
				"create table "+project_name+"("
						+_ID+" integer primary key autoincrement, "
						+button_name+" text not null , "
						+date+" text not null , "
						+x_location+" integer not null , "
						+y_location+" integer not null , "
						+coding_contents+" text );"
				;
	}

}
