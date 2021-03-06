package algorithim_dev_classis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ScrollingView;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimfamily.arduino_car_nodemcu.Main_activiy;
import com.example.kimfamily.arduino_car_nodemcu.R;

import java.util.ArrayList;
import java.util.Arrays;

import database.DbOpenHelper_algorithm;
import database.DbOpenHelper_button;
import database.InfoClass_algorithm;
import database.InfoClass_btn_data;
import movable_classis.Movable_Layout_Class;
import movable_classis.Movable_Layout_Class_auto_lineup;
import telnet.MainActivity;



/**
 * Created by KimFamily on 2017-10-25.
 */




public class Algorithm_dev_activity extends Activity implements View.OnClickListener {

    private ImageButton motor1_speed_btn, motor2_speed_btn, motor12_stop_btn, servo_motor_angle_btn, distance_value, delay_btn;
//    Shared_preference_for_saving_array_class shared_preference_for_saving_array_class;

    private RelativeLayout dev_layout_main;
    private int creating_button_id_number = 100;
    public int[][] DB_buttons;
    public int[] algorithm_continuous;
    public int last_creating_id_number = 0;
    private int last_creating_line = 0;
    public int[] unselected_buttons;
    public int button_type_numbers=8;

    private ScrollView scrollview1;
    private LinearLayout first_line;
    private int previous_scroll_value;

    LanguageColors languageColors;


    DisplayMetrics dm;
    public int display_width;
    int display_height;
    int screenSizeType;
    int resourceId;
    public int buttons_height;
    float text_size;

    int line_size;


    public int first_line_getY;
//    public int maximum_id = 0;


    /* DB관련 데이터*/
    private String DB_table_name_project_name_button_name;
    private DbOpenHelper_algorithm mDbOpenHelper_algorithm;
    private Cursor mCursor_algorithm;
    private InfoClass_algorithm mInfoClass_algorithm;
    private ArrayList<InfoClass_algorithm> mInfoClass_algorithm_list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_dev_layout);

        /*기존 db사용전 순서 정리용 변수*/
        DB_buttons = new int[4000][6]; //생성된 순서대로 id를 부여하고 버튼타입을 저장
        algorithm_continuous = new int[2000]; //id를 배치된 순서대로 넣는 배열
        Arrays.fill(algorithm_continuous, -1); //특정 값으로 초기화

        /*DB 관련 변수 초기화 */
        Intent intent = getIntent();
        DB_table_name_project_name_button_name = intent.getExtras().getString("project_name_button_name");
        mDbOpenHelper_algorithm = new DbOpenHelper_algorithm(this, DB_table_name_project_name_button_name);
        mDbOpenHelper_algorithm.open();
        mInfoClass_algorithm_list = new ArrayList<InfoClass_algorithm>();



        dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;
        screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
        resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

        line_size = (int) convertPixelsToDp(display_height/6, getApplicationContext());


        if(display_height < 1000){ //HD
//            buttons_height = (int)convertPixelsToDp((display_height/4), getApplicationContext());
//            text_size = buttons_height/2;
            buttons_height = (int) convertPixelsToDp(display_height, this) / 6;
            text_size =  (int) (buttons_height/1.2);

        }else if(display_height < 1400){ //FHD
            buttons_height = (int) convertPixelsToDp(display_height, this) / 4;
            text_size = (int) (buttons_height/1.7);

        }else if(display_height < 2000){ //QHD
            buttons_height = (int) convertPixelsToDp(display_height, this) / 3;
            text_size = (int) (buttons_height/2.3);


        }else{ //UHD

        }

//        Toast.makeText(this, buttons_height+"", Toast.LENGTH_SHORT).show();





        dev_layout_main = (RelativeLayout) findViewById(R.id.dev_layout_main);

        scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
        scrollview1.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollview1.getScrollY(); // For ScrollView
