package algorithim_dev_classis;

import android.graphics.Color;

import java.util.HashMap;

public class LanguageColors {

    private HashMap<String, String> colorMap;

    public LanguageColors() {

        colorMap = new HashMap<>();

        setColors();
    }

    private void setColors() {

        colorMap.put("java", "#b07219");
        colorMap.put("objective-c", "#438eff");
        colorMap.put("swift", "#ffac45");
        colorMap.put("groovy", "#e69f56");
        colorMap.put("python", "#3572A5");
        colorMap.put("ruby", "#701516");
        colorMap.put("c", "#555555");

        colorMap.put("nomal0", "#777777");
        colorMap.put("nomal1", "#444444");
        colorMap.put("nomal2", "#CCFFCC");

        colorMap.put("if0", "#00FFFF");
        colorMap.put("if1", "#00BFFF");
        colorMap.put("if2", "#1E90FF");
        colorMap.put("if3", "#0000FF");
        colorMap.put("if4", "#000080");

        colorMap.put("for0", "#00FF7F");
        colorMap.put("for1", "#7CFC00");
        colorMap.put("for2", "#32CD32");
        colorMap.put("for3", "#3CB371");
        colorMap.put("for4", "#228B22");
    }


    private void setColors_line_color_custum(int R, int G, int B) {

        String color = "#";
        if(R<10) {
            color += "0" + R;
        }else if(R<100){
            color += R;
        }else{
            color += "00";
        }

        if(G<10) {
            color += "0" + G;
        }else if(G<100){
            color += G;
        }else{
            color += "00";
        }

        if(B<10) {
            color += "0" + B;
        }else if(B<100){
            color += B;
        }else{
            color += "00";
        }
        colorMap.put("if_button_line", color);



    }


    public int getColor(String language) {

        return Color.parseColor(colorMap.get(language));
    }
}
