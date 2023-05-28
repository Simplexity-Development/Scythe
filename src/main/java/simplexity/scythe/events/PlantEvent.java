package simplexity.scythe.events;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import simplexity.scythe.config.ScythePermission;
import simplexity.scythe.hooks.CoreProtectHook;
@SuppressWarnings("unused")
public class PlantEvent extends Event implements Cancellable {
    private boolean cancelled;
    private final Player player;
    private final Block block;
    private final BlockData originialBlockData;
    private BlockData plantedBlockData;
    private final CoreProtectAPI coreProtectAPI = CoreProtectHook.getInstance().getCoreProtect();
    private static final HandlerList handlerList = new HandlerList();

    public PlantEvent(Player player, Block block, BlockData originialBlockData) {
        this.player = player;
        this.block = block;
        this.originialBlockData = originialBlockData;
    }

    public void prePlantChecks() {
        if (!isBlockStillSupported()) {
            cancelled = true;
            return;
        }
        if (!playerHasPermission()) {
            cancelled = true;
        }
    }

    public void replantCrop() {
        setToFirstGrowthStage();
        if (isCoreProtectEnabled()) {
            coreProtectAPI.logPlacement(player.getName(), getBlockLocation(), getBlockMaterial(), getPlantedBlockData());
        }
    }

    public void setToFirstGrowthStage() {
        if (getOriginialBlockData() instanceof Ageable newBlockData) {
            newBlockData.setAge(0);
            setPlantedBlockData(newBlockData);
            block.setBlockData(newBlockData);
        }
    }

    public boolean isBlockStillSupported() {
        return getOriginialBlockData().isSupported(getBlockLocation());
    }

    public boolean playerHasPermission() {
        return player.hasPermission(ScythePermission.USE_REPLANT.getPermission());
    }

    public Location getBlockLocation() {
        return block.getLocation();
    }

    public Material getBlockMaterial() {
        return getPlantedBlockData().getMaterial();
    }

    public BlockData getPlantedBlockData() {
        return plantedBlockData;
    }

    public void setPlantedBlockData(BlockData plantedBlockData) {
        this.plantedBlockData = plantedBlockData;
    }

    public boolean isCoreProtectEnabled() {
        return coreProtectAPI != null;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }


    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }


    public BlockData getOriginialBlockData() {
        return originialBlockData;
    }
}
