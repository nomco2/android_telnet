package algorithim_dev_classis;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json_sharedpreference {
    public json_string_class[] json_saver = new json_string_class[50];
    public StringBuilder for_saving_stringbuilder;

    SharedPreferences location_savaer;
    SharedPreferences.Editor location_xy_editor;
    String save_shared_data_name;
    Context from_activity_context;


    //초기화 하기
    public Json_sharedpreference(Context from_context, String save_string_name){
        for (int i = 0; i < 10; i++) {
            json_saver[i] = new json_string_class();

        }


        location_savaer = PreferenceManager.getDefaultSharedPreferences(from_context);
        location_xy_editor = location_savaer.edit();
        save_shared_data_name = save_string_name;

        for_saving_stringbuilder = new StringBuilder();
//        loading_button_data();
        from_activity_context = from_context;



    }


    //sharedpreference로 저장하기
    public boolean save_json_to_sharedpreference(StringBuilder save_string_builder) {
        try{
            location_xy_editor.remove(save_shared_data_name);
            location_xy_editor.putString(save_shared_data_name, save_string_builder.toString());
            location_xy_editor.commit();
            return true;
        }catch (Exception e){
            return false;
        }


    }

    //변수를 json 형태로 변환 시켜 주기
    public String json_adder(String btn_name, float x_location, float y_location, String coding_contents){
        String return_string = "";
        return_string += "{\"btn\":\"" + btn_name + "\",\"x\":" + x_location +",\"y\":" + y_location + ",\"code\":\""+ coding_contents + "\"}";

//        return_string += "{\"btn\" : \"first btn\", \"x\":100, \"y\":200, \"code\": \"code1\" }";
        return return_string;
    }


    //불러오기 : 저장된 string을 클래스 데이터 형태로 가져오기
    public json_string_class[] convert_json_to_data_class(){
        try{

            String json_string = location_savaer.getString(save_shared_data_name, "");
            Log.i("json string", json_string);

            //리턴할 클래스 초기화
            json_string_class[] return_class = new json_string_class[50];
            for (int i = 0; i < 50; i++) {
                return_class[i] = new json_string_class();
            }


            //json에서 데이터 추출
            String result = new String();
            JSONArray ja = new JSONArray(json_string);
            for (int i = 0; i < ja.length(); i++){
                JSONObject order = ja.getJSONObject(i);

                return_class[i].btn_name = order.getString("btn");
                return_class[i].x_location = (float) order.getInt("x");
                return_class[i].y_location = (float) order.getInt("y");
                return_class[i].coding_contents = order.getString("code");
                Log.i(i + " return class:", return_class[i].btn_name);

                ((Button_allocate)from_activity_context).button_creation_method(return_class[i].btn_name, (int)return_class[i].x_location, (int)return_class[i].y_location);

            }
            Log.i("result:", result);
            ((Button_allocate)from_activity_context).button_counter = ja.length() - 1;

            return return_class;

        }
        catch (JSONException e){
            Log.e("result err", e+"");
            return null;
        }
    }


    public boolean saving_button_data(){
        try {
            location_xy_editor.putString(save_shared_data_name, for_saving_stringbuilder.toString());
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public Boolean loading_button_data(){
        try {
            for_saving_stringbuilder = new StringBuilder(location_savaer.getString(save_shared_data_name,""));

            return true;
        }catch (Exception e){
            return false;
        }
    }




    public class json_string_class{
        public String btn_name;
        public float x_location;
        public float y_location;
        public String coding_contents;

    }


}
