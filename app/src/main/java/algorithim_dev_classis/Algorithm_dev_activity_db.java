package algorithim_dev_classis;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimfamily.arduino_car_nodemcu.R;

import movable_classis.Movable_Layout_Class_auto_lineup;


/**
 * Created by KimFamily on 2017-10-25.
 */




public class Algorithm_dev_activity_db extends Activity {
    private RelativeLayout dev_layout_main;
    private ScrollView scrollview1;
    private LinearLayout first_line;
    private int previous_scroll_value;

    LanguageColors languageColors;

    Display_size_converting_location mDisplay_size_converting_location;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_dev_layout);

        mDisplay_size_converting_location = new Display_size_converting_location(this);

        dev_layout_main = (RelativeLayout) findViewById(R.id.dev_layout_main);
        scrollview1 = (ScrollView) findViewById(R.id.scrollview1);
        scrollview1.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() { //스크를 이동 하면 자동으로 레이아웃 위치 바꿔야됨
            @Override
            public void onScrollChanged() {
                int scrollY = scrollview1.getScrollY(); // For ScrollView
//                int scrollX = scrollview1.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                Message msg =  layout_handler.obtainMessage();
                msg.what =100;
                layout_handler.sendMessage(msg);
                previous_scroll_value = scrollview1.getScrollY();
            }
        });

        //배경 레이아웃 그리기
        first_line = (LinearLayout) findViewById(R.id.first_line);
        //첫째 줄 시작 지점 텍스트 표기
        LinearLayout first_line_text_layout = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams temp_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mDisplay_size_converting_location.buttons_height);
        first_line_text_layout.setOrientation(LinearLayout.HORIZONTAL);
        first_line_text_layout.setLayoutParams(temp_params);
        first_line_text_layout.setBackgroundColor(Color.rgb(0,0,0));
        first_line_text_layout.setGravity(Gravity.LEFT);

        TextView first_line_text = new TextView(getApplicationContext());
        first_line_text.setText("Add code block below");
        first_line_text.setLayoutParams(new ViewGroup.LayoutParams(  (int) mDisplay_size_converting_location.line_size, ViewGroup.LayoutParams.MATCH_PARENT));
        first_line_text.setBackgroundColor(Color.rgb(0,0,0));
        first_line_text.setTextSize(1,mDisplay_size_converting_location.text_size);
        first_line_text_layout.addView(first_line_text);
        first_line.addView(first_line_text_layout);


        /* 기본 고정된 기능 들 배치 시키기 */
        for(int i = 0; i < 8; i++){
            int y_location = (i)*mDisplay_size_converting_location.buttons_height; // (i-1)*80 +50

            final RelativeLayout new_Linear_layout2 = button_creating_method2(i,i, 200,mDisplay_size_converting_location.buttons_height*i, true, 0, "");


        }


    }//oncreate 끝


    Handler layout_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 50: //레이아웃 이동 감지시


                    break;

                case 100: //스크롤 이동

                    break;

            }
        }
    };





    public RelativeLayout button_creating_method2(int id_numbers,int button_type, int location_x, int location_y, Boolean moving_hold_permanently, int algorithm_continuous_array_num, String function_data){


        RelativeLayout new_linear = new RelativeLayout(Algorithm_dev_activity_db.this);
        String[] new_buttons_location = new String[2];
        new_buttons_location[0] = "new_button_x";
        new_buttons_location[1] = "new_button_y";
        String scale_size = "scale_size";



        ImageView new_buttons = new ImageView(getApplicationContext());
//        new_buttons.setId(this_layout_id_number);
        select_background_img(new_buttons, button_type); //백그라운드 이미지를 button type 번호에 따라서 배치
        new_buttons.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView new_texts = new TextView(getApplicationContext());
//        new_texts.setTextSize(convertPixelsToDp(display_height/8, getApplicationContext()));
        new_texts.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_rectangle));
        new_texts.setTextColor(Color.rgb(0,0,0));
//        new_texts.setTextSize(1,convertPixelsToDp(75,this));
        new_texts.setTextSize(mDisplay_size_converting_location.text_size);
        select_background_text(new_linear, new_texts, button_type, id_numbers);



        Movable_Layout_Class_auto_lineup new_movable_button =
                new Movable_Layout_Class_auto_lineup(this, dev_layout_main, new_linear, new_buttons_location, scale_size, moving_hold_permanently, id_numbers);


        new_linear.addView(new_texts);
        dev_layout_main.addView(new_linear);
        new_linear.setX(location_x);
        new_linear.setY(location_y);


//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) new_linear.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,mDisplay_size_converting_location.buttons_height*2);
        layoutParams.width = mDisplay_size_converting_location.buttons_height*13;
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
        new_texts.setTextSize(1,mDisplay_size_converting_location.text_size);
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











}
