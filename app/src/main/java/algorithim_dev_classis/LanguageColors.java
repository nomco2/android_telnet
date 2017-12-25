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

        colorMap.put("nomal0", "#EEFFEE");
        colorMap.put("nomal1", "#DDFFDD");
        colorMap.put("nomal2", "#CCFFCC");
        colorMap.put("if", "#FFCCCC");
        colorMap.put("for", "#CCCCFF");


    }


    public int getColor(String language) {

        return Color.parseColor(colorMap.get(language));
    }
}
