package simplexity.scythe.handling;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.scythe.commands.subcommands.ToggleCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.ScythePermission;

public class CropManager {
    private static CropManager instance;

    public static CropManager getInstance() {
        if (instance == null) instance = new CropManager();
        return instance;
    }

    public CropManager() {
    }

    public boolean canHarvest(Player player, Block block, boolean rightClick) {
        if (!player.hasPermission(ScythePermission.USE_HARVEST.getPermission())) return false;
        if (!playerHasToggleEnabled(player)) return false;
        if (!(block.getBlockData() instanceof Ageable ageable)) return false;
        if (!isCropFullGrown(ageable)) return false;
        if (!ConfigHandler.getInstance().getConfiguredCrops().contains(block.getType())) return false;
        if (rightClick && !Util.getInstance().harvestAllowedWithCurrentTool(player)) return false;
        return true;
    }

    public boolean canReplant(Player player, BlockData blockData, boolean rightClick) {
        if (!player.hasPermission(ScythePermission.USE_REPLANT.getPermission())) return false;
        if (player.isSneaking()) return false;
        if (!replantingAllowed(rightClick)) return false;
        if (!Util.getInstance().replantAllowedWithCurrentTool(player, rightClick)) return false;
        return !replantingRequiresSeeds(rightClick) || Util.getInstance().hasSeeds(player, blockData.getPlacementMaterial());
    }

    public void harvestCrop(Player player, Block block, boolean rightClick) {
        Util.getInstance().handleDurability(player);
        player.breakBlock(block);
        Util.getInstance().logCoreProtectRemoval(player, block);
        if (rightClick) Util.getInstance().playHarvestEffects(block);
    }

    public void replantCrop(Player player, Block block, BlockData originalBlockData, boolean rightClick) {
        if (!(originalBlockData instanceof Ageable newCropData)) return;
        if (!originalBlockData.isSupported(block.getLocation())) return;
        Util.getInstance().handleSeeds(player, rightClick, originalBlockData);
        newCropData.setAge(0);
        block.setBlockData(newCropData);
        Util.getInstance().logCoreProtectPlacement(player, block);
    }

    private boolean playerHasToggleEnabled(Player player) {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.getOrDefault(ToggleCommand.toggleKey, PersistentDataType.BOOLEAN, Boolean.TRUE);
    }

    private boolean isCropFullGrown(Ageable ageable) {
        return ageable.getAge() == ageable.getMaximumAge();
    }

    private boolean replantingAllowed(boolean rightClick) {
        if (rightClick) return ConfigHandler.getInstance().allowRightClickReplant();
        else return ConfigHandler.getInstance().allowLeftClickReplant();
    }

    private boolean replantingRequiresSeeds(boolean rightClick) {
        if (rightClick) return ConfigHandler.getInstance().doesRightClickReplantRequireSeeds();
        else return ConfigHandler.getInstance().doesLeftClickReplantRequireSeeds();
    }


}
