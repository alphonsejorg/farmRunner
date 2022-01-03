package alphonse.util.teleport;

import alphonse.scripts.Script;
import alphonse.util.constants.C_Items;
import alphonse.util.constants.C_Varpbits;
import alphonse.util.constants.LevelRequirements;
import alphonse.util.items.ChargeableItem;
import alphonse.util.items.Items;
import alphonse.util.items.Item;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Input;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;

import java.util.logging.Logger;

import static alphonse.util.items.ChargeableItems.chargeableItems;
import static alphonse.util.managers.TeleportManager.teleportManager;
import static alphonse.util.items.Bank.bank;

public enum Teleport {
    CASTLE_WARS("2", "2", TeleportType.JEWELLERY, new Area(new Tile(2436,3081,0), new Tile(2446,3098,0))),
    DRAYNOR_VILLAGE("p", "3", TeleportType.JEWELLERY, new Area(new Tile(3098,3244,0), new Tile(3111,3256,0))),
    FISHING_GUILD("d", "1", TeleportType.JEWELLERY, new Area(new Tile(2605,3385,0), new Tile(2621,3393,0))),
    BURTHORPE("4", "1", TeleportType.JEWELLERY, new Area(new Tile(2892, 3549, 0), new Tile(2906, 3559, 0))),
    FALADOR_PARK("l", "3", TeleportType.JEWELLERY, new Area(new Tile(2990,3370,0), new Tile(3001,3379,0))),
    GRAND_EXCHANGE("k", "2", TeleportType.JEWELLERY, new Area(new Tile(3158,3472,0), new Tile(3171,3484,0))),
    CHAMPION_GUILD("a","2", TeleportType.JEWELLERY, new Area(new Tile(3186,3362,0), new Tile(3198,3372,0))),
    DUEL_ARENA("1", "1", TeleportType.JEWELLERY, new Area(new Tile(3305, 3228, 0), new Tile(3323, 3240, 0))),
    SLAYER_RING_NIEVE("", "", TeleportType.JEWELLERY, new Area(new Tile(2424, 3418), new Tile(2441, 3433))),
    RANGING_GUILD("", "4", TeleportType.JEWELLERY, new Area(new Tile(2648, 3434), new Tile(2662, 3448))),
    OUTPOST("", "2", TeleportType.JEWELLERY, new Area(new Tile(2428, 3344), new Tile(2442, 3356))),
    WIZARDS_TOWER("", "1", TeleportType.JEWELLERY, new Area(new Tile(3112, 3173), new Tile(3117, 3187))),
    WOODCUTTING_GUILD("", "5", TeleportType.JEWELLERY, new Area(new Tile(1658, 3499), new Tile(1667, 3511))),
    FARMING_GUILD("", "6", TeleportType.JEWELLERY, new Area(new Tile(1243, 3715), new Tile(1254, 3722))),

    HOSIDIUS("2", "2", TeleportType.JEWELLERY, new Area(new Tile(1747,3553,0),new Tile(1758,3569,0))),

    CAMELOT("camelot", "", TeleportType.SPELL, new Area(new Tile(2752,3473,0), new Tile(2765,3481,0))),
    LUMBRIDGE("lumbridge","", TeleportType.SPELL, new Area(new Tile(3217,3212,0), new Tile(3230,3226,0))),
    FALADOR("falador","", TeleportType.SPELL, new Area(new Tile(2959,3376,0), new Tile(2971,3385,0))),
    VARROCK("varrock","", TeleportType.SPELL, new Area(new Tile(3207,3421,0), new Tile(3220,3434,0))),
    CANIFIS("kharyrll","", TeleportType.SPELL, new Area(new Tile(3488,3470,0), new Tile(3504,3477,0))),
    WATCHTOWER("Watchtower","", TeleportType.SPELL, new Area(new Tile(2543,3111,2), new Tile(2550,3118,2))),
    POH_RIMMINGTON("","", TeleportType.SPELL, new Area(new Tile(2946,3216,0), new Tile(2963,3230,0))),
    POH_BRIMHAVEN("","", TeleportType.SPELL, new Area(new Tile(2753, 3172), new Tile(2763, 3183))),
    ARDOUGNE("ardougne","", TeleportType.SPELL, new Area(new Tile(2658, 3299), new Tile(2669, 3314))),

    HOME_TELEPORT("", "", TeleportType.HOME_TELEPORT, new Area(new Tile(3217,3212,0), new Tile(3230,3226,0))),
    SEED_POD("", "", TeleportType.ITEM, new Area(new Tile(2462, 3486), new Tile(2470, 3500))),
    TELEPORT_CRYSTAL_LLETYA("", "", TeleportType.ITEM, new Area(new Tile(2325, 3168), new Tile(2335, 3176))),
    ECTOPHIAL("", "", TeleportType.ITEM, new Area(new Tile(3654, 3514), new Tile(3665, 3525))),
    EXPLORERS_RING("", "Teleport", TeleportType.ITEM, new Area(new Tile(3044, 3284), new Tile(3062, 3300))),
    ARDOUGNE_MONESTERY("", "Teleport", TeleportType.ITEM, new Area(new Tile(2593, 3208), new Tile(2621, 3238))),
    ;

