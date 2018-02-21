package algorithim_dev_classis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimfamily.arduino_car_nodemcu.R;

import java.util.ArrayList;

import database.adapter.CustomAdapter;
import database.data.InfoClass;
import database.database.DbOpenHelper;
import database.util.DLog;
import movable_classis.Movable_Layout_Class;
import movable_classis.Movable_Layout_Class_auto_lineup;

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

    private RelativeLayout button_allocate_main_layout;
    private ImageButton button_creation;
    private CheckBox button_editing;

    private int button_ids = 30000;


    DisplayMetrics dm;
    int display_width;
    int display_height;
    int screenSizeType;
    int resourceId;

    float text_size;
    int buttons_height;

    float line_size;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_allocate);
        Intent intent = getIntent();

        project_list_num = intent.getExtras().getInt("project_list_num");
//        Toast.makeText(this, project_list_num + "", Toast.LENGTH_LONG).show();

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
                project_title.setText(mCursor.getString(mCursor.getColumnIndex("name")));
            }
        }

        mCursor.close();


        dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;
        screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
        resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

        line_size = convertPixelsToDp(display_height / 6, getApplicationContext());
        Toast.makeText(getApplicationContext(),display_height +"",Toast.LENGTH_LONG).show();

        if(display_height < 1000){ //HD
//            buttons_height = (int)convertPixelsToDp((display_height/4), getApplicationContext());
//            text_size = buttons_height/2;
            text_size = 20;
            buttons_height = 60;
        }else if(display_height < 1400){ //FHD
            text_size = 25;
            buttons_height = 100;
        }else if(display_height < 2000){ //QHD
            text_size = 25;
            buttons_height = 120;
        }else{ //UHD

        }

        button_allocate_main_layout = (RelativeLayout) findViewById(R.id.button_allocate_main_layout);

        button_creation = (ImageButton) findViewById(R.id.button_creation);
        button_creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_ids += 100;
                button_creating_method2(button_ids, "버튼1",display_width/2, button_ids % 30000, true);

            }
        });

        button_editing = (CheckBox) findViewById(R.id.button_editing);
        button_editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =30000; i <= button_ids; i += 100){
                    ImageView new_buttons = findViewById(i);
                    if(!button_editing.isChecked()) {
                        new_buttons.setOnClickListener(null);
                        new_buttons.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_uncheck));
                    }else{
                        new_buttons.setOnClickListener(null);
                        new_buttons.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_check));
                    }
                }


            }
        });
    }



    private RelativeLayout button_creating_method2(int id_numbers, String button_name, int location_x, int location_y, Boolean moving_hold_permanently){

        final int this_layout_id_number = id_numbers;

        RelativeLayout new_linear = new RelativeLayout(getApplicationContext());


        float text_length;

        if(display_height < 1000){ //HD
            text_length = button_name.length()*buttons_height*2/3;
            new_linear.setPadding((int)(text_length/8),0,0,0);

        }else if(display_height < 1400){ //FHD
            text_length = button_name.length()*buttons_height;
            new_linear.setPadding((int)(text_length/6),0,0,0);

        }else if(display_height < 2000){ //QHD
            text_length = button_name.length()*buttons_height;
            new_linear.setPadding((int)(text_length/6),0,0,0);

        }else{ //UHD
            text_length = button_name.length()*buttons_height;
            new_linear.setPadding((int)(text_length/6),0,0,0);

        }


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(text_length), buttons_height);
        new_linear.setGravity(Gravity.CENTER_VERTICAL);
        new_linear.setLayoutParams(params);





        //imageview 버튼 수정 체크박스 아닐때 온클릭 리스너 동작
        ImageView new_buttons = new ImageView(getApplicationContext());
        new_buttons.setScaleType(ImageView.ScaleType.FIT_XY);
        new_buttons.setId(this_layout_id_number);
        new_buttons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if(!button_editing.isChecked()) {
            new_buttons.setVisibility(View.INVISIBLE);
            new_buttons.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_check));

        }else{
            new_buttons.setVisibility(View.VISIBLE);
            new_buttons.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_editing_uncheck));
        }
        new_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        TextView new_texts = new TextView(getApplicationContext());
        new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_allocate_back_rectangle));
        new_texts.setTextColor(Color.rgb(0,0,0));
        new_texts.setTextSize(1,text_size);
        new_texts.setText(button_name);


        new_texts.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplication(), Algorithm_dev_activity.class);
                intent.putExtra("button_name", this_layout_id_number);
                startActivity(intent);
                return false;
            }
        });





        String[] new_buttons_location = new String[2];
        new_buttons_location[0] = "new_button_x" + this_layout_id_number;
        new_buttons_location[1] = "new_button_y" + this_layout_id_number;
        String scale_size = "scale_size" + this_layout_id_number;
        Movable_Layout_Class new_movable_button =
                new Movable_Layout_Class(this, button_allocate_main_layout, new_linear, new_buttons_location, scale_size, moving_hold_permanently);


        new_linear.addView(new_buttons);
        new_linear.addView(new_texts);
        button_allocate_main_layout.addView(new_linear);
        new_linear.setX(location_x);
        new_linear.setY(location_y);




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


}
