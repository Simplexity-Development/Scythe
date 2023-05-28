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

/**
 * Called when a crop is replanted
 */

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

    /**
     * Performs checks to ensure the block is still supported and the player has permission to replant.
     * If either check fails, the event is cancelled.
     */
    public void prePlantChecks() {
        if (!isBlockStillSupported()) {
            setCancelled(true);
            return;
        }
        if (!playerHasPermission()) {
            setCancelled(true);
        }
    }

    /**
     * Replants the crop by setting the block data to the first growth stage
     * <br>Logs the placement with CoreProtect if enabled.
     */
    public void replantCrop() {
        setToFirstGrowthStage();
        if (isCoreProtectEnabled()) {
            coreProtectAPI.logPlacement(player.getName(), getBlockLocation(), getBlockMaterial(), getPlantedBlockData());
        }
    }

    /**
     * Sets the block data to the first growth stage.
     * <br>Only applies to blocks with Ageable block data.
     */
    public void setToFirstGrowthStage() {
        if (getOriginialBlockData() instanceof Ageable newBlockData) {
            newBlockData.setAge(0);
            setPlantedBlockData(newBlockData);
            block.setBlockData(newBlockData);
        }
    }

    /**
     * Checks if the block is still supported in its current location.
     * <br>True if the block is still supported, false otherwise
     *
     * @return boolean
     */
    public boolean isBlockStillSupported() {
        return getOriginialBlockData().isSupported(getBlockLocation());
    }

    /**
     * Checks if the player has permission to replant.
     * <br>True if the player has permission, false otherwise
     *
     * @return boolean
     */
    public boolean playerHasPermission() {
        return player.hasPermission(ScythePermission.USE_REPLANT.getPermission());
    }

    /**
     * Returns the location of the block involved in the event.
     *
     * @return Location
     */
    public Location getBlockLocation() {
        return block.getLocation();
    }

    /**
     * Returns the material of the block involved in the event.
     *
     * @return Material
     */
    public Material getBlockMaterial() {
        return getPlantedBlockData().getMaterial();
    }

    /**
     * Returns the BlockData representing the new state of the block after replanting.
     *
     * @return BlockData
     */
    public BlockData getPlantedBlockData() {
        return plantedBlockData;
    }

    /**
     * Sets the BlockData representing the new state of the block after replanting.
     *
     * @param plantedBlockData BlockData
     */
    public void setPlantedBlockData(BlockData plantedBlockData) {
        this.plantedBlockData = plantedBlockData;
    }

    /**
     * Checks if CoreProtect is enabled on the server.
     * <br>True if CoreProtect is enabled, false otherwise
     *
     * @return boolean
     */
    public boolean isCoreProtectEnabled() {
        return coreProtectAPI != null;
    }

    /**
     * Returns the handler list for the PlantEvent.
     *
     * @return HandlerList
     */
    @SuppressWarnings("unused") //API method - not used internally
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Returns the handler list for the PlantEvent.
     *
     * @return HandlerList
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Checks if the event has been cancelled.
     * <br>True if the event has been cancelled, false otherwise
     *
     * @return boolean
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled state of the event.
     * <br>True to cancel the event, false otherwise
     *
     * @param cancel boolean
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Returns the player involved in the event.
     *
     * @return Player
     */
    @SuppressWarnings("unused") //API method - not used internally
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the block involved in the event.
     *
     * @return Block
     */
    @SuppressWarnings("unused") //API method - not used internally
    public Block getBlock() {
        return block;
    }

    /**
     * Returns the original BlockData for the block before replanting.
     *
     * @return BlockData
     */
    public BlockData getOriginialBlockData() {
        return originialBlockData;
    }
}