//                int scrollX = scrollview1.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                Message msg =  Auto_lineup_and_dont_overaping_handler.obtainMessage();
                msg.what =100;
                Auto_lineup_and_dont_overaping_handler.sendMessage(msg);
                previous_scroll_value = scrollview1.getScrollY();
            }
        });

        first_line = (LinearLayout) findViewById(R.id.first_line);

        /* 배경 줄 넣기         */

        //첫째 줄 시작 지점 텍스트 표기
        LinearLayout first_line_text_layout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams temp_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, buttons_height);
        first_line_text_layout.setOrientation(LinearLayout.HORIZONTAL);
        first_line_text_layout.setLayoutParams(temp_params);
        first_line_text_layout.setBackgroundColor(Color.rgb(0,0,0));
        first_line_text_layout.setGravity(Gravity.LEFT);

        TextView first_line_text = new TextView(getApplicationContext());
        first_line_text.setText("시작 지점");
        first_line_text.setLayoutParams(new ViewGroup.LayoutParams(  (int) ((float)display_width*6/12), ViewGroup.LayoutParams.MATCH_PARENT));
        first_line_text.setBackgroundColor(Color.rgb(0,0,0));
        first_line_text.setTextSize(1,convertPixelsToDp(70,this));
        first_line_text_layout.addView(first_line_text);
        first_line.addView(first_line_text_layout);

 /* 기본 고정된 기능 들 배치 시키기 */
        unselected_buttons = new int[8];
        for(int i = 0; i < button_type_numbers; i++){
            int y_location = (i)*buttons_height; // (i-1)*80 +50

            final RelativeLayout new_Linear_layout2 = button_creating_method2(i,i, (int) ((float)display_width*3/6),y_location, true, 0, "");
            unselected_buttons[i] = i;
            last_creating_id_number++;


        }



//        last_creating_id_number = 100;
        /* 데이터 불러와서 배치하기 */
//        for(int i = 0; i < button_type_numbers; i++){
//            DB_buttons[i][0] = (i)%10; //버튼 종류
//            DB_buttons[i][1] = i+1; //다음 연속된 버튼 id
//            DB_buttons[i][2] = 10; //x위치
//            DB_buttons[i][3] = i*buttons_height; //y위치
//
//            final RelativeLayout new_Linear_layout = button_creating_method2(i+last_creating_id_number, (i)%10, 10, i*buttons_height, true);
//            algorithm_continuous[i] = i+last_creating_id_number++; //알고리즘 순서
//            Log.i("algorithm_continuous "+(i-10), i+"");
//
//        }
//        last_creating_id_number += 10;
        line_creater(last_creating_id_number+20);
//        DB_buttons[0][0] = 1;
//        DB_buttons[0][1] = 10;
//        DB_buttons[0][2] = 10;
//        DB_buttons[0][3] = 100;
//        algorithm_continuous[0] = 10;












        auto_lining();



        /*쓰레드*/
