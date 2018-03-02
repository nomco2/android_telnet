package algorithim_dev_classis;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by KimFamily on 2018-03-02.
 */

public class Resolution_setting {
    int display_width;
    int display_height;
    public Resolution_setting(DisplayMetrics dm, Context pre_activity_context){
        display_width = dm.widthPixels;
        display_height = dm.heightPixels;

        int text_size;
        int buttons_height;
        if(display_height < 1000){ //HD
//            buttons_height = (int)convertPixelsToDp((display_height/4), getApplicationContext());
//            text_size = buttons_height/2;
            buttons_height = (int) convertPixelsToDp(display_height, pre_activity_context) / 6;
            text_size =  (int) (buttons_height/1.2);

        }else if(display_height < 1400){ //FHD
            buttons_height = (int) convertPixelsToDp(display_height, pre_activity_context) / 4;
            text_size = (int) (buttons_height/1.7);

        }else if(display_height < 2000){ //QHD
            buttons_height = (int) convertPixelsToDp(display_height, pre_activity_context) / 3;
            text_size = (int) (buttons_height/2.3);

        }else{ //UHD
            buttons_height = (int) convertPixelsToDp(display_height, pre_activity_context) / 2;
            text_size = (int) (buttons_height/2.6);
        }


    }
    public int text_size(){
        return text_size();
    }
    public int button_height(){
        return button_height();
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
