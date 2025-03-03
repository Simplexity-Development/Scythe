package simplexity.scythe.events;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a crop is replanted
 */
@SuppressWarnings("unused")
public class ReplantEvent extends Event implements Cancellable {
    private boolean cancelled;
    private Player player;
    private Block block;
    private BlockData blockData;

    private static final HandlerList handlerList = new HandlerList();

    public ReplantEvent(Player player, Block block, BlockData blockData) {
        this.player = player;
        this.block = block;
        this.blockData = blockData;
    }

    /**
     * Returns the original BlockData for the block before replanting.
     *
     * @return BlockData
     */
    public BlockData getBlockData() {
        return blockData;
    }

    /**
     * Gets the player involved in this event
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the block that will be replanted
     *
     * @return Block
     */
    public Block getBlock() {
        return block;
    }


    /**
     * Sets the block data that the replanted block will be based off of. This will set the information that
     * the new block will be. This does not adjust the logic before the block is broken, that is in HarvestEvent
     *
     * @param blockData BlockData
     */

    public void setBlockData(BlockData blockData) {
        this.blockData = blockData;
    }

    /**
     * Set the player connected to this event, the player that will be tracked in CoreProtect and have items
     * removed from their inventory
     *
     * @param player Player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Set the block that will be replanted, used for location
     *
     * @param block Block
     */
    public void setBlock(Block block) {
        this.block = block;
    }


    /**
     * Returns the handler list for the PlantEvent.
     *
     * @return HandlerList
     */
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

}
