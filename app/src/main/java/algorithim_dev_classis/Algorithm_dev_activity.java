package algorithim_dev_classis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
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
    private int[][] DB_buttons;
    public int[] algorithm_continuous;
    public int last_creating_id_number = 0;

    private ScrollView scrollview1;
    private LinearLayout first_line;
    private int previous_scroll_value;

    LanguageColors languageColors;


    DisplayMetrics dm;
    int display_width;
    int display_height;
    int screenSizeType;
    int resourceId;

    float line_size;


    public int first_line_getY;
//    public int maximum_id = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_dev_layout);

        DB_buttons = new int[4000][6]; //생성된 순서대로 id를 부여하고 버튼타입을 저장
        algorithm_continuous = new int[2000]; //id를 배치된 순서대로 넣는 배열



        dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;
        screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
        resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

        line_size = convertPixelsToDp(display_height/6, getApplicationContext());


        dev_layout_main = (RelativeLayout) findViewById(R.id.dev_layout_main);

        scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
        first_line = (LinearLayout) findViewById(R.id.first_line);

        /* 배경 줄 넣기         */

        //첫째 줄 시작 지점 텍스트 표기
        LinearLayout first_line_text_layout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams temp_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        first_line_text_layout.setOrientation(LinearLayout.HORIZONTAL);
        first_line_text_layout.setLayoutParams(temp_params);
        first_line_text_layout.setBackgroundColor(Color.rgb(0,0,0));
        first_line_text_layout.setGravity(Gravity.LEFT);

        TextView first_line_text = new TextView(getApplicationContext());
        first_line_text.setText("시작 지점");
        first_line_text.setLayoutParams(new ViewGroup.LayoutParams(  (int) ((float)display_width*6/12), 100));
        first_line_text.setBackgroundColor(Color.rgb(0,0,0));
        first_line_text.setTextSize(1,convertPixelsToDp(70,this));
        first_line_text_layout.addView(first_line_text);
        first_line.addView(first_line_text_layout);

        //나머지 줄 자동 생성
        languageColors =  new LanguageColors();
        for(int i =1; i<20 ; i++){
            LinearLayout new_linear = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            params.gravity = Gravity.CENTER;
            new_linear.setOrientation(LinearLayout.HORIZONTAL);
            new_linear.setLayoutParams(params);
            new_linear.setBackgroundColor(Color.rgb(0,0,0));

            ImageView new_line = new ImageView(getApplicationContext());
//            new_line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            new_line.setLayoutParams(new ViewGroup.LayoutParams(  (int) ((float)display_width/2), 100));

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


            TextView line_number = new TextView(getApplicationContext());
            line_number.setLayoutParams(new ViewGroup.LayoutParams((int) convertPixelsToDp((float)display_width/10, this), 100));
            line_number.setText(i+"");
            line_number.setBackgroundColor(Color.rgb(0,0,0));




            new_linear.addView(line_number);
            new_linear.addView(new_line);


            first_line.addView(new_linear);
            GradientDrawable bgShape = (GradientDrawable) new_line.getBackground();
            String selectedLanguage = "nomal" + i%2;
            bgShape.setColor(languageColors.getColor(selectedLanguage));

        }

