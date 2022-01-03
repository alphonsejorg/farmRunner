package alphonse.util;

import org.powbot.api.Area;
import org.powbot.api.Locatable;
import org.powbot.api.Tile;

public class AreaUtil {

    public static boolean contains(Area area, Locatable locatable) {
        if (locatable == null || locatable.tile().equals(Tile.getNil())) {
            return false;
        }
        if (locatable.tile().x() >= area.get_tiles()[0].x() &&
                locatable.tile().y() >= area.get_tiles()[0].y()) {
            return locatable.tile().x() < area.get_tiles()[2].x() &&
                    locatable.tile().y() < area.get_tiles()[2].y();
        }
        return false;
    }
}
