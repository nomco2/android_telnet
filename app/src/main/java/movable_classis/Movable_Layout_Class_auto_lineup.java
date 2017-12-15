package movable_classis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static java.lang.Math.round;

/**
 * Created by IGSksh on 2017-07-29.
 */

public class Movable_Layout_Class_auto_lineup {
    public int xDelta;
    public int yDelta;
    private ViewGroup mainLayout;
    private ViewGroup mframe;
    private String[] mloaction_xy;
    public boolean is_movable_hold_temporary = true;
    private boolean is_movable_hold_permanent = true;


    Context mainactivity_context;
    SharedPreferences location_savaer;
    SharedPreferences.Editor location_xy_editor;

    /* 스케일 사이즈 조절 변수 */
    boolean UI_scale_size_up_down = true;
    String present_scale_value_name;





    /**

     * "Movable_Layout_Class" want 4 parameters
     * @param from_mainAcviticy_context [Context] that from the viewing activity
     * @param form_mainAcitiviy_Layout [ViewGroup] viewing activity's root layout
     * @param you_want_moving_layout [ViewGroup] you want moving layout
     * @param location_xy [String[]] to save the "you_want_moving_layout" of location x, y
     */
    public Movable_Layout_Class_auto_lineup(Context from_mainAcviticy_context, ViewGroup form_mainAcitiviy_Layout, ViewGroup you_want_moving_layout, String[] location_xy){
        mainactivity_context = from_mainAcviticy_context;
        mainLayout = form_mainAcitiviy_Layout;
        mframe = you_want_moving_layout;
        mframe.bringToFront();
        mloaction_xy = new String[2];
        mloaction_xy = location_xy;

        //loading pre_location
        location_savaer = PreferenceManager.getDefaultSharedPreferences(mainactivity_context);
        location_xy_editor = location_savaer.edit();

        mframe.setX(location_savaer.getFloat(location_xy[0], 100));
        mframe.setY(location_savaer.getFloat(location_xy[1], 100));


        mframe.setOnTouchListener(onTouchListener());


    }


    /**
     * if you want to add Scale_size than input the 1 String value more
     * @param from_mainAcviticy_context [Context] that from the viewing activity
     * @param form_mainAcitiviy_Layout [ViewGroup] viewing activity's root layout
     * @param you_want_moving_layout [ViewGroup] you want moving layout
     * @param location_xy [String[]] to save the "you_want_moving_layout" of location x, y
     * @param scale_value_name the layout scale size value name : String
     */
    public Movable_Layout_Class_auto_lineup(Context from_mainAcviticy_context, ViewGroup form_mainAcitiviy_Layout, ViewGroup you_want_moving_layout,
                                            String[] location_xy, String scale_value_name, Boolean moving_hold_permanently){
        mainactivity_context = from_mainAcviticy_context;
        mainLayout = form_mainAcitiviy_Layout;
        mframe = you_want_moving_layout;
        mframe.bringToFront();
        mloaction_xy = new String[2];
        mloaction_xy = location_xy;
        present_scale_value_name = scale_value_name; //스케일 저장 변수 이름 받아서 지정

        //loading pre_location
        location_savaer = PreferenceManager.getDefaultSharedPreferences(mainactivity_context);
        location_xy_editor = location_savaer.edit();

        mframe.setX(location_savaer.getFloat(location_xy[0], 100));
        mframe.setY(location_savaer.getFloat(location_xy[1], 100));
        mframe.setScaleX(location_savaer.getFloat(present_scale_value_name, 1.0f));
        mframe.setScaleY(location_savaer.getFloat(present_scale_value_name, 1.0f));

        mframe.setOnTouchListener(onTouchListener());
    }





    public void Scale_size_adjustment(float scale_size){
        try {
            location_xy_editor.putFloat(present_scale_value_name, scale_size);
            location_xy_editor.commit();

            mframe.setScaleX(scale_size);
            mframe.setScaleY(scale_size);
        }catch (Exception e){
            Toast.makeText(mainactivity_context,"scale_size name is NULL", Toast.LENGTH_LONG).show();
        }
    }

    public float Saved_scale_size(){
        return location_savaer.getFloat(present_scale_value_name, 1.0f);
    }






    public View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();




                if(is_movable_hold_temporary) { //위치 고정
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                    view.getLayoutParams();

                            xDelta = x - lParams.leftMargin;
                            yDelta = y - lParams.topMargin;

                            break;

                        case MotionEvent.ACTION_UP:
//                        Toast.makeText(mainactivity_context,"thanks for new location!", Toast.LENGTH_SHORT).show();


//saving _xy location
                            location_savaer = PreferenceManager.getDefaultSharedPreferences(mainactivity_context);
                            location_xy_editor = location_savaer.edit();


                            location_xy_editor.putFloat(mloaction_xy[0], mframe.getX());
                            location_xy_editor.putFloat(mloaction_xy[1], mframe.getY());
                            location_xy_editor.commit();



                            RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) view
                                    .getLayoutParams();
//                            layoutParams1.leftMargin = round(x/100)*100 - xDelta;
                            layoutParams1.leftMargin = 30;
                            layoutParams1.topMargin = round(y/100)*100 - yDelta;
                            layoutParams1.rightMargin = 0;
                            layoutParams1.bottomMargin = 0;
                            view.setLayoutParams(layoutParams1);



                            break;

                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                    .getLayoutParams();
                            layoutParams.leftMargin = x - xDelta;
                            layoutParams.topMargin = y - yDelta;
                            layoutParams.rightMargin = 0;
                            layoutParams.bottomMargin = 0;
                            view.setLayoutParams(layoutParams);
                            break;
                    }
                }
                mainLayout.invalidate();
                return true;
            }
        };
    }




}