/*
 일반 id 배치
 id 0~10 = 고정된 배치
 id + 3000 = for문 끝
 id + 2000 = if문 끝
 */



        /* 데이터 불러와서 배치하기 */
        for(int i = 10; i < 17; i++){
            DB_buttons[i][0] = (i)%10; //버튼 종류
            DB_buttons[i][1] = i+1; //다음 연속된 버튼 id
            DB_buttons[i][2] = 10; //x위치
            DB_buttons[i][3] = i*100; //y위치

            RelativeLayout new_Linear_layout = button_creating_method2(i, DB_buttons[i][0], DB_buttons[i][2], DB_buttons[i][3], true);
            algorithm_continuous[i-10] = i; //알고리즘 순서
            Log.i("algorithm_continuous "+(i-10), i+"");
            last_creating_id_number = i; //마지막 id 번호
        }
        DB_buttons[0][0] = 1;
        DB_buttons[0][1] = 10;
        DB_buttons[0][2] = 10;
        DB_buttons[0][3] = 100;
        algorithm_continuous[0] = 10;





        for(int i = 1; i < 9; i++){
            int y_location = (i-1)*100; // (i-1)*80 +50
            if(i == 7){
                continue;
            }else if(i == 8){
                y_location = (i-2)*100;
            }

                    /* 기본 고정된 기능 들 배치 시키기 */
            RelativeLayout hold_layout = button_creating_method2(i,i, (int) ((float)display_width*3/6), y_location, false);
                    /* 오른쪽 새로 생성 버튼 */
            RelativeLayout new_Linear_layout2 = button_creating_method2(i + last_creating_id_number,i, (int) ((float)display_width*3/6),y_location, true);
        }




        auto_lining();



        /*쓰레드*/
        Auto_lineup_and_dont_overaping auto_lineup_and_dont_overaping = new Auto_lineup_and_dont_overaping();
        auto_lineup_and_dont_overaping.setDaemon(true);
        auto_lineup_and_dont_overaping.start();



    }

    /***************oncreate 끝 ********************/


    class Auto_lineup_and_dont_overaping extends Thread{
        @Override
        public void run() {


            while(!this.isInterrupted()) {

                /* 스크롤 이동할때 */
                if(previous_scroll_value != scrollview1.getScrollY()){
                    Message msg =  Auto_lineup_and_dont_overaping_handler.obtainMessage();
                    msg.what =100;
                    Auto_lineup_and_dont_overaping_handler.sendMessage(msg);
                    previous_scroll_value = scrollview1.getScrollY();
                }


            }

        }
    }


    Handler Auto_lineup_and_dont_overaping_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 50: //새로운 레이아웃 생성

                    break;

                case 100: //스크롤 이동
//                    Toast.makeText(getApplicationContext(),scrollview1.getScrollY()+"",Toast.LENGTH_SHORT).show();
//                    DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//                    int display_width = dm.widthPixels;
//                    int display_height = dm.heightPixels;
//
//                    ViewGroup linerlayout1 = findViewById(0); //시작되는 레이아웃 id = 0
//                    try {
//                        linerlayout1.setY(display_height - getStatusBarHeight() - scrollview1.getScrollY());
//                    }catch(Exception e){
//
//                    }
//                    layout_placement_by_next_id(DB_buttons[0][1], 10, display_height - getStatusBarHeight() - (display_height - 100) - scrollview1.getScrollY());
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
        first_line_getY = display_height - getStatusBarHeight() - (display_height - 100) - scrollview1.getScrollY();