    private String pohAction;
    private String action;
    private Area area;
    private final TeleportType type;
    private TeleportOption teleportOption;
    private boolean haveToRestock = false;
    private final static Logger LOGGER = Logger.getLogger("Teleport");

    Teleport(String pohAction, String action, TeleportType type, Area area) {
        this.pohAction = pohAction;
        this.action = action;
        this.type = type;
        this.area = area;

        if (type.equals(TeleportType.SPELL)) {
            teleportOption = TeleportOption.TAB;
        } else if (type.equals(TeleportType.JEWELLERY)) {
            teleportOption = TeleportOption.JEWELLERY;
        } else {
            teleportOption = TeleportOption.NONE;
        }
    }

    public TeleportType getType() {
        return type;
    }

    public Area getArea() {
        return area;
    }

    public String getAction() {
        return action;
    }

    public Magic.Spell getSpell() {
        switch (this) {
            case VARROCK:
                return Magic.Spell.VARROCK_TELEPORT;

            case LUMBRIDGE:
                return Magic.Spell.LUMBRIDGE_TELEPORT;

            case FALADOR:
                return Magic.Spell.FALADOR_TELEPORT;

            case POH_RIMMINGTON: case POH_BRIMHAVEN:
                return Magic.Spell.TELEPORT_TO_HOUSE;

            case ARDOUGNE:
                return Magic.Spell.ARDOUGNE_TELEPORT;

            case WATCHTOWER:
                return Magic.Spell.WATCHTOWER_TELEPORT;

            case HOME_TELEPORT:
                return Magic.Spell.HOME_TELEPORT;

            case CAMELOT:
                return Magic.Spell.CAMELOT_TELEPORT;

            default:
                Script.stopScript("Teleport#getSpell did not recognize: " + this);
                return Magic.Spell.WEAKEN;
        }
    }

    public String parseIngameName() {
        return this.name().toLowerCase().replaceAll("_", " ");
    }

    public void useJewellery() {
        ChargeableItem chargeableItem = getChargeableItem();
        if (chargeableItem == null) {
            return;
        }

        // trying to interact with equipped jewellery
        Game.tab(Game.Tab.EQUIPMENT);
        org.powbot.api.rt4.Item item = Equipment.stream().id(chargeableItem.getIds()).first();

        if (!item.equals(org.powbot.api.rt4.Item.getNil())) {
            String action = parseIngameName();
            LOGGER.info("trying to interact with equipped jewellery with action: \"" + action + "\"");
            if (item.interact(action)) {
                if (Condition.wait(() -> getArea().contains(Players.local()), 800, 6)) {
                    LOGGER.info("successfully teleported to area");
                    Game.tab(Game.Tab.INVENTORY);
                    return;
                }
            }
        }
        Game.tab(Game.Tab.INVENTORY);



        item = Inventory.stream().id(chargeableItem.getIds()).first();

        if (!item.equals(org.powbot.api.rt4.Item.getNil())) {
            LOGGER.info("rubbing " + item.name() + " in inventory...");
            if (item.interact("Rub")) {
                if (Condition.wait(() -> Components.stream(219).textContains("eleport to").isNotEmpty() ||
                        Components.stream(187).textContains("eleport to").isNotEmpty(), 900, 4)) {
                    LOGGER.info("sending input: " + this.getAction() + "...");
                    if (Input.send(this.getAction())) {
                        if (Condition.wait(() -> getArea().contains(Players.local()), 800, 6)) {
                            LOGGER.info("successfully teleported to area");
                        } else {
                            LOGGER.info("failed to teleport to area");
                        }
                    }
                }
            }
        }
    }

    public void useItem(ChargeableItem chargeableItem) {
        org.powbot.api.rt4.Item item = Inventory.stream().id(chargeableItem.getIds()).first();

        if (!item.equals(org.powbot.api.rt4.Item.getNil())) {
            if (item.click()) {
                if (!this.action.isEmpty()) {
                    if (Condition.wait(() -> Components.stream().textContains("eleport to").isNotEmpty(), 900, 4)) {
                        LOGGER.info("successfully found teleport menu");
                        if (Input.send(this.getAction())) {
                            LOGGER.info("sent input: " + this.getAction());
                        }
                    }
                }
                if (Condition.wait(() -> getArea().contains(Players.local()), 800, 6)) {
                    LOGGER.info("successfully teleported to area");
                }
            }
        }
    }

