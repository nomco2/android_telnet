package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DbOpenHelper_algorithm {

    public String DATABASE_TYPE = "coding_";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;


    public String project_name;
    private String function_type = "function_type"; //기능의 종류 : 디지털 신호, 아날로그 신호, if, for 등
    private String function_data= "function_data"; //비교 대상의 종류 : 센서 읽은 데이터(1) 사용자 지정 카운터(2) 통신으로 받은 것(3) 다른 버튼에서 누름 감지(4)
    private String algorithm_continuous = "algorithm_continuous";
    private String layout_id = "layout_id";

    public DbOpenHelper_algorithm(Context context, String project_name) {
        this.mCtx = context;
        this.project_name = project_name;
    }


    private class DatabaseHelper extends SQLiteOpenHelper {


        // 생성자
        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
//			db.execSQL(CreateDB._CREATE);
            db.execSQL("create table " + project_name + "("
                    + BaseColumns._ID + " integer primary key autoincrement, "
                    + function_type + " int not null , "
                    + function_data + " String not null, "
                    + algorithm_continuous + "int not null, "
                    + layout_id + "int not null);");

        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CreateDB._TABLENAME);
            onCreate(db);
        }


    }


    public DbOpenHelper_algorithm open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, project_name, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }



    // 새로운 데이터 넣기
    public long insertColumn_layout_id(int function_type, String function_data, int algorithm_continuous, int layout_id) {
        ContentValues values = new ContentValues();
        values.put(this.function_type, function_type);
        values.put(this.function_data, function_data);
        values.put(this.algorithm_continuous, algorithm_continuous);
        values.put(this.layout_id, layout_id);

        return mDB.insert(project_name, null, values);
    }


    // Update DB
    public boolean updateColumn(long id, String name, String contact, String email) {
        ContentValues values = new ContentValues();
        values.put(CreateDB.NAME, name);
        values.put(CreateDB.CONTACT, contact);
        values.put(CreateDB.EMAIL, email);
        return mDB.update(CreateDB._TABLENAME, values, "_id=" + id, null) > 0;
    }

    // Update DB for algorithm_continuous by layout id 여기서 algorithm_continuous 만 업데이트 나머지는 그대로
    public boolean updateColumn_by_layout_id(int layout_id, int algorithm_continuous) {
        Cursor c = mDB.rawQuery( "select * from address where "+ layout_id +"=" + "'" + layout_id + "'" , null);
        c.moveToFirst();
        int id = c.getInt(c.getColumnIndex("_id"));

        //algorithm_continuous만 업데이트
        ContentValues values = new ContentValues();
        values.put(this.function_type, c.getInt(c.getColumnIndex(this.function_type)));
        values.put(this.function_data, c.getString(c.getColumnIndex(this.function_data)));
        values.put(this.algorithm_continuous, algorithm_continuous);
        values.put(this.layout_id, c.getInt(c.getColumnIndex(this.layout_id)));


        return mDB.update(project_name, values, "_id=" + id, null) > 0;
    }



    // Delete ID
    public boolean deleteColumn(long id) {
        return mDB.delete(CreateDB._TABLENAME, "_id=" + id, null) > 0;
    }

    // Delete ID _btn_data  : id 번호를 알아야 됨 -> 이름으로 번호 찾기 돌려야
    public boolean deleteColumn_btn_data(long id) {
        return mDB.delete(project_name, "_id=" + id, null) > 0;
    }


    // Delete Contact
    public boolean deleteColumn(String number) {
        return mDB.delete(CreateDB._TABLENAME, "contact=" + number, null) > 0;
    }

    // Select All
    public Cursor getAllColumns() {
        return mDB.query(CreateDB._TABLENAME, null, null, null, null, null, null);
    }

    // Select All btn data
    public Cursor getAllColumns_btn_data() {
        return mDB.query(project_name, null, null, null, null, null, null);
    }

    // ID 컬럼 얻어 오기
    public Cursor getColumn(long id) {
        Cursor c = mDB.query(CreateDB._TABLENAME, null,
                "_id=" + id, null, null, null, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    // 이름 검색 하기 (rawQuery)
    public Cursor getMatchName(String name) {
        Cursor c = mDB.rawQuery("select * from address where name=" + "'" + name + "'", null);
        return c;
    }



////     이름으로 검색 하기 (rawQuery) _ id 번호 주기
//    public int get_id_by_Name_btn_data(String name) {
//
//        Cursor c = mDB.rawQuery("select * from " + project_name + " where " + btn_name + " =" + "'" + name + "'", null);
//        c.moveToFirst();
//        return c.getInt(c.getColumnIndex("_id"));
//    }
//
//    public void duplicate_btn_data(String name) {
//        Cursor c = mDB.rawQuery("select * from " + project_name + " where " + btn_name + " =" + "'" + name + "'", null);
//        c.moveToFirst();
//        insertColumn_button_data(c.getString(c.getColumnIndex(btn_name)) + "_복사", c.getInt(c.getColumnIndex(x_location)), c.getInt(c.getColumnIndex(y_location)), c.getString(c.getColumnIndex(coding)));
//
//    }


//     layout_id 로 검색하기
    public Cursor getMatchName_laytout_id(int layout_id) {
        Cursor c = mDB.rawQuery("select * from " + project_name + " where " + this.layout_id + " =" + "'" + layout_id + "'", null);
        return c;
    }



//    layout_id가 db에 추가되어 있는지 찾아줌 찾으면 _id번호 없으면 0
    public int is_same_layout_id_existed(int layout_id) {
        try {
            Cursor cursor = getMatchName_laytout_id(layout_id);
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("_id"));

        } catch (Exception e) {
            return 0;
        }
    }


    public class CreateDB implements BaseColumns {
        public static final String NAME = "name";
        public static final String CONTACT = "contact";
        public static final String EMAIL = "email";
        public static final String _TABLENAME = "address";
        public static final String _CREATE =
                "create table " + _TABLENAME + "("
                        + _ID + " integer primary key autoincrement, "
                        + NAME + " text not null , "
                        + CONTACT + " text not null , "
                        + EMAIL + " text not null );";


    }


}