//        Auto_lineup_and_dont_overaping auto_lineup_and_dont_overaping = new Auto_lineup_and_dont_overaping();
//        auto_lineup_and_dont_overaping.setDaemon(true);
//        auto_lineup_and_dont_overaping.start();

        for(int i=0; i<button_type_numbers;i++) {
            Log.i("unselected array", unselected_buttons[i]+"");
        }
        Log.i("last id number", last_creating_id_number+"");

    }

    /***************oncreate 끝 ********************/


    class Auto_lineup_and_dont_overaping extends Thread{
        @Override
        public void run() {


            while(!this.isInterrupted()) {

                /* 스크롤 이동할때 */
//                if(previous_scroll_value != scrollview1.getScrollY()){
//                    Message msg =  Auto_lineup_and_dont_overaping_handler.obtainMessage();
//                    msg.what =100;
//                    Auto_lineup_and_dont_overaping_handler.sendMessage(msg);
//                    previous_scroll_value = scrollview1.getScrollY();
//                }


            }

        }
    }


    Handler Auto_lineup_and_dont_overaping_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 50: //레이아웃 이동 감지시

                    ViewGroup select_view = findViewById(msg.arg1);
                    int y_location = (DB_buttons[msg.arg1][0])*buttons_height;
                    //배치가 되고 새로 생길때
                    RelativeLayout new_Linear_layout = button_creating_method2(
                            last_creating_id_number, DB_buttons[msg.arg1][0],
                            (int) ((float)display_width*3/6), y_location, true, 0 , "");
                    select_view.bringToFront();
                    unselected_buttons[DB_buttons[msg.arg1][0]] = last_creating_id_number++;
                    break;

                case 100: //스크롤 이동
                    auto_lining();
                break;

            }
        }
    };

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


    private int getStatusBarHeight(){

        int statusHeight = 0;


        if(screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {

            if (resourceId > 0) {
                statusHeight = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusHeight;
    }


    public void auto_lining(){


        first_line_getY = buttons_height -  scrollview1.getScrollY();

        if(display_height < 1000){ //HD

            first_line_getY += 3;

        }else if(display_height < 1400){ //FHD

            first_line_getY += 15;

        }else if(display_height < 2000){ //QHD

            first_line_getY += 21;

        }else{ //UHD


        }


        //처음 데이터가 있으면 순서대로 불러오기
        if(algorithm_continuous[0] > 0) {
            layout_placement_by_next_id(0, 10, first_line_getY);
        }
    }


    public boolean unselected_or_not(int id){
        boolean unselected_or_not = false;
        for(int i =0; i<button_type_numbers; i++){
            if(unselected_buttons[i] == id){
                unselected_or_not = true;
//                Toast.makeText(getApplicationContext(),"unselected id",Toast.LENGTH_SHORT).show();
            }

        }
        return unselected_or_not;
    }




    public void arranging_algorithm_continuous_from_layout_location(int touched_id){

//        try{
            ViewGroup layout1 = findViewById(touched_id);
//        Toast.makeText(getApplicationContext(),touched_id+"",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),layout1.getWidth()+":"+layout1.getHeight(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),layout1.getId()+"",Toast.LENGTH_SHORT).show();


        //오른쪽 버튼 땡겨쓰면 새로 만들어 주는 기능
        if(unselected_or_not(touched_id)){
            Message msg = Auto_lineup_and_dont_overaping_handler.obtainMessage();
            msg.what =50;
            msg.arg1 = touched_id;
//            Toast.makeText(getApplicationContext(),touched_id+":last id num",Toast.LENGTH_SHORT).show();

            Auto_lineup_and_dont_overaping_handler.sendMessage(msg);

        }




        for(int i=0;i<unselected_buttons.length;i++)
            Log.i("unselected_buttons",i + ":" + unselected_buttons[i]);
            if(layout1.getX() < convertPixelsToDp((float)display_width/2, this)) { //왼쪽으로 끌어서 코드 내용으로 추가할때


                    int calculation_array_num = (-first_line_getY + (int) layout1.getY()+buttons_height/2) / buttons_height;
                    push_id_next_line(touched_id, calculation_array_num - 1);
                    algorithm_continuous[calculation_array_num - 1] = touched_id;
//                    mDbOpenHelper_algorithm.updateColumn_by_layout_id(touched_id, calculation_array_num - 1);//db를 layoutid로 찾아서 업데이트 해줌


                    Log.i("algorithm_continuous", calculation_array_num - 1+ "에 id가 " + touched_id + ", button type : " + DB_buttons[touched_id][0]);



                    if((DB_buttons[touched_id][0] == 6 || DB_buttons[touched_id][0] == 7) && unselected_or_not(touched_id)){ //if문이라면 if문 끝나는것 하나 추가 && 이미 만들어 져있으면 만들지 말고
//                        int end_of_if_condition = touched_id + 2000;
                        int end_of_if_condition = touched_id+1000;

                        RelativeLayout new_Relative_layout = button_creating_method2(end_of_if_condition, 8,  (int) ((float)display_width*3/5) ,(int)layout1.getY(), true,0,"");
                        push_id_next_line(end_of_if_condition, calculation_array_num );
                        algorithm_continuous[calculation_array_num] = end_of_if_condition;
//                        Log.i("algorithm_continuous", calculation_array_num + "에 id가 " + (end_of_if_condition));
                        for(int i =0; i<algorithm_continuous.length;i++){
                            Log.i("algoritm :",i+":"+algorithm_continuous[i]);
                            if(algorithm_continuous[i] == 0)
                                break;
                        }

                    }


            }else{

//                Toast.makeText(getApplicationContext(),"no arrange"+touched_id,Toast.LENGTH_SHORT).show();
                if(DB_buttons[touched_id][0] == 6 || DB_buttons[touched_id][0] == 7) {
                    //if나 for문이라면 끝나는것도 함께 지우기
                    int near_end_condition_id = find_end_condition_by_button_type_below(touched_id);
                    delete_void_or_double_id(touched_id);
                    delete_void_or_double_id(near_end_condition_id);
                    ViewGroup delete_if_condition_child = findViewById(touched_id);
                    ViewGroup delete_if_condition_child2 = findViewById(near_end_condition_id);
                    dev_layout_main.removeView(delete_if_condition_child);
                    dev_layout_main.removeView(delete_if_condition_child2);
                }else if(DB_buttons[touched_id][0] == 8) {
                    int near_end_condition_id = find_end_condition_by_button_type_upper(touched_id);
                    delete_void_or_double_id(touched_id);
                    delete_void_or_double_id(near_end_condition_id);
                    ViewGroup delete_if_condition_child = findViewById(touched_id);
                    ViewGroup delete_if_condition_child2 = findViewById(near_end_condition_id);
                    dev_layout_main.removeView(delete_if_condition_child);
                    dev_layout_main.removeView(delete_if_condition_child2);
                }else{
                    //조건 반목문이 아닌 일반 버튼은 그냥 삭제
                    delete_void_or_double_id(touched_id);
                    ViewGroup delete_if_condition_child = findViewById(touched_id);
                    dev_layout_main.removeView(delete_if_condition_child);
                }


            }
            auto_lining();

            /*line color change */
//            if(algorithm_continuous[0] > 0) {
//            int count = 0;
//            for (int i = 0; i < algorithm_continuous.length - 2; i++) {
//                //조건문 반복문 counting
//                if (DB_buttons[algorithm_continuous[i]][0] == 6 || DB_buttons[algorithm_continuous[i]][0] == 7) {
//                    count += 1;
//                }
//                changing_backline_color_if(i, count);
//                //조건문 반복문 end decounting
//                if (DB_buttons[algorithm_continuous[i]][0] == 8) {
//                    changing_backline_color_if(i, count);
//                    count -= 1;
//                }
//                if (algorithm_continuous[i] == 0)
//                    break;
//            }
//        }

            /* line exceed */



//        }catch (Exception e){
//            Log.e("arranging_algorithm", e+"");
//        }


    }



    private int find_end_condition_by_button_type_below(int touch_id){
        int return_id=0;
        boolean eixisting_touch_id = false; //기존 알고리즘에 포함되었던건지 확인
        int eixistied_location = 0;
        int count = 0;
        for(int i =0;  i < algorithm_continuous.length - 2; i++) {
            //조건문 반복문 counting
            if (DB_buttons[algorithm_continuous[i]][0] == 6 || DB_buttons[algorithm_continuous[i]][0] == 7) {
                count += 1;
                if(algorithm_continuous[i] == touch_id){
                    eixistied_location = count;
                }
            }
            //조건문 반복문 end decounting
            if (DB_buttons[algorithm_continuous[i]][0] == 8) {
                if(count == eixistied_location){
                    return_id = algorithm_continuous[i];
                    break;
                }
                count -= 1;

            }
            if (algorithm_continuous[i] == 0)
                break;
        }


        return return_id;
    }

    private int find_end_condition_by_button_type_upper(int touch_id){
        //역으로 올라가기 위해 배열의 마지막을 찾음
        int end_algorithm_continuous = 0;
        for(int i =0;  i < algorithm_continuous.length - 2; i++) {
            if (algorithm_continuous[i] == 0) {
                end_algorithm_continuous = i;
                break;
            }
        }


        int return_id=0;
        boolean eixisting_touch_id = false; //기존 알고리즘에 포함되었던건지 확인
        int eixistied_location = 0;
        int count = 0;
        for(int i =end_algorithm_continuous;  i > 0 ; i--) {
            if (DB_buttons[algorithm_continuous[i]][0] == 8) {
                count += 1;
                if(algorithm_continuous[i] == touch_id){
                    eixistied_location = count;
                }
            }


            //조건문 반복문 counting
            if (DB_buttons[algorithm_continuous[i]][0] == 6 || DB_buttons[algorithm_continuous[i]][0] == 7) {
                if(count == eixistied_location){
                    return_id = algorithm_continuous[i];
                    break;
                }
                count -= 1;
            }

        }
        return return_id;
    }

    public boolean push_id_next_line(int touch_id, int pre_id_location){
//        try{
            delete_void_or_double_id(touch_id);
            int[] temp_algorithm_contiuous = new int[2000];

            for(int i=pre_id_location; i < algorithm_continuous.length-2;i++){ //id 순서 배열 복제
                temp_algorithm_contiuous[i] = algorithm_continuous[i];
                if(algorithm_continuous[i] == 0)
                    break;
            }
            for(int i=pre_id_location; i < algorithm_continuous.length-2;i++){ //id를 한칸씩 밀기
                algorithm_continuous[i+1] = temp_algorithm_contiuous[i];
                if(algorithm_continuous[i] == 0)
                    break;
            }


            return true;
//        }catch(Exception e){
//            return false;
//        }
    }

    public boolean delete_void_or_double_id(int touch_id){

        try {
            boolean eixisting_touch_id = false; //기존 알고리즘에 포함되었던건지 확인
            int eixistied_location = 0;
            for (int i = 0; i < algorithm_continuous.length - 2; i++) {
                if (algorithm_continuous[i] == touch_id) {
                    eixisting_touch_id = true;
                    eixistied_location = i;
                    Log.i("already included id", touch_id+"");
                }
                if (algorithm_continuous[i] == 0)
                    break;
            }

            if (eixisting_touch_id) {
                int[] temp_algorithm_continuous = new int[2000];
                for (int i = eixistied_location; i < algorithm_continuous.length - 2; i++) { // 배열 복사
                    temp_algorithm_continuous[i] = algorithm_continuous[i];
                    if (algorithm_continuous[i] == 0)
                        break;
                }
                for (int i = eixistied_location; i < algorithm_continuous.length - 2; i++) { //뒤에있던것 앞으로 땡기기
                    algorithm_continuous[i] = temp_algorithm_continuous[i + 1];
                    if (algorithm_continuous[i] == 0)
                        break;
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }



    }


    public boolean layout_placement_by_next_id(int algorithm_continuous_number, int pre_layout_x, int pre_layout_y){
//        Log.i("db buttons : ", algorithm_continuous_number +"");

        RelativeLayout layout1 = findViewById(algorithm_continuous[algorithm_continuous_number]);
        layout1.setX(pre_layout_x);
        layout1.setY(pre_layout_y+buttons_height);
//        layout1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout1.getLayoutParams();
//        layoutParams.leftMargin = 10;
        try {
            if (algorithm_continuous[algorithm_continuous_number++] > 0) { //다음 연속된 id가 저장되어 있으면
                layout_placement_by_next_id(algorithm_continuous_number++, (int) layout1.getX(), (int) layout1.getY());
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    /* UI pretty method */
    private void changing_backline_color_if(int line_number, int count){
        try {
            ImageView new_line = new ImageView(getApplicationContext());
            new_line = findViewById(90000 + line_number+1);
            GradientDrawable bgShape = (GradientDrawable) new_line.getBackground();

            if(count != 0) {
                String selectedLanguage;
                selectedLanguage = "if" + count % 10;

                bgShape.setColor(languageColors.getColor(selectedLanguage));
            }else{
                String selectedLanguage = "nomal" + (line_number+1)%2;
                bgShape.setColor(languageColors.getColor(selectedLanguage));
            }

        }catch (Exception e){

        }

    }


    private void line_creater(int line_number){


        languageColors =  new LanguageColors();
        for(int i =1; i<line_number ; i++){
            LinearLayout new_linear = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, buttons_height);
            params.gravity = Gravity.CENTER;
            new_linear.setOrientation(LinearLayout.HORIZONTAL);
            new_linear.setLayoutParams(params);
            new_linear.setBackgroundColor(Color.rgb(0,0,0));

            ImageView new_line = new ImageView(getApplicationContext());
//            new_line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            new_line.setLayoutParams(new ViewGroup.LayoutParams(  (int) ((float)display_width/2), ViewGroup.LayoutParams.MATCH_PARENT));

            new_line.setImageDrawable(getResources().getDrawable(R.drawable.line_image));
            new_line.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.myshape));
            new_line.setId(90000 + i);
            final int temp_i = i;
            new_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),temp_i+"", Toast.LENGTH_SHORT).show();
                }
            });


            TextView line_number2 = new TextView(getApplicationContext());
            line_number2.setLayoutParams(new ViewGroup.LayoutParams((int) convertPixelsToDp((float)display_width/10, this), ViewGroup.LayoutParams.MATCH_PARENT));
            line_number2.setText(i+"");
            line_number2.setBackgroundColor(Color.rgb(0,0,0));
            line_number2.setTextSize(text_size/4);




            new_linear.addView(line_number2);
            new_linear.addView(new_line);


            first_line.addView(new_linear);
            GradientDrawable bgShape = (GradientDrawable) new_line.getBackground();
            String selectedLanguage = "nomal" + i%2;
            bgShape.setColor(languageColors.getColor(selectedLanguage));

        }

    }




    public void unselected_button_creation(int touch_id){
        Message msg =  Auto_lineup_and_dont_overaping_handler.obtainMessage();
        msg.what =50;
        msg.arg1 = touch_id;

        Auto_lineup_and_dont_overaping_handler.sendMessage(msg);

        StringBuilder a = new StringBuilder();
        a.append(touch_id + ", unselect \n ");
        for(int i=0;i<unselected_buttons.length;i++){
            a.append(" i:");
            a.append(unselected_buttons[i]);

        }

        Log.i("unselect",a+"");

    }










    @Override
    public void onClick(View view) {

    }

    View.OnClickListener user_listing_buttons = new View.OnClickListener() {
        @Override
        public void onClick(View v) {




        }
    };


    View.OnClickListener new_creation_buttons = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };




    public RelativeLayout button_creating_method2(int id_numbers,int button_type, int location_x, int location_y, Boolean moving_hold_permanently, int algorithm_continuous_array_num, String function_data){





//        DB_buttons[id_numbers][0] = button_type; //버튼 종류
//        DB_buttons[id_numbers][1] = 0; //다음 연속된 버튼 id
//        DB_buttons[id_numbers][2] = location_x; //x위치
//        DB_buttons[id_numbers][3] = location_y; //y위치



        int this_layout_id_number = id_numbers;

        RelativeLayout new_linear = new RelativeLayout(getApplicationContext());
        new_linear.setId(this_layout_id_number);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,buttons_height);
        new_linear.setGravity(Gravity.CENTER_VERTICAL);
        new_linear.setLayoutParams(params);
