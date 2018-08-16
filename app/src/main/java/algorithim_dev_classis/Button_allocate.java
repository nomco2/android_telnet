package algorithim_dev_classis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimfamily.arduino_car_nodemcu.R;

import java.util.ArrayList;

import database.DbOpenHelper_button;
import database.InfoClass_btn_data;
import database.adapter.CustomAdapter;
import database.data.InfoClass;
import database.database.DbOpenHelper;
import database.util.DLog;
import movable_classis.Movable_Layout_Class;

/**
 * Created by KimFamily on 2018-02-05.
 */

public class Button_allocate extends Activity{

    //프로젝트 테이블 에서 가져온것
    int project_list_num;
    private Cursor mCursor;
    private DbOpenHelper mDbOpenHelper;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;


    //버튼 데이터 테이블 에서 가져온것
    private DbOpenHelper_button mDbOpenHelper_btn;
    private Cursor mCursor_btn;
    private InfoClass_btn_data mInfoClass_btn;
    private ArrayList<InfoClass_btn_data> mInfoArray_btn;

    private TextView project_title;

    private RelativeLayout button_allocate_main_layout;
    private ImageButton button_creation;
    private CheckBox button_editing;
    private TextView msgbox_title;

    private int button_ids = 30000;


    DisplayMetrics dm;
    int display_width;
    int display_height;
    int screenSizeType;
    int resourceId;

    float text_size;
    int buttons_height;

    float line_size;


    /* 프로젝트 버튼 리스트 sharedpreference 관련 */
    SharedPreferences sharedPreferences_savaer;
    SharedPreferences.Editor sharedPreferences_editor;
    private ViewGroup mframe;
    private float loaction_x;
    private float loaction_y;

    private ViewGroup button_name_box;
    private EditText button_name_edit_text;
    private Button button_name_confirm;
    private Button button_name_cancel;

    private String[] button_list;




    public String this_project_name = "";






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_allocate);
        Intent intent = getIntent();



        button_name_box = (ViewGroup) findViewById(R.id.button_name_box);
        button_name_edit_text = (EditText) findViewById(R.id.button_name_edit_text);
        button_name_confirm = (Button)  findViewById(R.id.button_name_confirm);
        button_name_cancel = (Button)  findViewById(R.id.button_name_cancel);

        button_allocate_main_layout = (RelativeLayout) findViewById(R.id.button_allocate_main_layout);
        button_editing = (CheckBox) findViewById(R.id.button_editing);
        button_creation = (ImageButton) findViewById(R.id.button_creation);
        msgbox_title = (TextView) findViewById(R.id.msgbox_title);


        project_list_num = intent.getExtras().getInt("project_list_num");
//        Toast.makeText(this, project_list_num + "", Toast.LENGTH_LONG).show();

        /* 현재 프로젝트 이름 가져오기 */
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        DLog.e("load list", "COUNT = " + mCursor.getCount());

        project_title = (TextView) findViewById(R.id.project_title);

        int while_count = 0;
        while (mCursor.moveToNext()) {


            while_count++;
            if (while_count == project_list_num) {
                this_project_name = mCursor.getString(mCursor.getColumnIndex("name"));
                project_title.setText(this_project_name);
            }
        }

        mCursor.close();



        /********* 버튼 테이블에서 데이터 가져오기 **********/
        //현재 버튼 이름으로 테이블 데이터 가져오기
        mDbOpenHelper_btn = new DbOpenHelper_button(this,this_project_name);
        mDbOpenHelper_btn.open();

        //기존 데이터 찾아서 가져오기
        startManagingCursor(mCursor);


        mInfoArray_btn = new ArrayList<InfoClass_btn_data>();







        /* 화면 크기에 따라 버튼 크기 조절하기 */


        dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;
        screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
        resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

        line_size = convertPixelsToDp(display_height / 6, getApplicationContext());
//        Toast.makeText(getApplicationContext(),display_height +"",Toast.LENGTH_LONG).show();


        if(display_height < 1000){ //HD

            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 6;
            text_size =  (int) (buttons_height/1.2);

        }else if(display_height < 1400){ //FHD
            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 4;
            text_size = (int) (buttons_height/1.7);

        }else if(display_height < 2000){ //QHD
            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 3;
            text_size = (int) (buttons_height/2.3);

        }else{ //UHD
            buttons_height = (int) convertPixelsToDp(display_height, getApplicationContext()) / 2;
            text_size = (int) (buttons_height/2.6);
        }





        /* 버튼 추가 삭제 관련 */

        sharedPreferences_savaer = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences_editor = sharedPreferences_savaer.edit();





