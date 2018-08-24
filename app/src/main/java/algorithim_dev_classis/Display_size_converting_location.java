package algorithim_dev_classis;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

//디스플레이 크기에 따른 버튼 크기와 배치 위치를 지정 하기 위한 클래스
class Display_size_converting_location{
    DisplayMetrics dm;
    public int display_width;
    int display_height;
    int screenSizeType;
    int resourceId;
    public int buttons_height;
    public float text_size;
    public int line_size;

    public Display_size_converting_location(Context from_context){

        dm = from_context.getResources().getDisplayMetrics();
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;
        screenSizeType = (from_context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK);
        resourceId = from_context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        line_size = (int) convertPixelsToDp(display_height/6, from_context);




        if(display_height < 1000){ //HD
//            buttons_height = (int)convertPixelsToDp((display_height/4), getApplicationContext());
//            text_size = buttons_height/2;
            buttons_height = (int) convertPixelsToDp(display_height, from_context) / 6;
            text_size =  (int) (buttons_height/1.2);

        }else if(display_height < 1400){ //FHD
            buttons_height = (int) convertPixelsToDp(display_height, from_context) / 4;
            text_size = (int) (buttons_height/1.7);

        }else if(display_height < 2000){ //QHD
            buttons_height = (int) convertPixelsToDp(display_height, from_context) / 3;
            text_size = (int) (buttons_height/2.3);


        }else{ //UHD

        }

    }



    public float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }


}