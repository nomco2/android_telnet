package database.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Project_button_list_DB {

	private static String DATABASE_NAME = "";
	private static final int DATABASE_VERSION = 1;
	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;



	private class DatabaseHelper extends SQLiteOpenHelper {

		// 생성자
		public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// 최초 DB를 만들때 한번만 호출된다.
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DataBases.Project_list._CREATE);

		}

		// 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+DataBases.Project_list.project_name);
			onCreate(db);
		}
	}

	public Project_button_list_DB(Context context, String Project_name){
		this.mCtx = context;
		DATABASE_NAME = Project_name;
	}

	public Project_button_list_DB open() throws SQLException {
		mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close(){
		mDB.close();
	}



	// 프로젝트 db에 넣기
	public long create_project_db_insert(String project_name, String button_name, String date, String x_location, String y_location, String coding_contents){
		ContentValues values = new ContentValues();
//		values.put(DataBases.Project_list.project_name, project_name);
		values.put(DataBases.Project_list.project_name, project_name);
		values.put(DataBases.Project_list.button_name, button_name);
		values.put(DataBases.Project_list.date, date);
		values.put(DataBases.Project_list.x_location, x_location);
		values.put(DataBases.Project_list.y_location, y_location);
		values.put(DataBases.Project_list.coding_contents, coding_contents);

//		return mDB.insert(DataBases.Project_list.project_name, null, values);
		return mDB.insert("project_name", null, values);

	}


	// Update DB
	public boolean updateColumn(long id , String project_name, String button_name, String date, int x_location, int y_location, String coding_contents){
		ContentValues values = new ContentValues();
		values.put(DataBases.Project_list.project_name, project_name);
		values.put(DataBases.Project_list.button_name, button_name);
		values.put(DataBases.Project_list.date, date);
		values.put(DataBases.Project_list.x_location, x_location);
		values.put(DataBases.Project_list.y_location, y_location);
		values.put(DataBases.Project_list.coding_contents, coding_contents);
		return mDB.update(DataBases.Project_list.project_name, values, "_id="+id, null) > 0;
	}

	// Delete ID
	public boolean deleteColumn(long id){
		return mDB.delete(DataBases.Project_list.project_name, "_id="+id, null) > 0;
	}
	
	// Delete Contact
	public boolean deleteColumn(String number){
		return mDB.delete(DataBases.Project_list.project_name, "contact="+number, null) > 0;
	}
	
	// Select All
	public Cursor getAllColumns(){
		return mDB.query(DataBases.Project_list.project_name, null, null, null, null, null, null);
	}

	// ID 컬럼 얻어 오기
	public Cursor getColumn(long id){
		Cursor c = mDB.query(DataBases.Project_list.project_name, null,
				"_id="+id, null, null, null, null);
		if(c != null && c.getCount() != 0)
			c.moveToFirst();
		return c;
	}

	// 이름 검색 하기 (rawQuery)
	public Cursor getMatchName(String name){
		Cursor c = mDB.rawQuery( "select * from address where name=" + "'" + name + "'" , null);
		return c;
	}


}