//버튼 만들기 버튼
        button_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                button_ids += 100;
//
//                button_name_box.setVisibility(View.VISIBLE);
//                msgbox_title.setText("생성할 버튼의 이름을 넣어 주세요.");
//                button_name_edit_text.setText("");
//                button_name_edit_text.requestFocus();

                final EditText edittext = new EditText(Button_allocate.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(Button_allocate.this);
                builder.setTitle("버튼 이름 수정");
                builder.setMessage("바꿀 버튼 이름을 넣어주세요. \n ※중복은 허용 안됨.");
                builder.setView(edittext);
                builder.setPositiveButton("입력",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
                                if(!mDbOpenHelper_btn.is_same_btn_existed(edittext.getText().toString())){
                                    mDbOpenHelper_btn.insertColumn_button_data(edittext.getText().toString().toString(), 100, 200, "1");
                                    int db_button_id = mDbOpenHelper_btn.get_id_by_Name_btn_data(edittext.getText().toString().toString());
                                    button_creation_method(db_button_id, edittext.getText().toString(), display_width/2, display_height/2);

                                }else{
                                    Toast.makeText(getApplicationContext(), "같은 이름의 데이터가 있습니다.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();





            }
        });

        //버튼이름 넣고 확인 버튼 누를때->@@안쓰는 코드
        button_name_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if(!mDbOpenHelper_btn.is_same_btn_existed(button_name_edit_text.getText().toString())){
                    mDbOpenHelper_btn.insertColumn_button_data(button_name_edit_text.getText().toString(), 100, 200, "1");
                    int db_button_id = mDbOpenHelper_btn.get_id_by_Name_btn_data(button_name_edit_text.getText().toString());
                    button_creation_method(db_button_id, button_name_edit_text.getText().toString(), display_width/2, display_height/2);

                }else{
                    Toast.makeText(getApplicationContext(), "같은 이름의 데이터가 있습니다.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        button_name_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_name_box.setVisibility(View.INVISIBLE);
//                boolean result = mProject_button_list_DB.deleteColumn(position + 1);
//                if(result){
//                    mInfoArray_db.remove(position);
//                    mAdapter.setArrayList(mInfoArray_db);
//                    mAdapter.notifyDataSetChanged();
//                }else {
//                    Toast.makeText(getApplicationContext(), "INDEX를 확인해 주세요.",
//                            Toast.LENGTH_LONG).show();
//                }

            }
        });


        button_editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                visible_or_not();


            }
        });



        //기존 데이터 불러와서 배치
        doWhileCursorToArray();

        visible_or_not();





    }//oncreate 끝





    public void button_creation_method(int db_button_id, String btn_name, int x_location, int y_location){

            String button_name_text = btn_name;

            ViewGroup frame = button_creating_method2(db_button_id, button_name_text, x_location, y_location, true);


            button_name_box.setVisibility(View.INVISIBLE);




    }






    private RelativeLayout button_creating_method2(final int id_numbers, final String button_name_method, int location_x, int location_y, Boolean moving_hold_permanently){

        final int this_layout_id_number = id_numbers;

        final LinearLayout line_two_for_setting_buttons = new LinearLayout((getApplicationContext()));
        line_two_for_setting_buttons.setOrientation(LinearLayout.VERTICAL);

        //line one
        LinearLayout frame_linear = new LinearLayout((getApplicationContext()));
        frame_linear.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout new_linear = new RelativeLayout(getApplicationContext());



        float text_length;

        if(display_height < 1000){ //HD
            text_length = (int) (button_name_method.length()*buttons_height*1.5);
//            new_linear.setPadding((int)(text_length/8),0,0,0);

        }else if(display_height < 1400){ //FHD
            text_length = (int) (button_name_method.length()*buttons_height*1.5);
//            new_linear.setPadding((int)(text_length/6),(button_name.length()/2),0,0);

        }else if(display_height < 2000){ //QHD
            text_length = (int) (button_name_method.length()*buttons_height*1.3);
//            new_linear.setPadding((int)(text_length/6),(button_name.length()/4),0,0);

        }else{ //UHD
            text_length = (int) (button_name_method.length()*buttons_height*1.7);
//            new_linear.setPadding((int)(text_length/6),0,0,0);

        }


        new_linear.setGravity(Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) text_length, buttons_height*2);
        new_linear.setLayoutParams(params);
        new_linear.setId(id_numbers);





        //imageview 버튼 수정 체크박스 아닐때 온클릭 리스너 동작
        ImageView new_buttons = new ImageView(getApplicationContext());
