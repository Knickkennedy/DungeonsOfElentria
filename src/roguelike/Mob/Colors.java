package roguelike.mob;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by knich on 7/10/2017.
 */
public class Colors {
    private static HashMap <String, Color> colorLibrary = new HashMap<>();

    public static void initializeColors(){
        colorLibrary.put("bright green", new Color(0, 185, 0));
        colorLibrary.put("blue", new Color(0, 0, 160));
        colorLibrary.put("dark green", new Color(0, 90, 0));
        colorLibrary.put("brown", new Color(102, 51, 0));
        colorLibrary.put("gray", new Color(130, 130, 130));
        colorLibrary.put("light gray", new Color(190, 190, 190));
        colorLibrary.put("dark gray", new Color(60, 60, 60));
        colorLibrary.put("white", new Color(255, 255, 255));
        colorLibrary.put("magenta", new Color(190, 0, 190));
        colorLibrary.put("bright magenta", new Color(255, 0, 255));
        colorLibrary.put("dark magenta", new Color(128, 0, 128));
    }

    public static Color getColor(String color){
        return colorLibrary.get(color);
    }

    public static HashMap<String, Color> getColorLibrary() {
        return colorLibrary;
    }
}
