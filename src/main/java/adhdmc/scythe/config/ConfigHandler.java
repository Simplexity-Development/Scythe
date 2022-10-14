package adhdmc.scythe.config;

import adhdmc.scythe.Scythe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ConfigHandler {
    private static final Logger logger = Scythe.getScytheLogger();
    private static final Plugin instance = Scythe.getInstance();
    private static boolean requireHoe;
    private static boolean rightClickHarvest;
    private static final ArrayList<Material> configuredCrops = new ArrayList<>();
    public static void configParser(){
        Message.reloadMessages();
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
                logger.warning( Message.CONSOLE_PREFIX.getMessage() + configCrop + " is not a valid material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            Material crop = Material.matchMaterial(configCrop);
            assert crop != null;
            if(crop == Material.COCOA){
                configuredCrops.add(Material.COCOA);
                continue;
            }
            if(!crop.isBlock()){
                logger.warning(Message.CONSOLE_PREFIX.getMessage() + configCrop + " is not a valid block material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            BlockData cropBlock = Bukkit.createBlockData(crop);
            try{
                Ageable ageableCrop = (Ageable) cropBlock;
            } catch (ClassCastException exception){
                logger.warning(Message.CONSOLE_PREFIX.getMessage() + configCrop + " is not a valid crop material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            configuredCrops.add(crop);
            }
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
