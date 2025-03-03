package simplexity.scythe.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a plant is harvested
 */
public class HarvestEvent extends Event implements Cancellable {
    private boolean cancelled;
    private Player player;
    private Block block;


    private static final HandlerList handlerList = new HandlerList();

    public HarvestEvent(Player player, Block block) {
        this.player = player;
        this.block = block;
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
     * Sets the block this event will run on
     *
     * @param block Block
     */

    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     * Sets the player that this event will involve and track with coreprotect
     *
     * @param player Player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

}
