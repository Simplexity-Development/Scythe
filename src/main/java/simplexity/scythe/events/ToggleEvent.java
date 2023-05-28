package simplexity.scythe.events;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import simplexity.scythe.Scythe;
import simplexity.scythe.config.Message;

/**
 * Called when a player runs the toggle command
 */

public class ToggleEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final NamespacedKey namespacedKey;
    private final Player player;
    private final MiniMessage miniMessage = Scythe.getMiniMessage();

    public ToggleEvent(Player player, NamespacedKey namespacedKey) {
        this.player = player;
        this.namespacedKey = namespacedKey;
    }

    /**
     * Gets the current toggle state of the player
     * <br>Returns a default of 0b (Enabled) if there was no information on the player
     *
     * @return byte
     */

    public byte getCurrentToggleState() {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.getOrDefault(namespacedKey, PersistentDataType.BYTE, (byte) 0);
    }

    /**
     * Sets the toggle to be disabled in the player's PDC
     * <br>Sets value to 1b
     */

    public void setDisabled() {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        playerPDC.set(namespacedKey, PersistentDataType.BYTE, (byte) 1);
        player.sendMessage(miniMessage.deserialize(Message.TOGGLE_OFF.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
    }

    /**
     * Sets the toggle to be enabled in the player's PDC
     * <br>Sets value to 0b
     * <br>Calls sendPlayerFeedback() with the ENABLED_MESSAGE_FORMAT message
     */

    public void setEnabled() {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        playerPDC.set(namespacedKey, PersistentDataType.BYTE, (byte) 0);
        player.sendMessage(miniMessage.deserialize(Message.TOGGLE_ON.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
    }


    /**
     * Returns the handler list for the ToggleEvent.
     *
     * @return HandlerList
     */

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Returns the handler list for the ToggleEvent.
     *
     * @return HandlerList
     */
    @SuppressWarnings("unused") //API method - not used internally
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets if the event is cancelled
     *
     * @return boolean
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets if the event should be cancelled
     *
     * @param cancel true if you wish to cancel this event
     */

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the player whose settings were toggled
     *
     * @return Player
     */
    @SuppressWarnings("unused") //API method - not used internally
    public Player getPlayer() {
        return player;
    }
}
