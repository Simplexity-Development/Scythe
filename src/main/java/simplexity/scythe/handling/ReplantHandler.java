package simplexity.scythe.handling;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.ScythePermission;
import simplexity.scythe.hooks.CoreProtectHook;

public class ReplantHandler {
    private static ReplantHandler instance;
    private final CoreProtectAPI coreProtectAPI = CoreProtectHook.getInstance().getCoreProtect();

    public ReplantHandler() {
    }

    public static ReplantHandler getInstance() {
        if (instance == null) {
            instance = new ReplantHandler();
        }
        return instance;
    }

    public boolean passesPreChecks(Player player, boolean rightClick) {
        if (!player.hasPermission(ScythePermission.USE_REPLANT.getPermission())) return false;
        if (player.isSneaking()) return false;
        if (rightClick) {
            if (!passesRightHandChecks(player)) return false;
        } else {
            if (!passesLeftHandChecks(player)) return false;
        }
        return true;
    }

    public void replantCrop(Block block, BlockData blockData, Player player) {
        Location location = block.getLocation();
        if (!blockData.isSupported(location)) return;
        if (!(blockData instanceof Ageable newCropData)) return;
        newCropData.setAge(0);
        block.setBlockData(newCropData);
        if (isCoreProtectEnabled()) {
            coreProtectAPI.logPlacement(player.getName(), location, block.getType(), blockData);
        }
    }


    private boolean passesLeftHandChecks(Player player) {
        if (!ConfigHandler.getInstance().allowLeftClickReplant()) return false;
        if (!ConfigHandler.getInstance().shouldRequireToolLeftClickReplant()) return true;
        return HarvestHandler.getInstance().wasConfiguredToolUsed(player.getInventory().getItemInMainHand());
    }

    private boolean passesRightHandChecks(Player player) {
        if (!ConfigHandler.getInstance().isRightClickReplant()) return false;
        if (!ConfigHandler.getInstance().shouldRequireToolRightClickReplant()) return true;
        return HarvestHandler.getInstance().wasConfiguredToolUsed(player.getInventory().getItemInMainHand());
    }

    public boolean isCoreProtectEnabled() {
        return coreProtectAPI != null;
    }
}
