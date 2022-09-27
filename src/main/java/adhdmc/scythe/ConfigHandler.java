package adhdmc.scythe;

import com.destroystokyo.paper.MaterialSetTag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Logger;

public class ConfigHandler {
    private static final Logger logger = Scythe.plugin.getLogger();
    private static final String logPrefix = Scythe.pluginLoggerPrefix;
    public static FileConfiguration config = Scythe.plugin.getConfig();
    private static final NamespacedKey cropMaterials = new NamespacedKey(Scythe.plugin, "crops");
    public static boolean requireHoe;
    public static boolean rightClickHarvest;
    private static final HashMap<Message, String> messageMap = new HashMap<>();
    public enum Message {
        PREFIX, TOGGLE_ON, TOGGLE_OFF, UNKNOWN_COMMAND, CONFIG_RELOAD, NO_PERMISSION,
        NOT_A_PLAYER, HELP_MAIN, HELP_TOGGLE, HELP_RELOAD
    }
    public static final ArrayList<Material> configuredCrops = new ArrayList<Material>();
    public static void configParser(){
        setMessageMap();
        setConfiguredCrops();
        requireHoe = false;
        rightClickHarvest = true;
        requireHoe = config.getBoolean("require-hoe", false);
        rightClickHarvest = config.getBoolean("right-click-to-harvest", false);
    }

    private static void setConfiguredCrops(){
        configuredCrops.clear();
        List<String> cropList = config.getStringList("crops");
        for (String configCrop : cropList){
            if(Material.matchMaterial(configCrop) == null){
                logger.warning( logPrefix + configCrop + " is not a valid material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            Material crop = Material.matchMaterial(configCrop);
            assert crop != null;
            if(crop == Material.COCOA){
                configuredCrops.add(Material.COCOA);
                continue;
            }
            if(!crop.isBlock()){
                logger.warning(logPrefix + configCrop + " is not a valid block material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            BlockData cropBlock = Bukkit.createBlockData(crop);
            try{
                Ageable ageableCrop = (Ageable) cropBlock;
            } catch (ClassCastException exception){
                logger.warning(logPrefix + configCrop + " is not a valid crop material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            configuredCrops.add(crop);
            }
        }

    private static void setMessageMap(){
        messageMap.clear();
        messageMap.put(Message.PREFIX,
                config.getString("prefix","<gold><bold>[</bold><yellow>Scythe</yellow><bold>]<reset>"));
        messageMap.put(Message.TOGGLE_ON,
                config.getString("toggle-on", "<green>Scythe toggled on!"));
        messageMap.put(Message.TOGGLE_OFF,
                config.getString("toggle-off", "<red>Scythe toggled off!"));
        messageMap.put(Message.UNKNOWN_COMMAND,
                config.getString("unknown-command", "<red>Unknown Command"));
        messageMap.put(Message.CONFIG_RELOAD,
                config.getString("config-reload", "<gold>Scythe Config Reloaded!"));
        messageMap.put(Message.NO_PERMISSION,
                config.getString("no-permission", "<red>You do not have the required permissions to run this command"));
        messageMap.put(Message.NOT_A_PLAYER,
                config.getString("not-a-player", "Sorry! This command can only be run by a player"));
        messageMap.put(Message.HELP_MAIN,
                config.getString("help-main", "<grey>Scythe allows players to harvest grown crops without needing to replant"));
        messageMap.put(Message.HELP_TOGGLE,
                config.getString("help-toggle", "<yellow>/scythe toggle \n<grey>• Toggle scythe on or off"));
        messageMap.put(Message.HELP_RELOAD,
                config.getString("help-reload", "<yellow>/scythe reload \n<grey>• Reloads config settings"));
    }
    public static Map<Message, String> getMessageMap() {
        return Collections.unmodifiableMap(messageMap);
    }

}
