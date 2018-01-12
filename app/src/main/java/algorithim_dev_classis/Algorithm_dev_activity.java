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

    private ScrollView scrollview1;
    private LinearLayout first_line;
    private int previous_scroll_value;

    LanguageColors languageColors;


    DisplayMetrics dm;
    int display_width;
    int display_height;
    int screenSizeType;
    int resourceId;

    public int first_line_getY;
    public int maximum_id = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_dev_layout);

        DB_buttons = new int[2000][6];
        algorithm_continuous = new int[2000];



        dm = getApplicationContext().getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;
        screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
        resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");




        dev_layout_main = (RelativeLayout) findViewById(R.id.dev_layout_main);

        scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
        first_line = (LinearLayout) findViewById(R.id.first_line);

        /* 배경 줄 넣기         */
        languageColors =  new LanguageColors();
        for(int i =0; i<20 ; i++){
            LinearLayout new_linear = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
            params.gravity = Gravity.CENTER;
            new_linear.setOrientation(LinearLayout.HORIZONTAL);
            new_linear.setLayoutParams(params);
            new_linear.setBackgroundColor(Color.rgb(124,252,0));

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




        /* 기본 고정된 기능 들 배치 시키기 */
        for(int i = 0; i < 7; i++){
            LinearLayout new_Linear_layout = button_creating_method(i + 99000,i, (int) ((float)display_width*3/5), (i-1)*80 +50, false);
            LinearLayout new_Linear_layout2 = button_creating_method(i,i, (int) ((float)display_width*3/5), (i-1)*80 +50, true);
        }

        //테스트용 db
//        for(int i=0; i<7; i++){
//            id_numbering[i] = (i+1)*100 +i+1;
//            Log.i("id number", id_numbering[i]+"");
//            if(i !=0 ) {
//                id_previous_next_operation[i][0] = (i) * 100 + i; //이전 것 id 넣기
//                ViewGroup previous_layout = findViewById(id_previous_next_operation[i][0]);
//                id_numbering_location[i][0] = id_numbering_location[i-1][0]; //x 위치는 동일하게
//                id_numbering_location[i][1] = id_numbering_location[i-1][1] + 100; //y위치는 높이만큼 더해서
//            }
//
//            if(i !=6 ) {
//                id_previous_next_operation[i][1] = (i + 2) * 100 + i + 2; //다음 것 id 넣기
//            }
//        }



        /* 데이터 불러와서 배치하기 */
        for(int i = 10; i < 17; i++){
            DB_buttons[i][0] = (i+1)%10; //버튼 종류
            DB_buttons[i][1] = i+1; //다음 연속된 버튼 id
            DB_buttons[i][2] = 10; //x위치
            DB_buttons[i][3] = i*100; //y위치

            LinearLayout new_Linear_layout = button_creating_method(i, DB_buttons[i][0], DB_buttons[i][2], DB_buttons[i][3], true);
            algorithm_continuous[i-10] = i; //알고리즘 순서
            Log.i("algorithm_continuous "+(i-10), i+"");
            maximum_id = i; //마지막 id 번호
        }
        DB_buttons[0][0] = 1;
        DB_buttons[0][1] = 10;
        DB_buttons[0][2] = 10;
        DB_buttons[0][3] = 100;
        algorithm_continuous[0] = 10;
//        LinearLayout new_Linear_layout = button_creating_method(0, DB_buttons[0][0], DB_buttons[0][2], DB_buttons[0][3], true);
//        final int temp = DB_buttons[0][1];
//        new_Linear_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),temp+"", Toast.LENGTH_LONG).show();
//            }
//        });
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
        layout_placement_by_next_id(0, 10, display_height - getStatusBarHeight() - (display_height - 100) - scrollview1.getScrollY());
        Log.i("first_line_getY", first_line_getY+"");
    }

    public void arranging_algorithm_continuous_from_layout_location(int touched_id){

        int temp_array_num=0;
        try{
            ViewGroup layout1 = findViewById(touched_id);
            if(layout1.getX() < convertPixelsToDp((float)display_width/2, this)) { //화면 중간을 넘어가면 배치 안하게
                push_id_next_line(touched_id, (-first_line_getY + (int) layout1.getY()+50) / 100 - 1);
                algorithm_continuous[(-first_line_getY + (int) layout1.getY()+50) / 100 - 1] = touched_id;
                Log.i("algorithm_continuous", (-first_line_getY + (int) layout1.getY()+50) / 100 - 1+ "에 id가 " + touched_id);

            }else{
                delete_void_or_double_id(touched_id);

            }
            auto_lining();
        }catch (Exception e){
            Log.e("arranging_algorithm", e+"");
        }

    }

    public boolean push_id_next_line(int touch_id, int pre_located_id){
        try{
            delete_void_or_double_id(touch_id);
            int[] temp_algorithm_contiuous = new int[2000];

            for(int i=pre_located_id; i < algorithm_continuous.length-2;i++){
                temp_algorithm_contiuous[i] = algorithm_continuous[i];
                if(algorithm_continuous[i] == 0)
                    break;
            }
            for(int i=pre_located_id; i < algorithm_continuous.length-2;i++){
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






    /**버튼 자동으로 생성시 백그라운드를 쉽게 설정하기 위한 함수
     *
     * @param id_number
     * @return
     */
    private void select_background_img(ImageView imageView, int id_number){


        switch (id_number){
            case 1 :
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor1_speed_btn));
                break;
            case 2 :
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor2_speed_btn));
                break;
            case 3 :
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor12_stop_btn));
                break;
            case 4 :
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.servo_motor_angle_btn));
                break;
            case 5 :
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.distance_value));
                break;
            case 6 :
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.delay_btn));
                break;
            case 7 :
//                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.motor1_speed_btn));
                break;
        }

    }










}
