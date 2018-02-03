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

        colorMap.put("if0", "#E8F5E9");
        colorMap.put("if1", "#C8E6C9");
        colorMap.put("if2", "#A5D6A7");
        colorMap.put("if3", "#81C784");
        colorMap.put("if4", "#66BB6A");
        colorMap.put("if5", "#4CAF50");
        colorMap.put("if6", "#43A047");
        colorMap.put("if7", "#388E3C");
        colorMap.put("if8", "#2E7D32");
        colorMap.put("if9", "#1B5E20");
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
