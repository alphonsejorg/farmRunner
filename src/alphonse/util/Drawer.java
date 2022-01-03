package alphonse.util;

import org.powbot.api.Color;
import org.powbot.api.Point;
import org.powbot.mobile.drawing.Graphics;

import java.util.List;

public class Drawer {

    public static void drawString(Graphics g, Point point, String string) {
        int color = g.getColor();
        g.setColor(Color.getBLACK_A());
        g.fillRect(point.getX(), point.getY() - (int) g.getTextSize() + 2, (int) g.getTextWidth(string) / 2, (int) g.getTextSize());
        g.setColor(color);
        g.drawString(string, point.getX(), point.getY());
    }

    public static void drawStrings(Graphics g, Point point, List<String> strings) {
        int i = 0;
        for (String string : strings) {
            drawString(g, new Point(point.getX(), point.getY() + (i++ * (int)g.getTextSize())), string);
        }
    }
}
