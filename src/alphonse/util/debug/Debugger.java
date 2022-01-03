package alphonse.util.debug;

import org.powbot.mobile.drawing.Graphics;

public abstract class Debugger {
    private static boolean debugging = false;

    public abstract void draw(Graphics g);

    public static void setDebugging(boolean debugging) {
        Debugger.debugging = debugging;
    }

    public static boolean isDebugging() {
        return debugging;
    }
}