    public ChargeableItem getChargeableItem() {
        switch (this) {
            case CASTLE_WARS: case DUEL_ARENA:
                return ChargeableItem.RING_OF_DUELING;

            case DRAYNOR_VILLAGE:
                return ChargeableItem.AMULET_OF_GLORY;

            case GRAND_EXCHANGE: case FALADOR_PARK:
                return ChargeableItem.RING_OF_WEALTH;

            case FISHING_GUILD:
                return ChargeableItem.SKILLS_NECKLACE;

            case CHAMPION_GUILD: case RANGING_GUILD:
                return ChargeableItem.COMBAT_BRACELET;

            case BURTHORPE:
                return ChargeableItem.GAMES_NECKLACE;

            case SLAYER_RING_NIEVE:
                return ChargeableItem.SLAYER_RING;

            case OUTPOST:
                return ChargeableItem.NECKLACE_OF_PASSAGE;

            case HOSIDIUS:
                return ChargeableItem.XERICS_TALISMAN;

        }
        System.err.println("Teleport#getChargeableItem called when teleport type is not a chargeable item ("  + this + ")");
        return ChargeableItem.SLAYER_RING;
    }

    public boolean haveHardRequirements(boolean castSpell) {
        switch (this) {
            case ARDOUGNE_MONESTERY:
                return bank.getItems().nameContains("ardougne cloak");

            case CAMELOT:
                if (castSpell) {
                    return Skills.realLevel(Constants.SKILLS_MAGIC) >= LevelRequirements.TELEPORT_CAMELOT;
                }
                return true;

            case FALADOR:
                if (castSpell) {
                    return Skills.realLevel(Constants.SKILLS_MAGIC) >= LevelRequirements.TELEPORT_FALADOR;
                }
                return  true;

            case VARROCK:
                if (castSpell) {
                    return Skills.realLevel(Constants.SKILLS_MAGIC) >= LevelRequirements.TELEPORT_VARROCK;
                }
                return true;

            case WATCHTOWER:
                if (Varpbits.varpbit(C_Varpbits.WATCHTOWER_TELEPORT) < C_Varpbits.WATCHTOWER_SPELL_LEARNT) {
                    return false;
                }
                if (castSpell) {
                    return Skills.realLevel(Constants.SKILLS_MAGIC) >= LevelRequirements.TELEPORT_TO_WATCHTOWER;
                } else {
                    return true;
                }

            case POH_RIMMINGTON: case POH_BRIMHAVEN: // TODO add varbit check for rimmington, brimhaven
                if (Varpbits.varpbit(C_Varpbits.HOUSE_OWNERSHIP) == 0) {
                    return false;
                }
                if (castSpell) {
                    return Skills.realLevel(Constants.SKILLS_MAGIC) >= LevelRequirements.TELEPORT_TO_HOUSE;
                }
                return true;

            case SLAYER_RING_NIEVE:
                return chargeableItems.haveItem(ChargeableItem.SLAYER_RING.toString());

            case HOME_TELEPORT:
                return teleportManager.getNextHomeTeleport() < System.currentTimeMillis();

            case CANIFIS:
                if (Varpbits.varpbit(C_Varpbits.PRIEST_IN_PERIL) < 61) {
                    return false;
                }
                return Items.ownItem(C_Items.ECTOPHIAL);

            case ARDOUGNE:
                if (Varpbits.varpbit(C_Varpbits.PLAGUE_CITY) < 30) {
                    return false;
                }
                if (castSpell) {
                    return Skills.realLevel(Constants.SKILLS_MAGIC) >= LevelRequirements.TELEPORT_ARDOUGNE;
                }
                return true;

            case TELEPORT_CRYSTAL_LLETYA:
                return chargeableItems.haveItem(ChargeableItem.CRYSTAL_TELEPORT.toString());

            case SEED_POD:
                return chargeableItems.haveItem(ChargeableItem.SEED_POD.toString());

            default:
                return true;
        }
    }

    public boolean haveRunes() {
        return getRunes().ownAll();
    }