//        new_linear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });


        ImageView new_buttons = new ImageView(getApplicationContext());
//        new_buttons.setId(this_layout_id_number);
        select_background_img(new_buttons, button_type); //백그라운드 이미지를 button type 번호에 따라서 배치
        new_buttons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            new_buttons.setOnClickListener(new_creation_buttons);

        TextView new_texts = new TextView(getApplicationContext());
//        new_texts.setTextSize(convertPixelsToDp(display_height/8, getApplicationContext()));
        new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle));
        new_texts.setTextColor(Color.rgb(0,0,0));
//        new_texts.setTextSize(1,convertPixelsToDp(75,this));
        new_texts.setTextSize(text_size);
        select_background_text(new_linear, new_texts, button_type, id_numbers);








        String[] new_buttons_location = new String[2];
        new_buttons_location[0] = "new_button_x" + this_layout_id_number;
        new_buttons_location[1] = "new_button_y" + this_layout_id_number;
        String scale_size = "scale_size" + this_layout_id_number;
//            String new_button_scale = "new_button_scale" + creating_button_number;
        Movable_Layout_Class_auto_lineup new_movable_button =
                new Movable_Layout_Class_auto_lineup(
                        this, dev_layout_main, new_linear, new_buttons_location, scale_size, moving_hold_permanently,
                        this_layout_id_number);