//        new_buttons.setScaleType(ImageView.ScaleType.FIT_XY);
        new_buttons.setId(this_layout_id_number+10000);
        new_buttons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        new_buttons.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_check));

        //메인 버튼 이름 누를때 기능을 wifi 로 보내기
        new_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        ImageView new_buttons2 = new ImageView(getApplicationContext());
        new_buttons2.setId(this_layout_id_number+20000);
        new_buttons2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        new_buttons2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_uncheck));

        new_buttons2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send wifi telnet


            }
        });


        final TextView new_texts = new TextView(getApplicationContext());
        new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_allocate_back_rectangle));
        new_texts.setTextColor(Color.rgb(0,0,0));
        new_texts.setTextSize(1,text_size);
        new_texts.setText(button_name_method);
        new_texts.setId(id_numbers+30000);




        ImageView move_arrow = new ImageView(getApplicationContext());
        move_arrow.setId(this_layout_id_number+40000);
        move_arrow.setLayoutParams(new ViewGroup.LayoutParams(buttons_height*2,buttons_height*2));
        move_arrow.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.move_arrow));
        move_arrow.bringToFront() ;

        ImageView move_arrow2 = new ImageView(getApplicationContext());
        move_arrow2.setId(this_layout_id_number+50000);
        move_arrow2.setLayoutParams(new ViewGroup.LayoutParams(buttons_height*2,buttons_height*2));
        move_arrow2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.move_arrow));
        move_arrow2.bringToFront() ;




        // line two
        LinearLayout line_two_setting_buttons = new LinearLayout((getApplicationContext()));
        line_two_setting_buttons.setOrientation(LinearLayout.HORIZONTAL);

        Button name_edit = new Button(getApplicationContext());
        name_edit.setText("수정");
        name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final ArrayList<String> ListItems = new ArrayList<>();
                ListItems.add("버튼 이름 수정");
                ListItems.add("코드 내용 수정");
                ListItems.add("취소");
                final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(Button_allocate.this);
                builder.setTitle("수정 선택");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {

                        String selectedText = items[pos].toString();
                        if(pos == 0) {//버튼 이름 수정
                            final EditText edittext = new EditText(Button_allocate.this);

                            AlertDialog.Builder builder = new AlertDialog.Builder(Button_allocate.this);
                            builder.setTitle("버튼 이름 수정");
                            builder.setMessage("바꿀 버튼 이름을 넣어주세요. \n ※중복은 허용 안됨.");
                            builder.setView(edittext);
                            builder.setPositiveButton("입력",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            new_texts.setText(edittext.getText().toString());
                                            if(!mDbOpenHelper_btn.is_same_btn_existed(edittext.getText().toString())) {
                                                mDbOpenHelper_btn.updateColumn_btn_data(id_numbers, edittext.getText().toString(), 0, 0); //x, y위치가 0이면 그대로 유지
                                            }else{
                                                Toast.makeText(Button_allocate.this, "같은 이름의 데이터가 있습니다. \n 다른 이름을 넣어 주세요.", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    });
                            builder.setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            builder.show();

                        }else if(pos == 1) { // 코드 내용 수정
                            Intent intent = new Intent(getApplication(), Algorithm_dev_activity.class);
                            intent.putExtra("project_list_num", button_name_method);
                            startActivity(intent);

                        }else if(pos == 2) {//취소
                            Toast.makeText(Button_allocate.this, selectedText, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();





            }
        });



        Button delete_btn = new Button(getApplicationContext());
        delete_btn.setText("삭제");
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(Button_allocate.this);
                alt_bld.setMessage("정말로 삭제하시겠습니까?").setCancelable(
                        false).setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Action for 'Yes' Button
                                mDbOpenHelper_btn.deleteColumn_btn_data(id_numbers);
                                line_two_for_setting_buttons.removeAllViews();
                                Toast.makeText(Button_allocate.this, "삭제됨", Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(Button_allocate.this, "취소", Toast.LENGTH_SHORT).show();

                            }
                        });
                AlertDialog alert = alt_bld.create();
                // Title for AlertDialog
                alert.setTitle("수정 Edit");
                // Icon for AlertDialog
                alert.show();









            }
        });

        Button copy_btn = new Button(getApplicationContext());
        copy_btn.setText("복사");
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!mDbOpenHelper_btn.is_same_btn_existed(button_name_method+"_복사")){
                    mDbOpenHelper_btn.duplicate_btn_data(button_name_method);
                    Cursor c = mDbOpenHelper_btn.getMatchName_btn_data(button_name_method+"_복사");

                    c.moveToFirst();
                    button_creating_method2(c.getInt(c.getColumnIndex("_id")),
                            c.getString(c.getColumnIndex("btn_name")),
                            c.getInt(c.getColumnIndex("x")),
                            c.getInt(c.getColumnIndex("y")),
                            true);

                }else{
                    Toast.makeText(getApplicationContext(), "이미 복사된 버튼이 있습니다.", Toast.LENGTH_SHORT).show();

                }








            }
        });


        line_two_setting_buttons.addView(name_edit);
        line_two_setting_buttons.addView(delete_btn);
        line_two_setting_buttons.addView(copy_btn);








        String[] new_buttons_location = new String[2];
        new_buttons_location[0] = "new_button_x" + this_layout_id_number;
        new_buttons_location[1] = "new_button_y" + this_layout_id_number;
        String scale_size = "scale_size" + this_layout_id_number;
        Movable_Layout_Class new_movable_button =
                new Movable_Layout_Class(this, button_allocate_main_layout, line_two_for_setting_buttons, new_buttons_location, scale_size, moving_hold_permanently);

        line_two_for_setting_buttons.addView(frame_linear);
        line_two_for_setting_buttons.addView(line_two_setting_buttons);

        frame_linear.addView(move_arrow);
        frame_linear.addView(new_linear);
        frame_linear.addView(move_arrow2);
        new_linear.addView(new_buttons);
        new_linear.addView(new_buttons2);
        new_linear.addView(new_texts);



        button_allocate_main_layout.addView(line_two_for_setting_buttons);
        line_two_for_setting_buttons.setX(location_x);
        line_two_for_setting_buttons.setY(location_y);




        visible_or_not();



        return new_linear;
    }
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private void visible_or_not(){
        if (button_editing.isChecked()) {
            button_creation.setVisibility(View.VISIBLE);
        } else {
            button_creation.setVisibility(View.INVISIBLE);

        }

        for(int i =30000; i <= button_ids; i += 100){
            ImageView new_buttons2 = findViewById(i+20000);
            ImageView move_arrow = findViewById(i+40000);
            ImageView move_arrow2 = findViewById(i+50000);
            button_name_box.setVisibility(View.INVISIBLE);

            try {
                if (button_editing.isChecked()) {
                    new_buttons2.setVisibility(View.INVISIBLE);
                    move_arrow.setVisibility(View.VISIBLE);
                    move_arrow2.setVisibility(View.VISIBLE);

                } else {
                    new_buttons2.setVisibility(View.VISIBLE);
                    move_arrow.setVisibility(View.INVISIBLE);
                    move_arrow2.setVisibility(View.INVISIBLE);

                }



            }catch (Exception e){

            }
        }


    }



    /**
     * DB에서 받아온 값을 ArrayList에 Add
     */
    private void doWhileCursorToArray(){

        mCursor_btn = null;
        mCursor_btn = mDbOpenHelper_btn.getAllColumns_btn_data();

        while (mCursor_btn.moveToNext()) {

            mInfoClass_btn = new InfoClass_btn_data(

                    mCursor_btn.getInt(mCursor_btn.getColumnIndex("_id")),
                    mCursor_btn.getString(mCursor_btn.getColumnIndex("btn_name")),
                    mCursor_btn.getInt(mCursor_btn.getColumnIndex("x")),
                    mCursor_btn.getInt(mCursor_btn.getColumnIndex("y")),
                    mCursor_btn.getString(mCursor_btn.getColumnIndex("coding"))



            );
            Log.i("class", mInfoClass_btn +"");
            mInfoArray_btn.add(mInfoClass_btn);
            button_creation_method(mCursor_btn.getInt(mCursor_btn.getColumnIndex("_id")), mCursor_btn.getString(mCursor_btn.getColumnIndex("btn_name")), mCursor_btn.getInt(mCursor_btn.getColumnIndex("x")), mCursor_btn.getInt(mCursor_btn.getColumnIndex("y")));


        }

        mCursor_btn.close();
    }



    //수정 버튼 누를때
    void show()
    {
        final ArrayList<String> ListItems = new ArrayList<>();
        ListItems.add("사과");
        ListItems.add("배");
        ListItems.add("귤");
        ListItems.add("바나나");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(Button_allocate.this, selectedText, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }






}