    public Items getRunes() {
        Items runes = new Items("teleport runes");

        switch (this) {
            case LUMBRIDGE:
                runes.addItem(new Item(C_Items.LAW_RUNE, "law Rune", 1));
                runes.addItem(new Item(C_Items.AIR_RUNE, "air Rune", 3));
                runes.addItem(new Item(C_Items.EARTH_RUNE, "earth Rune", 1));
                break;

            case CAMELOT:
                runes.addItem(new Item(C_Items.AIR_RUNE, "air Rune", 5));
                runes.addItem(new Item(C_Items.LAW_RUNE, "law Rune", 1));
                break;

            case FALADOR:
                runes.addItem(new Item(C_Items.WATER_RUNE, "water Rune", 1));
                runes.addItem(new Item(C_Items.AIR_RUNE, "air Rune", 3));
                runes.addItem(new Item(C_Items.LAW_RUNE, "law Rune", 1));
                break;

            case VARROCK:
                runes.addItem(new Item(C_Items.FIRE_RUNE, "fire Rune", 1));
                runes.addItem(new Item(C_Items.AIR_RUNE, "air Rune", 3));
                runes.addItem(new Item(C_Items.LAW_RUNE, "law Rune", 1));
                break;

            case WATCHTOWER:
                runes.addItem(new Item(C_Items.LAW_RUNE, "law Rune", 2));
                runes.addItem(new Item(C_Items.EARTH_RUNE, "earth Rune", 2));
                break;

            case POH_RIMMINGTON: case POH_BRIMHAVEN:
                runes.addItem(new Item(C_Items.LAW_RUNE, "law Rune", 1));
                runes.addItem(new Item(C_Items.EARTH_RUNE, "earth Rune", 1));
                runes.addItem(new Item(C_Items.AIR_RUNE, "air Rune", 1));
                break;

            case ARDOUGNE:
                runes.addItem(new Item(C_Items.LAW_RUNE, "law Rune", 2));
                runes.addItem(new Item(C_Items.WATER_RUNE, "water Rune", 2));
                break;

            default:
                Script.stopScript("Teleport#getRunes did not recognize teleport: " + this);
                return runes;
        }

        return runes;
    }

    public boolean valid() {
        return valid(teleportOption);
    }

    public boolean valid(TeleportOption teleportOption) {
        switch (type) {
            case JEWELLERY:
                if (!haveHardRequirements(false)) {
                    LOGGER.info("don't have hard requirements for " + this);
                    return false;
                }
                if (!chargeableItems.haveItem(getChargeableItem().toString())) {
                    LOGGER.info("don't have chargeable item for " + this);
                    return false;
                }
                break;

            case SPELL:
                if (!haveHardRequirements(teleportOption.equals(TeleportOption.SPELL))) {
                    LOGGER.info("did not have hard requirements for " + this);
                    return false;
                }
                if (teleportOption.equals(TeleportOption.SPELL)) {
                    if (!haveRunes()) {
                        LOGGER.info("don't have runes for " + this);
                        return false;
                    }
                } else if (teleportOption.equals(TeleportOption.TAB)) {
                    if (!haveTabs()) {
                        LOGGER.info("don't have tabs for " + this);
                        return false;
                    }
                }
                break;
        }
        return true;
    }

    public boolean haveTabs() {
        return Items.ownItem(getTab().getSharedId());
    }

    public Item getTab() {
        switch (this) {
            case LUMBRIDGE:
                return new Item(C_Items.LUMBRIDGE_TELEPORT, "Lumbridge teleport", 1);

            case CAMELOT:
                return new Item(C_Items.CAMELOT_TELEPORT, "Camelot teleport", 1);

            case FALADOR:
                return new Item(C_Items.FALADOR_TELEPORT, "Falador teleport", 1);

            case VARROCK:
                return new Item(C_Items.VARROCK_TELEPORT, "Varrock teleport", 1);

            case WATCHTOWER:
                return new Item(C_Items.WATCHTOWER_TELEPORT, "Camelot teleport", 1);

            case POH_RIMMINGTON: case POH_BRIMHAVEN:
                return new Item(C_Items.TELEPORT_TO_HOUSE_TABLET, "Teleport to house", 1);

            case ARDOUGNE:
                return new Item(C_Items.ARDOUGNE_TELEPORT, "Ardougne teleport", 1);

            default:
                Script.stopScript("Teleport#getTabs did not recognize teleport: " + this);
                return new Item(-1);
        }
    }

    public Items getRequiredItems() {
        Items items = new Items("required items for " + this);
        switch (this.getType()) {
            case JEWELLERY:
                if (getChargeableItem() == null) {
                    return new Items("empty");
                }
                items.addItem(getChargeableItem().getItemWithCharges(1));
                return items;

            case SPELL:
                if (teleportOption.equals(TeleportOption.TAB)) {
                    if (haveHardRequirements(false)) {
                        items.addItem(getTab());
                    }
                } else {
                    if (haveHardRequirements(true)) {
                        items.addItems(getRunes());
                    }
                }

                return items;

            default:
                LOGGER.info("Teleport#getRequiredItems returned no items");
                return Items.NIL;
        }
    }

    public void setTeleportOption(TeleportOption teleportOption) {
        this.teleportOption = teleportOption;
    }

    public TeleportOption getTeleportOption() {
        return teleportOption;
    }

    public boolean isHaveToRestock() {
        return haveToRestock;
    }

    public void setHaveToRestock(boolean haveToRestock) {
        this.haveToRestock = haveToRestock;
    }
}
