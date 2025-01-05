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

public class ReplantEvent extends Event implements Cancellable {
    private boolean cancelled;
    private final Player player;
    private final Block block;
    private final BlockData originialBlockData;
    private static final HandlerList handlerList = new HandlerList();

    public ReplantEvent(Player player, Block block, BlockData originialBlockData) {
        this.player = player;
        this.block = block;
        this.originialBlockData = originialBlockData;
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
     * Returns the original BlockData for the block before replanting.
     *
     * @return BlockData
     */
    public BlockData getOriginialBlockData() {
        return originialBlockData;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }
}
