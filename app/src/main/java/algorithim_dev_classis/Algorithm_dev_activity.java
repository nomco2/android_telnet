package algorithim_dev_classis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
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


    private ScrollView scrollview1;
    private LinearLayout first_line;
    private int previous_scroll_value;

    LanguageColors languageColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_dev_layout);

        dev_layout_main = (RelativeLayout) findViewById(R.id.dev_layout_main);

        scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
        first_line = (LinearLayout) findViewById(R.id.first_line);

        /* 배경 줄 넣기         */
        languageColors =  new LanguageColors();
        for(int i =0; i<20 ; i++){
            final ImageView new_line = new ImageView(getApplicationContext());
            new_line.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
            new_line.setImageDrawable(getResources().getDrawable(R.drawable.line_image));
            new_line.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.myshape));
            new_line.setId(90000 + i);

            new_line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),new_line.getY()+ " : " + scrollview1.getScrollY()+"", Toast.LENGTH_LONG).show();
                }
            });

            first_line.addView(new_line);
            GradientDrawable bgShape = (GradientDrawable) new_line.getBackground();
            String selectedLanguage = "nomal" + i%3;
            bgShape.setColor(languageColors.getColor(selectedLanguage));

        }





        DB_buttons = new int[4000][6];


        /* 기본 고정된 기능 들 배치 시키기 */
        for(int i = 0; i < 7; i++){
            LinearLayout new_Linear_layout = button_creating_method(i,i, 300, i*100, false);
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
            DB_buttons[i][0] = 1; //버튼 종류
            DB_buttons[i][1] = i+1; //다음 연속된 버튼 id
            DB_buttons[i][2] = 10; //x위치
            DB_buttons[i][3] = i*100; //y위치

            final LinearLayout new_Linear_layout = button_creating_method(i, DB_buttons[i][0], DB_buttons[i][2], DB_buttons[i][3], true);
            final int temp = DB_buttons[i][1];
            new_Linear_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),temp+"", Toast.LENGTH_LONG).show();
                }
            });
        }
        DB_buttons[0][0] = 1;
        DB_buttons[0][1] = 10;
        DB_buttons[0][2] = 10;
        DB_buttons[0][3] = 100;
        LinearLayout new_Linear_layout = button_creating_method(0, DB_buttons[0][0], DB_buttons[0][2], DB_buttons[0][3], true);
        final int temp = DB_buttons[0][1];
        new_Linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),temp+"", Toast.LENGTH_LONG).show();
            }
        });


        /*쓰레드*/
        Auto_lineup_and_dont_overaping auto_lineup_and_dont_overaping = new Auto_lineup_and_dont_overaping();
        auto_lineup_and_dont_overaping.setDaemon(true);
        auto_lineup_and_dont_overaping.start();
    }

    /***************oncreate 끝 ********************/


    class Auto_lineup_and_dont_overaping extends Thread{
        @Override
        public void run() {
//            int[] find_arrayment_by_location_y = new int[2000];
//            for(int i=0; i<20; i++){
//                for(int j=0; j<100; j++){
//                    if(id_numbering_location[i][j][1] > 10){ //빈값이 아니고
//                        /* i+j는 button_creating_method로 동적으로 만들때 부여하는 id 번호
//                         * 그래서 find_arrayment_by_location_y배열 1번은 y위치가 100인 레이아웃 id를 넣음
//                         * 최종적으로 find_arrayment_by_location_y에 y순서에 따라 id를 넣어놓음
//                         */
//                        find_arrayment_by_location_y[id_numbering_location[i][j][1]/100] = i+j;
//                        /* 불러온 id_numbering_location 값을
//                         * button_creating_method로 배치
//                         */
//                        LinearLayout new_Linear_layout = button_creating_method(i+j, 10, id_numbering_location[i][j][1], true);
//                    }
//                }
//            }

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
                    DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                    int display_width = dm.widthPixels;
                    int display_height = dm.heightPixels;

                    ViewGroup linerlayout1 = findViewById(0); //시작되는 레이아웃 id = 0
                    try {
                        linerlayout1.setY(display_height - getStatusBarHeight() - DB_buttons[0][3] - scrollview1.getScrollY());
                    }catch(Exception e){

                    }
                    layout_placement_by_next_id(DB_buttons[0][1], 10, 100);
//                        try {
//                            if(DB_buttons[i][1] >1) { //이전 값이 있으면
//                                ViewGroup previous_layout = findViewById(DB_buttons[i][1]);
////                                linerlayout1.setY(previous_location[i][1] + previous_layout.getHeight() - convertPixelsToDp(scrollview1.getScrollY(), getApplicationContext()));
////                                linerlayout1.setY((float) (new_line.getY() - scrollview1.getScrollY()));
//                                linerlayout1.setY((float) (display_height-getStatusBarHeight() - previous_location[i][1] - scrollview1.getScrollY()));
//
//
////                                Log.i("이전y", previous_layout.getY()+"");
////                                Log.i("이전 높이", previous_layout.getHeight()+"");
////                                Log.i("set y", previous_layout.getY() + convertPixelsToDp(previous_layout.getHeight(),getApplicationContext()) - convertPixelsToDp(scrollview1.getScrollY(), getApplicationContext())+"");
//                                Log.i(" pre id num : ",id_previous_next_operation[i][0]+" height = "+ previous_layout.getHeight());
//                            }else{ //이전 값이 없으면
//                                linerlayout1.setY(id_numbering_location[i][1] - convertPixelsToDp(scrollview1.getScrollY(), getApplicationContext()));
////                                linerlayout1.setVisibility(View.INVISIBLE);
//                                Log.i(" first : ",i+"");
//                            }
//
//                        }catch (Exception e){
//
//                        }

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
        int screenSizeType = (getApplicationContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);

        if(screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");

            if (resourceId > 0) {
                statusHeight = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusHeight;
    }

    private boolean layout_placement_by_next_id(int next_id_number, int pre_layout_x, int pre_layout_y){
        Log.i("db buttons : ", next_id_number +"");

        ViewGroup layout1 = findViewById(next_id_number);
        layout1.setX(pre_layout_x);
        layout1.setY(pre_layout_y+100);
        try {
            if (DB_buttons[next_id_number][1] > 0) { //다음 연속된 id가 저장되어 있으면
                layout_placement_by_next_id(DB_buttons[next_id_number][1], (int) layout1.getX(), (int) layout1.getY());
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        Movable_Layout_Class new_movable_button =
                new Movable_Layout_Class(getApplicationContext(), dev_layout_main, new_linear, new_buttons_location, scale_size, moving_hold_permanently);

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