//        layout_placement_by_next_id(DB_buttons[0][1], 10, display_height - getStatusBarHeight() - (display_height - 100) - scrollview1.getScrollY());
                               //int algorithm_continuous_number, int pre_layout_x, int pre_layout_y
        layout_placement_by_next_id(0, 10, display_height - getStatusBarHeight() - (display_height - 100) - scrollview1.getScrollY());
        Log.i("first_line_getY", first_line_getY+"");
    }


    public boolean is_id_included_algorithm_continuous(int id){
        boolean it_was_included = false;
        for(int i =0; i<algorithm_continuous.length - 2; i++){
            if(algorithm_continuous[i] == id){
                it_was_included = true;
            }
            if (algorithm_continuous[i] == 0)
                break;
        }
        return it_was_included;
    }


    public void arranging_algorithm_continuous_from_layout_location(int touched_id){

        int temp_array_num=0;
        try{
            ViewGroup layout1 = findViewById(touched_id);
            if(layout1.getX() < convertPixelsToDp((float)display_width/2, this)) { //화면 중간을 넘어가면 배치 안하게
                push_id_next_line(touched_id, (-first_line_getY + (int) layout1.getY()+50) / 100 - 1);
                algorithm_continuous[(-first_line_getY + (int) layout1.getY()+50) / 100 - 1] = touched_id;
                Log.i("algorithm_continuous", (-first_line_getY + (int) layout1.getY()+50) / 100 - 1+ "에 id가 " + touched_id + ", button type : " + DB_buttons[touched_id][0]);


                if(DB_buttons[touched_id][0] == 6 && !is_id_included_algorithm_continuous(touched_id+2000)){ //if문이라면 if문 끝나는것 하나 추가 && 이미 만들어 져있으면 만들지 말고
                    int end_of_if_condition = touched_id + 2000;
                    RelativeLayout new_Relative_layout = button_creating_method2(end_of_if_condition, 7,  (int) ((float)display_width*3/5) ,(int)layout1.getY(), true);
                    push_id_next_line(end_of_if_condition, (-first_line_getY + (int) new_Relative_layout.getY()+50) / 100 );
                    algorithm_continuous[(-first_line_getY + (int) new_Relative_layout.getY()+50) / 100] = end_of_if_condition;
                    Log.i("algorithm_continuous", (-first_line_getY + (int) new_Relative_layout.getY()+50) / 100 + "에 id가 " + (end_of_if_condition));
                }

                if(DB_buttons[touched_id][0] == 8 && !is_id_included_algorithm_continuous(touched_id+2000)){ //for문이라면 for문 끝나는것 하나 추가 && 이미 만들어 져있으면 만들지 말고
                    int end_of_if_condition = touched_id + 2000;
                    RelativeLayout new_Relative_layout = button_creating_method2(end_of_if_condition, 9,  (int) ((float)display_width*3/5) ,(int)layout1.getY(), true);
                    push_id_next_line(end_of_if_condition, (-first_line_getY + (int) new_Relative_layout.getY()+50) / 100 );
                    algorithm_continuous[(-first_line_getY + (int) new_Relative_layout.getY()+50) / 100] = end_of_if_condition;
                    Log.i("algorithm_continuous", (-first_line_getY + (int) new_Relative_layout.getY()+50) / 100 + "에 id가 " + (end_of_if_condition));
                }

            }else{
                if(DB_buttons[touched_id][0] == 6 || DB_buttons[touched_id][0] == 7) { //if문이라면 if문 끝나는것도 함께 지우기
                    delete_void_or_double_id(touched_id%2000);
                    delete_void_or_double_id(touched_id%2000 + 2000);
                    ViewGroup delete_if_condition_child = findViewById(touched_id%2000);
                    ViewGroup delete_if_condition_child2 = findViewById(touched_id%2000 + 2000);
                    dev_layout_main.removeView(delete_if_condition_child);
                    dev_layout_main.removeView(delete_if_condition_child2);
                }else{
                    delete_void_or_double_id(touched_id);
                }

                if(DB_buttons[touched_id][0] == 8 || DB_buttons[touched_id][0] == 9) { //for문이라면 for문 끝나는것도 함께 지우기
                    delete_void_or_double_id(touched_id%2000);
                    delete_void_or_double_id(touched_id%2000 + 2000);
                    ViewGroup delete_if_condition_child = findViewById(touched_id%2000);
                    ViewGroup delete_if_condition_child2 = findViewById(touched_id%2000 + 2000);
                    dev_layout_main.removeView(delete_if_condition_child);
                    dev_layout_main.removeView(delete_if_condition_child2);
                }else{
                    delete_void_or_double_id(touched_id);
                }

            }
            auto_lining();

            /*line color change */

            int count = 0;
            for(int i =0; i< 100; i++) {

                //조건문 반복문 counting
                if (DB_buttons[algorithm_continuous[i]][0] == 6 || DB_buttons[algorithm_continuous[i]][0] == 8) {
                    count += 1;
                }



                changing_backline_color_if(i, count);

                //조건문 반복문 end decounting
                if (DB_buttons[algorithm_continuous[i]][0] == 7 || DB_buttons[algorithm_continuous[i]][0] == 9) {
                    changing_backline_color_if(i, count);
                    count -= 1;
                }


                if (algorithm_continuous[i] == 0)
                    break;
            }
        }catch (Exception e){
            Log.e("arranging_algorithm", e+"");
        }

    }

    public boolean push_id_next_line(int touch_id, int pre_id_location){
        try{
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
        }catch(Exception e){
            return false;
        }
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
        Log.i("db buttons : ", algorithm_continuous_number +"");

        ViewGroup layout1 = findViewById(algorithm_continuous[algorithm_continuous_number]);
        layout1.setX(pre_layout_x);
        layout1.setY(pre_layout_y+100);
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






    public void log_print(){
        Log.i("test", "test");
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


    private LinearLayout button_creating_method(int id_numbers,int button_type, int location_x, int location_y, Boolean moving_hold_permanently){

        int this_layout_id_number = id_numbers;


        LinearLayout new_linear = new LinearLayout(getApplicationContext());
        new_linear.setId(this_layout_id_number);


        ImageView new_buttons = new ImageView(getApplicationContext());
//        new_buttons.setId(this_layout_id_number);
        select_background_img(new_buttons, button_type); //백그라운드 이미지를 button type 번호에 따라서 배치
        new_buttons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            new_buttons.setOnClickListener(new_creation_buttons);

//            TextView new_texts = new TextView(getApplicationContext());
//            new_texts.setId(creating_button_number + 2);
//            new_texts.setText("이동 손잡이");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200);
        params.gravity = Gravity.CENTER;
        new_linear.setOrientation(LinearLayout.VERTICAL);
        new_linear.setLayoutParams(params);




//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(new_buttons.getWidth(), new_buttons.getHeight());
//            new_frames.setLayoutParams(params);


        String[] new_buttons_location = new String[2];
        new_buttons_location[0] = "new_button_x" + this_layout_id_number;
        new_buttons_location[1] = "new_button_y" + this_layout_id_number;
        String scale_size = "scale_size" + this_layout_id_number;
//            String new_button_scale = "new_button_scale" + creating_button_number;
        Movable_Layout_Class_auto_lineup new_movable_button =
                new Movable_Layout_Class_auto_lineup(
                        this, dev_layout_main, new_linear, new_buttons_location, scale_size, moving_hold_permanently,
                        this_layout_id_number);

        new_movable_button.Scale_size_adjustment(0.5f);

        new_linear.addView(new_buttons);
//            new_linear.addView(new_texts);
        dev_layout_main.addView(new_linear);
        new_linear.setX(location_x);
        new_linear.setY(location_y);


        return new_linear;
    }


    private RelativeLayout button_creating_method2(int id_numbers,int button_type, int location_x, int location_y, Boolean moving_hold_permanently){

        int this_layout_id_number = id_numbers;
        DB_buttons[id_numbers][0] = button_type; //버튼 종류 저장

        RelativeLayout new_linear = new RelativeLayout(getApplicationContext());
        new_linear.setId(this_layout_id_number);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 200);
        new_linear.setGravity(Gravity.CENTER_VERTICAL);
        new_linear.setLayoutParams(params);


        ImageView new_buttons = new ImageView(getApplicationContext());
//        new_buttons.setId(this_layout_id_number);
        select_background_img(new_buttons, button_type); //백그라운드 이미지를 button type 번호에 따라서 배치
        new_buttons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100));