//        new_movable_button.Scale_size_adjustment(0.5f);

//        new_linear.addView(new_buttons);
        new_linear.addView(new_texts);
        dev_layout_main.addView(new_linear);
        new_linear.setX(location_x);
        new_linear.setY(location_y);


//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) new_linear.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,buttons_height*2);
        layoutParams.width = buttons_height*13;
        new_linear.setLayoutParams(layoutParams);


        return new_linear;
    }






    /**버튼 자동으로 생성시 백그라운드를 쉽게 설정하기 위한 함수
     *
     * @param id_number
     * @return
     */
    private void select_background_img(ImageView imageView, int id_number) {


        switch (id_number) {
            case 1:
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor1_speed_btn));
                break;
            case 2:
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor2_speed_btn));
                break;
            case 3:
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor12_stop_btn));
                break;
            case 4:
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.servo_motor_angle_btn));
                break;
            case 5:
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.distance_value));
                break;
            case 6:
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delay_btn));
                break;
            case 7:
//                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor1_speed_btn));
                break;
        }
    }

    private void select_background_text(ViewGroup layout, TextView new_texts, int button_type, int id_number){
//        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 10));
        new_texts.setTextSize(1,text_size);
        switch (button_type){
            case 99 :
                new_texts.setText("시 작  지 점");
                break;

            case 0 :
                new_texts.setText(" 모터1 속도 \n              = 0 ");
                break;
            case 1 :
                new_texts.setText(" 모터2 속도 \n              = 0");
                break;
            case 2 :
                new_texts.setText(" 모터1,2 \n              정지");
                break;
            case 3 :
                new_texts.setText(" 서보모터 각도 \n              = 360도");
                break;
            case 4 :
                new_texts.setText(" 측정된 거리 \n              ");
                break;
            case 5 :
                new_texts.setText(" 지연 시간 \n              = 0초");
                break;
            case 6 :
                new_texts.setText(" 만약에~라면");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_if));
                break;
            case 7 :
                new_texts.setText(" 반복하기");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_if));
                break;
            case 8 :
                new_texts.setText("~여기까지");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_for));
                break;
            case 9 :
                new_texts.setText(" 반복하기 끝");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_for));
                break;


        }


    }



    /**
     * DB에서 받아온 값을 ArrayList에 Add
     */
    private void doWhileCursorToArray() {

        mCursor_algorithm = null;
        mCursor_algorithm = mDbOpenHelper_algorithm.getAllColumns_btn_data();

        while (mCursor_algorithm.moveToNext()) {

            mInfoClass_algorithm = new InfoClass_algorithm(
                    mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("_id")),
                    mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("function_type")),
                    mCursor_algorithm.getString(mCursor_algorithm.getColumnIndex("function_data")),
                    mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("algorithm_continuous")),
                    mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("layout_id"))




                    );
            Log.i("class", mInfoClass_algorithm + "");
            mInfoClass_algorithm_list.add(mInfoClass_algorithm);

            //불러온데이터 순서 데이터 넣기
            algorithm_continuous[mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("algorithm_continuous"))] = mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("layout_id"));
            //불러온 데이터 일단 만들기
            button_creating_method2(
                    mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("layout_id")),
                    mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("function_type")),
                    0,0, true,
                    mCursor_algorithm.getInt(mCursor_algorithm.getColumnIndex("algorithm_continuous")),
                    mCursor_algorithm.getString(mCursor_algorithm.getColumnIndex("function_data"))
                    );


        }
        auto_lining();

        mCursor_algorithm.close();
    }




    @Override
    protected void onDestroy() {
        mDbOpenHelper_algorithm.close();
        super.onDestroy();
    }


}
