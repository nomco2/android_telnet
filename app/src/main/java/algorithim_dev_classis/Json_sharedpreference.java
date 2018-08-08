package algorithim_dev_classis;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Json_sharedpreference {
//    public json_string_class[] json_saver = new json_string_class[10];
    public StringBuilder for_saving_stringbuilder;
    ArrayList<json_string_class> json_saver;


    SharedPreferences location_savaer;
    SharedPreferences.Editor location_xy_editor;
    String save_shared_data_name;
    Context original_activity_context;


    //초기화 하기
    public Json_sharedpreference(Context from_context, String save_string_name){

        json_saver = new ArrayList<>();

        location_savaer = PreferenceManager.getDefaultSharedPreferences(from_context);
        location_xy_editor = location_savaer.edit();
        save_shared_data_name = save_string_name;

        for_saving_stringbuilder = new StringBuilder();
        original_activity_context = from_context;




    }




    public boolean add_json_arraylist(String btn_name, float x_location, float y_location, String coding_contents){
        try{
            json_string_class test_class = new json_string_class() ;
            test_class.btn_name = btn_name;
            test_class.x_location = x_location;
            test_class.y_location = y_location;
            test_class.coding_contents = coding_contents;

            json_saver.add(test_class);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    //sharedpreference로 저장하기
    public void save_json_to_sharedpreference() {

        JSONObject obj = new JSONObject();
        try {
            JSONArray jArray = new JSONArray();//배열이 필요할때
            for (int i = 0; i < json_saver.size(); i++)//배열
            {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("btn", json_saver.get(i).btn_name);
                sObject.put("x", json_saver.get(i).x_location);
                sObject.put("y", json_saver.get(i).y_location);
                sObject.put("code", json_saver.get(i).coding_contents);
                jArray.put(sObject);
            }
            obj.put("explain", "planA");
            obj.put("id", "userID");
            obj.put("date", "20180808");
            obj.put("item", jArray);//배열을 넣음

            System.out.println(obj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }




//        try{
            location_xy_editor.putString(save_shared_data_name, obj.toString());
            location_xy_editor.commit();
//            return true;
//        }catch (Exception e){
//            return false;
//        }


    }

    //변수를 json 형태로 변환 시켜 주기
    public String json_adder(String btn_name, float x_location, float y_location, String coding_contents){
        String return_string = "";
        return_string += "{\"btn\":\"" + btn_name + "\",\"x\":" + x_location +",\"y\":" + y_location + ",\"code\":\""+ coding_contents + "\"}";

//        return_string += "{\"btn\" : \"first btn\", \"x\":100, \"y\":200, \"code\": \"code1\" }";
        return return_string;
    }


    //불러오기 : 저장된 string을 클래스 데이터 형태로 가져오기
    public ArrayList<json_string_class> convert_json_to_data_class(){
        try{

            String json_string = location_savaer.getString(save_shared_data_name, "");

            Log.i("json string", json_string);






            String id;
            String explain;
            String date;
            String item = "";
            //json에서 데이터 추출
            JSONArray ja = new JSONArray(json_string);
            JSONObject order = ja.getJSONObject(0);

            id = order.getString("id");
            explain =  order.getString("explain");
            date = order.getString("date");
            item = order.getString("item");



            ArrayList<json_string_class> return_class = new ArrayList<>();

            JSONArray ja2 = new JSONArray(item);
            for (int i = 0; i < ja2.length(); i++){
                JSONObject order2 = ja2.getJSONObject(i);
//                result += "btn: " + order.getString("btn") + ", x_location: " + order.getString("x") +
//                        ", y_location: " + order.getInt("y") + ", code: " + order.getString("code") + "\n";
                return_class.get(i).btn_name = order2.getString("btn");
                return_class.get(i).x_location = (float) order2.getInt("x");
                return_class.get(i).y_location = (float) order2.getInt("y");
                return_class.get(i).coding_contents = order2.getString("code");

                //바로 json arrrary 추가 및 버튼 만들기
                add_json_arraylist(return_class.get(i).btn_name, return_class.get(i).x_location, return_class.get(i).y_location, return_class.get(i).coding_contents);
                ((Button_allocate)original_activity_context).button_creation_method(return_class.get(i).btn_name, (int) return_class.get(i).x_location, (int)return_class.get(i).y_location);
            }


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