//            new_buttons.setOnClickListener(new_creation_buttons);

        TextView new_texts = new TextView(getApplicationContext());
        new_texts.setTextSize(convertPixelsToDp(display_height/8, getApplicationContext()));
        new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle));
        new_texts.setTextColor(Color.rgb(0,0,0));
        new_texts.setTextSize(1,convertPixelsToDp(75,this));
        select_background_text(new_texts, button_type, id_numbers);








        String[] new_buttons_location = new String[2];
        new_buttons_location[0] = "new_button_x" + this_layout_id_number;
        new_buttons_location[1] = "new_button_y" + this_layout_id_number;
        String scale_size = "scale_size" + this_layout_id_number;
//            String new_button_scale = "new_button_scale" + creating_button_number;
        Movable_Layout_Class_auto_lineup new_movable_button =
                new Movable_Layout_Class_auto_lineup(
                        this, dev_layout_main, new_linear, new_buttons_location, scale_size, moving_hold_permanently,
                        this_layout_id_number);

        new_movable_button.Scale_size_adjustment(0.5f);

//        new_linear.addView(new_buttons);
        new_linear.addView(new_texts);
        dev_layout_main.addView(new_linear);
        new_linear.setX(location_x);
        new_linear.setY(location_y);


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) new_linear.getLayoutParams();
        layoutParams.width = 600;

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

    private void select_background_text(TextView new_texts, int button_type, int id_number){
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
                new_texts.setText("서보모터 각도 \n              = 360도");
                break;
            case 4 :
                new_texts.setText("측정된 거리 \n              ");
                break;
            case 5 :
                new_texts.setText("지연 시간 \n              = 0초");
                break;
            case 6 :
                new_texts.setText(id_number%2000+ " 만약에~라면");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_if));
                break;
            case 7 :
                new_texts.setText(id_number%2000+ " 만약에 끝");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_if));
                break;
            case 8 :
                new_texts.setText(id_number%2000 + " 반복하기");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_for));
                break;
            case 9 :
                new_texts.setText(id_number%2000 + " 반복하기 끝");
                new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle_for));
                break;


        }


    }










}
