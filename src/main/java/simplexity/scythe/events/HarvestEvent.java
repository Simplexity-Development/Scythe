package simplexity.scythe.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a plant is harvested
 */
public class HarvestEvent extends Event implements Cancellable {
    private boolean cancelled;
    private Player player;
    private Block block;
    private boolean isRightClick;


    private static final HandlerList handlerList = new HandlerList();

    public HarvestEvent(Player player, Block block, boolean isRightClick) {
        this.player = player;
        this.block = block;
        this.isRightClick = isRightClick;
    }


    /**
     * Returns a boolean value that indicates whether the event is cancelled or not.
     * <br>The method returns the value of the "cancelled" flag, which is set by the setCancelled() method.
     *
     * @return boolean
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled flag to the specified value.
     * <br>The method sets the value of the "cancelled" flag to the specified boolean value.
     *
     * @param cancel boolean
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Returns the HandlerList object for the event.
     * <br>The method returns the static "handlerList" object, which is an instance of the HandlerList class.
     * <br>The HandlerList class is a built-in Bukkit class that provides methods for registering and unregistering listeners for events.
     *
     * @return HandlerList
     */

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * Returns the HandlerList object for the event.
     * <br>The method returns the static "handlerList" object, which is an instance of the HandlerList class.
     * <br>The HandlerList class is a built-in Bukkit class that provides methods for registering and unregistering listeners for events.
     *
     * @return HandlerList
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * Returns a Player object that represents the player involved in this event.
     *
     * @return Player
     */

    public Player getPlayer() {
        return player;
    }

    /**
     * Returns a Block object that represents the block involved in this event.
     *
     * @return Block
     */

    public Block getBlock() {
        return block;
    }

    /**
     * Returns a boolean value that indicates whether the interaction was a right-click action.
     * <br>The method returns the value of the "isRightClick" boolean field, which is set by the calling code to indicate whether the interaction was a right-click action or not.
     * <br>If the value is true, it means that the interaction was a right-click action; otherwise, it was not.
     *
     * @return boolean
     */

    public boolean isRightClick() {
        return isRightClick;
    }

    /**
     * Sets whether this event should be run with right-click conditions or not
     * @param rightClick boolean
     */

    public void setRightClick(boolean rightClick) {
        isRightClick = rightClick;
    }

    /**
     * Sets the block this event will run on
     * @param block Block
     */

    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     * Sets the player that this event will involve and track with coreprotect
     * @param player Player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

}
