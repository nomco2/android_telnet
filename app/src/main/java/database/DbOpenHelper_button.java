package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DbOpenHelper_button {

	public String DATABASE_NAME = "wifi_coding_data";
	private static final int DATABASE_VERSION = 1;
	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;


	public String project_name;
	private String btn_name = "btn_name";
	private String x_location = "x";
	private String y_location = "y";
	private String coding = "coding";



	public DbOpenHelper_button(Context context, String project_name){
		this.mCtx = context;
		this.project_name = project_name;
	}

	public void create_new_button(String btn_name, float x_location, float y_location, String coding_contents){

	}



	private class DatabaseHelper extends SQLiteOpenHelper{





		// 생성자
		public DatabaseHelper(Context context, String name,
							  CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// 최초 DB를 만들때 한번만 호출된다.
		@Override
		public void onCreate(SQLiteDatabase db) {
//			db.execSQL(CreateDB._CREATE);
			db.execSQL("create table "+project_name+"("
					+BaseColumns._ID+" integer primary key autoincrement, "
					+btn_name+" text not null , "
					+x_location+" int not null , "
					+y_location+" int not null ,"
					+coding+" text);");

		}

		// 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+CreateDB._TABLENAME);
			onCreate(db);
		}



	}



	public DbOpenHelper_button open() throws SQLException{
		mDBHelper = new DatabaseHelper(mCtx, project_name, null, DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close(){
		mDB.close();
	}

	// Insert DB
	public long insertColumn(String name, String contact, String email){
		ContentValues values = new ContentValues();
		values.put(CreateDB.NAME, name);
		values.put(CreateDB.CONTACT, contact);
		values.put(CreateDB.EMAIL, email);
		return mDB.insert(CreateDB._TABLENAME, null, values);
	}


	// 새로운 데이터 넣기
	public long insertColumn_button_data(String btn_name, int x, int y, String coding){
		ContentValues values = new ContentValues();
		values.put(this.btn_name, btn_name);
		values.put(this.x_location, x);
		values.put(this.y_location, y);
		values.put(this.coding, coding);

		return mDB.insert(project_name, null, values);
	}


	// Update DB
	public boolean updateColumn(long id , String name, String contact, String email){
		ContentValues values = new ContentValues();
		values.put(CreateDB.NAME, name);
		values.put(CreateDB.CONTACT, contact);
		values.put(CreateDB.EMAIL, email);
		return mDB.update(CreateDB._TABLENAME, values, "_id="+id, null) > 0;
	}

	// Update DB _업데이트 : id 번호를 알아야 됨 -> 이름으로 번호 찾기 돌려야
	public boolean updateColumn_btn_data(long id , String btn_name, int x, int y){
		ContentValues values = new ContentValues();
		if(btn_name != "") {
			values.put(this.btn_name, btn_name);
		}
		if(x != 0) {
			values.put(this.x_location, x);
		}
		if(y != 0) {
			values.put(this.y_location, y);
		}

		return mDB.update(project_name, values, "_id="+id, null) > 0;
	}



	// Delete ID
	public boolean deleteColumn(long id){
		return mDB.delete(CreateDB._TABLENAME, "_id="+id, null) > 0;
	}

	// Delete ID _btn_data  : id 번호를 알아야 됨 -> 이름으로 번호 찾기 돌려야
	public boolean deleteColumn_btn_data(long id){
		return mDB.delete(project_name, "_id="+id, null) > 0;
	}


	// Delete Contact
	public boolean deleteColumn(String number){
		return mDB.delete(CreateDB._TABLENAME, "contact="+number, null) > 0;
	}

	// Select All
	public Cursor getAllColumns(){
		return mDB.query(CreateDB._TABLENAME, null, null, null, null, null, null);
	}

	// Select All btn data
	public Cursor getAllColumns_btn_data(){
		return mDB.query(project_name, null, null, null, null, null, null);
	}

	// ID 컬럼 얻어 오기
	public Cursor getColumn(long id){
		Cursor c = mDB.query(CreateDB._TABLENAME, null,
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

	// 이름으로 검색 하기 (rawQuery) _btn_data 있는지 없는지
	public Cursor getMatchName_btn_data(String name){
		Cursor c = mDB.rawQuery( "select * from " + project_name + " where " + btn_name + " =" + "'" + name + "'" , null);
		return c;
	}

	// 이름으로 검색 하기 (rawQuery) _ id 번호 주기
	public int get_id_by_Name_btn_data(String name){

		Cursor c = mDB.rawQuery( "select * from " + project_name + " where " + btn_name + " =" + "'" + name + "'" , null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex("_id"));
	}

	public void duplicate_btn_data(String name){
		Cursor c = mDB.rawQuery( "select * from " + project_name + " where " + btn_name + " =" + "'" + name + "'" , null);
		c.moveToFirst();
		insertColumn_button_data(c.getString(c.getColumnIndex(btn_name))+ "_복사",c.getInt(c.getColumnIndex(x_location)), c.getInt(c.getColumnIndex(y_location)),c.getString(c.getColumnIndex(coding)));

	}


	//이름 찾으면 True 없으면 False
	public boolean is_same_btn_existed(String btn_name){
		try{
			Cursor cursor = getMatchName_btn_data(btn_name);
			cursor.moveToFirst();
			cursor.getInt(cursor.getColumnIndex("_id"));
			return true;

		}catch (Exception e){
			return false;
		}
	}









	public class CreateDB implements BaseColumns {
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





}






