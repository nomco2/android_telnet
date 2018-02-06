package algorithim_dev_classis;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimfamily.arduino_car_nodemcu.R;

import java.util.ArrayList;

import database.data.InfoClass;
import database.database.DbOpenHelper;
import database.util.DLog;

/**
 * Created by KimFamily on 2018-02-05.
 */

public class Button_allocate extends Activity{

    int project_list_num;
    private Cursor mCursor;
    private DbOpenHelper mDbOpenHelper;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;

    private TextView project_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_allocate);
        Intent intent = getIntent();

        project_list_num = intent.getExtras().getInt("project_list_num");
        Toast.makeText(this, project_list_num +"", Toast.LENGTH_LONG).show();

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        DLog.e("load list", "COUNT = " + mCursor.getCount());

        project_title = (TextView) findViewById(R.id.project_title);

        int while_count=0;
        while (mCursor.moveToNext()) {

//            mInfoClass = new InfoClass(
//                    mCursor.getInt(mCursor.getColumnIndex("_id")),
//                    mCursor.getString(mCursor.getColumnIndex("name")),
//                    mCursor.getString(mCursor.getColumnIndex("contact")),
//                    mCursor.getString(mCursor.getColumnIndex("email"))
//            );
//
//            mInfoArray.add(mInfoClass);

            while_count++;
            if(while_count == project_list_num){
                project_title.setText(mCursor.getString(mCursor.getColumnIndex("name")));
            }
        }

        mCursor.close();


    }

}
