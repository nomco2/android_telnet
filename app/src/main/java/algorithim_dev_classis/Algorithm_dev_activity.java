package algorithim_dev_classis;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.kimfamily.arduino_car_nodemcu.R;
import movable_classis.Movable_Layout_Class;
import movable_classis.Movable_Layout_Class_auto_lineup;

/**
 * Created by KimFamily on 2017-10-25.
 */




public class Algorithm_dev_activity extends Activity implements View.OnClickListener {

    private ImageButton motor1_speed_btn, motor2_speed_btn, motor12_stop_btn, servo_motor_angle_btn, distance_value, delay_btn;
//    Shared_preference_for_saving_array_class shared_preference_for_saving_array_class;

    private RelativeLayout dev_layout_main;
    private int creating_button_id_number = 100;
    private int[][][] id_numbering_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_dev_layout);

        dev_layout_main = (RelativeLayout) findViewById(R.id.dev_layout_main);


        id_numbering_location = new int[20][100][2]; //[id번호], [그 번호에 생성된 번호 개수], [x,y주소]

        /* 기본 고정된 기능 들 배치 시키기 */
        for(int i = 0; i < 7; i++){
//            LinearLayout new_Linear_layout = button_creating_method(i+0, 10, i*100, true);
//            id_numbering_location[i][0][0] = (int) new_Linear_layout.getX();
//            id_numbering_location[i][0][1] = (int) new_Linear_layout.getY();
            id_numbering_location[i][0][1] = i+100;

        }

        Auto_lineup_and_dont_overaping auto_lineup_and_dont_overaping = new Auto_lineup_and_dont_overaping();
        auto_lineup_and_dont_overaping.setDaemon(true);
        auto_lineup_and_dont_overaping.start();
    }

    /***************oncreate 끝 ********************/


    class Auto_lineup_and_dont_overaping extends Thread{
        @Override
        public void run() {
            int[] find_arrayment_by_location_y = new int[2000];
            for(int i=0; i<20; i++){
                for(int j=0; j<100; j++){
                    if(id_numbering_location[i][j][1] > 10){ //빈값이 아니고
                        /* i+j는 button_creating_method로 동적으로 만들때 부여하는 id 번호
                         * 그래서 find_arrayment_by_location_y배열 1번은 y위치가 100인 레이아웃 id를 넣음
                         * 최종적으로 find_arrayment_by_location_y에 y순서에 따라 id를 넣어놓음
                         */
                        find_arrayment_by_location_y[id_numbering_location[i][j][1]/100] = i+j;
                        /* 불러온 id_numbering_location 값을
                         * button_creating_method로 배치
                         */
                        LinearLayout new_Linear_layout = button_creating_method(i+j, 10, id_numbering_location[i][j][1], true);
                    }
                }
            }

            while(!this.isInterrupted()) {
                //사용자가 새로운 레이아웃을 끌어댕길때 y값을 잡아서 사이에 넣을수 있도록 해주는 쓰레드


            }

        }
    }


    Handler Auto_lineup_and_dont_overaping_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    break;
            }
        }
    };






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


    private LinearLayout button_creating_method(int id_numbers, int location_x, int location_y, Boolean moving_hold_permanently){

        int this_layout_id_number = id_numbers;


        LinearLayout new_linear = new LinearLayout(getApplicationContext());
        new_linear.setId(this_layout_id_number);


        ImageView new_buttons = new ImageView(getApplicationContext());
//        new_buttons.setId(this_layout_id_number);
        select_background_img(new_buttons, this_layout_id_number); //백그라운드 레이아웃 id 번호에 따라서 알아서 배치
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
        Movable_Layout_Class_auto_lineup new_movable_button =
                new Movable_Layout_Class_auto_lineup(getApplicationContext(), dev_layout_main, new_linear, new_buttons_location, scale_size, moving_hold_permanently);

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


        switch (id_number%100){
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
