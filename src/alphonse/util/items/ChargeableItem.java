package alphonse.util.items;

import alphonse.scripts.Script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ChargeableItem {
    GAMES_NECKLACE("Games necklace", Stream.of(new Integer[][] {
            {3867, 1},
            {3865, 2},
            {3863, 3},
            {3861, 4},
            {3859, 5},
            {3857, 6},
            {3855, 7},
            {3853, 8},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),

    RING_OF_DUELING("Ring of dueling", Stream.of(new Integer[][] {
            {2566, 1},
            {2564, 2},
            {2562, 3},
            {2560, 4},
            {2558, 5},
            {2556, 6},
            {2554, 7},
            {2552, 8},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    COMBAT_BRACELET("Combat bracelet", Stream.of(new Integer[][] {
            {11124, 1},
            {11122, 2},
            {11120, 3},
            {11118, 4},
            {11974, 5},
            {11972, 6},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    SKILLS_NECKLACE("Skills necklace", Stream.of(new Integer[][] {
            {11111, 1},
            {11109, 2},
            {11107, 3},
            {11105, 4},
            {11970, 5},
            {11968, 6},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    AMULET_OF_GLORY("Amulet of glory", Stream.of(new Integer[][] {
            {1706, 1},
            {1708, 2},
            {1710, 3},
            {1712, 4},
            {11976, 5},
            {11978, 6},
            {19707, Integer.MAX_VALUE}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    RING_OF_WEALTH("Ring of wealth ", Stream.of(new Integer[][] {
            {11988, 1},
            {11986, 2},
            {11984, 3},
            {11982, 4},
            {11980, 5},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    SLAYER_RING("Slayer ring", Stream.of(new Integer[][] {
            {11873, 1},
            {11872, 2},
            {11871, 3},
            {11870, 4},
            {11869, 5},
            {11868, 6},
            {11867, 7},
            {11866, 8},
            {21268, Integer.MAX_VALUE},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    DIGSITE_PENDANT("Digsite pendant", Stream.of(new Integer[][] {
            {11190, 1},
            {11191, 2},
            {11192, 3},
            {11193, 4},
            {11194, 5},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    NECKLACE_OF_PASSAGE("Necklace of passage", Stream.of(new Integer[][] {
            {21155, 1},
            {21153, 2},
            {21151, 3},
            {21149, 4},
            {21146, 5},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    BURNING_AMULET("Burning amulet", Stream.of(new Integer[][] {
            {21175, 1},
            {21173, 2},
            {21171, 3},
            {21169, 4},
            {21166, 5},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    CRYSTAL_TELEPORT("Teleport crystal", Stream.of(new Integer[][] {
            {6102, 1},
            {6101, 2},
            {6100, 3},
            {6099, 4},
            {13102, 5},
            {23946, Integer.MAX_VALUE},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    SEED_POD("seed pod", Stream.of(new Integer[][] {
            {9469, 1},
            {19564, Integer.MAX_VALUE},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]))),
    XERICS_TALISMAN("Xeric's talisman", Stream.of(new Integer[][] {
            {13393, 1},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1])));

    private String name;
    private Map<Integer, Integer> chargesMap;

    ChargeableItem(String name, Map<Integer,Integer> chargesMap) {
        this.chargesMap = chargesMap;
        this.name = name;
    }

    public int[] getIds() {
        int[] ids = new int[chargesMap.size()];
        int j = 0;
        for (Integer i : chargesMap.keySet()) {
            ids[j++] = i;
        }
        return ids;
    }

    public String parseName(int id) {
        return name + "(" + this.chargesMap.get(id) + ")";
    }

    public Items getItemsWithAtLeastXCharges(int charges) {
        Items items = new Items("items with at least " + charges + " charges");
        for (Map.Entry<Integer, Integer> set : this.getChargesMap().entrySet()) {
            if (set.getValue() >= charges) {
                items.addItem(new Item(set.getKey(), parseName(set.getKey()), 1));
            }
        }
        return items;
    }

    public String getName() {
        return name;
    }

    public Item getMaxChargedItem() {
        int id;
        switch (this) {
            case NECKLACE_OF_PASSAGE:
                id = 21146;
                return new Item(id, parseName(id));

            case BURNING_AMULET:
                id = 21166;
                return new Item(id, parseName(id));

            case DIGSITE_PENDANT:
                id = 11194;
                return new Item(id, parseName(id));

            case SLAYER_RING:
                id = 11866;
                return new Item(id, parseName(id));

            case SKILLS_NECKLACE:
                id = 11968;
                return new Item(id, parseName(id));

            case RING_OF_DUELING:
                id = 2552;
                return new Item(id, parseName(id));

            case COMBAT_BRACELET:
                id = 11972;
                return new Item(id, parseName(id));

            case AMULET_OF_GLORY:
                id = 11978;
                return new Item(id, parseName(id));

            case GAMES_NECKLACE:
                id = 3853;
                return new Item(id, parseName(id));

            case RING_OF_WEALTH:
                id = 11980;
                return new Item(id, parseName(id));

            default:
                Script.stopScript("ChargeableItem#getMaxChargedItem did not recognize: " + this);
                return new Item(-1);
        }
    }

    public Map<Integer, Integer> getChargesMap() {
        return chargesMap;
    }

    public Item getItemWithCharges(int charges) {
        for (Map.Entry<Integer, Integer> set : this.getChargesMap().entrySet()) {
            if (set.getValue() == charges) {
                return new Item(set.getKey(), parseName(set.getKey()), 1);
            }
        }
        return new Item(-1);
    }
}
