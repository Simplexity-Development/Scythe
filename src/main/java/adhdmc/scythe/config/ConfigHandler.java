package adhdmc.scythe.config;

import adhdmc.scythe.Scythe;
import com.destroystokyo.paper.MaterialSetTag;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.logging.Logger;

public class ConfigHandler {
    private static final Logger logger = Scythe.getScytheLogger();
    private static final Plugin instance = Scythe.getInstance();
    private static boolean requireHoe;
    private static boolean rightClickHarvest;
    private static final HashMap<Message, String> messageMap = new HashMap<>();
    public enum Message {
        CONSOLE_PREFIX, PREFIX, TOGGLE_ON, TOGGLE_OFF, UNKNOWN_COMMAND, CONFIG_RELOAD, NO_PERMISSION,
        NOT_A_PLAYER, HELP_MAIN, HELP_TOGGLE, HELP_RELOAD
    }
    private static final ArrayList<Material> configuredCrops = new ArrayList<>();
    public static void configParser(){
        setMessageMap();
        setConfiguredCrops();
        requireHoe = false;
        rightClickHarvest = true;
        requireHoe = instance.getConfig().getBoolean("require-hoe");
        rightClickHarvest = instance.getConfig().getBoolean("right-click-to-harvest");
    }

    private static void setConfiguredCrops(){
        configuredCrops.clear();
        List<String> cropList = instance.getConfig().getStringList("crops");
        for (String configCrop : cropList){
            if(Material.matchMaterial(configCrop) == null){
                logger.warning( messageMap.get(Message.CONSOLE_PREFIX) + configCrop + " is not a valid material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            Material crop = Material.matchMaterial(configCrop);
            assert crop != null;
            if(crop == Material.COCOA){
                configuredCrops.add(Material.COCOA);
                continue;
            }
            if(!crop.isBlock()){
                logger.warning(messageMap.get(Message.CONSOLE_PREFIX) + configCrop + " is not a valid block material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            BlockData cropBlock = Bukkit.createBlockData(crop);
            try{
                Ageable ageableCrop = (Ageable) cropBlock;
            } catch (ClassCastException exception){
                logger.warning(messageMap.get(Message.CONSOLE_PREFIX) + configCrop + " is not a valid crop material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            configuredCrops.add(crop);
            }
        }

    private static final HashMap<Permission, String> permMap = new HashMap<>();

    public enum Permission{
        USE, TOGGLE_COMMAND, RELOAD_COMMAND
    }

    public static void setPerms(){
        permMap.clear();
        permMap.put(Permission.USE, "scythe.use");
        permMap.put(Permission.TOGGLE_COMMAND, "scythe.toggle");
        permMap.put(Permission.RELOAD_COMMAND, "scythe.reload");
    }

    public static Map<Permission, String> getPermMap(){
        return Collections.unmodifiableMap(permMap);
    }

    private static void setMessageMap(){
        messageMap.clear();
        messageMap.put(Message.CONSOLE_PREFIX, "[Scythe] ");
        messageMap.put(Message.PREFIX,
                instance.getConfig().getString("prefix","<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset>"));
        messageMap.put(Message.TOGGLE_ON,
                instance.getConfig().getString("toggle-on", "<green>Scythe toggled on!"));
        messageMap.put(Message.TOGGLE_OFF,
                instance.getConfig().getString("toggle-off", "<red>Scythe toggled off!"));
        messageMap.put(Message.UNKNOWN_COMMAND,
                instance.getConfig().getString("unknown-command", "<red>Unknown Command"));
        messageMap.put(Message.CONFIG_RELOAD,
                instance.getConfig().getString("config-reload", "<gold>Scythe Config Reloaded!"));
        messageMap.put(Message.NO_PERMISSION,
                instance.getConfig().getString("no-permission", "<red>You do not have the required Permission to run this command"));
        messageMap.put(Message.NOT_A_PLAYER,
                instance.getConfig().getString("not-a-player", "Sorry! This command can only be run by a player"));
        messageMap.put(Message.HELP_MAIN,
                instance.getConfig().getString("help-main", "<grey>Scythe allows players to harvest grown crops without needing to replant"));
        messageMap.put(Message.HELP_TOGGLE,
                instance.getConfig().getString("help-toggle", "<yellow>/scythe toggle \n<grey>• Toggle scythe on or off"));
        messageMap.put(Message.HELP_RELOAD,
                instance.getConfig().getString("help-reload", "<yellow>/scythe reload \n<grey>• Reloads config settings"));
    }
    public static Map<Message, String> getMessageMap() {
        return Collections.unmodifiableMap(messageMap);
    }
    public static boolean isRequireHoe(){
        return requireHoe;
    }
    public static boolean isRightClickHarvest() {
        return rightClickHarvest;
    }
    public static List<Material> getConfiguredCrops() {
        return Collections.unmodifiableList(configuredCrops);
    }
}
