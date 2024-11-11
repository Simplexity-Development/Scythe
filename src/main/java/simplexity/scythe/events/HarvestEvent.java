package simplexity.scythe.events;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import simplexity.scythe.commands.subcommands.ToggleCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.ScythePermission;
import simplexity.scythe.hooks.CoreProtectHook;

import java.util.List;

/**
 * Called when a plant is harvested
 */
public class HarvestEvent extends Event implements Cancellable {
    private boolean cancelled;
    private final Player player;
    private final Block block;
    private final ItemStack usedItem;
    private final boolean isRightClick;
    private Ageable ageable;
    private final CoreProtectAPI coreProtectAPI = CoreProtectHook.getInstance().getCoreProtect();
    private static final HandlerList handlerList = new HandlerList();

    public HarvestEvent(Player player, Block block, boolean isRightClick, ItemStack usedItem) {
        this.player = player;
        this.block = block;
        this.isRightClick = isRightClick;
        this.usedItem = usedItem;
    }

    /**
     * Performs pre-harvest checks to determine if the harvest action is allowed.
     * <br>Checks for crop allowance, right-click harvesting permission, the use of configured tool for right-click harvesting, ageable block type, player permission, PDC (Persistent Data Container) toggle, and crop growth level (if right-click harvesting).
     * <br>If any of the checks fail, the action is cancelled by calling the setCancelled() method and returning.
     * <br>If all checks pass, the method completes without any issues.
     */
    public void preHarvestChecks() {
        if (!isCropAllowed()) {
            setCancelled(true);
            return;
        }
        if (!isRightClick && !isLeftClickReplantEnabled()) {
            setCancelled(true);
            return;
        }
        if (isRightClick && !isRightClickHarvestEnabled()) {
            setCancelled(true);
            return;
        }
        if ((isRightClick && getRequireToolRightClickHarvest()) && !wasConfiguredToolUsed()) {
            setCancelled(true);
            return;
        }
        if (!isAgeableBlock()) {
            setCancelled(true);
            return;
        }
        if (!player.hasPermission(ScythePermission.USE_HARVEST.getPermission())) {
            setCancelled(true);
            return;
        }
        if (!playerPDCToggleEnabled()) {
            setCancelled(true);
            return;
        }
        if (!isCropFullGrown() && isRightClick) {
            setCancelled(true);
        }
    }

    /**
     * Harvests the crop at the specified location and performs additional actions such as breaking the block, logging the removal using CoreProtect API (if enabled),
     * <br>playing sound, and showing particles based on the configuration settings.
     * <br>The method performs pre-harvest checks by calling the preHarvestChecks() method, and returns immediately if the action has been cancelled.
     * <br>If the crop is not fully grown, the method sets the action as cancelled and returns.
     * <br>If the crop is full-grown, the block is broken using the usedItem parameter specified.
     * <br>If CoreProtect API is enabled, the removal of the crop is logged using the CoreProtectAPIs logRemoval() method.
     * <br>If sound is enabled, it will be played using the getConfiguredSound() method and the getConfiguredVolume() and getConfiguredPitch() settings to determine the volume and pitch.
     * <br>If particles are enabled, the particle effect will be shown using the getConfiguredParticle(), getConfiguredParticleCount(), and getCropBlockData() settings to determine the type, count, and data of the particle.
     */
    public void harvestCrop() {
        preHarvestChecks();
        if (cancelled) return;
        if (!isCropFullGrown()) {
            setCancelled(true);
            return;
        }
        player.breakBlock(block);
        if (isCoreProtectEnabled()) {
            coreProtectAPI.logRemoval(player.getName(), getCropLocation(), getCropMaterial(), getCropBlockData());
        }
        if (shouldPlaySound()) {
            getCropLocation().getWorld().playSound(getCropLocation(), getConfiguredSound(), getConfiguredVolume(), getConfiguredPitch());
        }
        if (shouldShowParticles()) {
            getCropLocation().getWorld().spawnParticle(getConfiguredParticle(), getCropLocation().toCenterLocation(), getConfiguredParticleCount());
        }
    }

    /**
     * Returns a boolean value that indicates whether CoreProtect API is enabled or not.
     * <br>The method checks if the coreProtectAPI object is null or not.
     * <br>If the object is not null, it means that the CoreProtect API is enabled and the method returns true.
     * <br>If the object is null, it means that the CoreProtect API is disabled and the method returns false.
     *
     * @return boolean
     */
    public boolean isCoreProtectEnabled() {
        return coreProtectAPI != null;
    }

    /**
     * Returns a boolean value that indicates whether the block is ageable or not.
     * <br>The method attempts to cast the block's block data to an Ageable object using the getBlockData() method on the "block" object.
     * <br>If the cast is successful, the method stores the Ageable object in the "ageable" variable and returns true.
     * <br>If the cast fails, the method prints a stack trace and returns false.
     *
     * @return boolean
     */
    public boolean isAgeableBlock() {
        try {
            ageable = (Ageable) block.getBlockData();
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Returns a boolean value that indicates whether the crop is fully grown or not.
     * <br>The method obtains the maximum age of the crop and its current age using the getMaximumAge() and getAge() methods on the "ageable" object.
     * <br>It then checks if the current age is equal to the maximum age and returns the result.
     *
     * @return boolean
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isCropFullGrown() {
        int maxAge = ageable.getMaximumAge();
        int currentAge = ageable.getAge();
        return currentAge == maxAge;
    }

    /**
     * Returns a boolean value that indicates whether sounds should be played or not.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its shouldPlaySounds() method to retrieve the value.
     *
     * @return boolean
     */
    public boolean shouldPlaySound() {
        return ConfigHandler.getInstance().shouldPlaySounds();
    }

    /**
     * Returns a boolean value that indicates whether break particles should be shown or not.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its showBreakParticles() method to retrieve the value.
     *
     * @return boolean
     */
    public boolean shouldShowParticles() {
        return ConfigHandler.getInstance().showBreakParticles();
    }

    /**
     * Returns the block data of the crop.
     * <br>The method returns the block data of the block where the crop is planted.
     *
     * @return BlockData
     */
    public BlockData getCropBlockData() {
        return block.getBlockData();
    }

    /**
     * Returns the location of the block where the crop is planted.
     *
     * @return Location
     */
    public Location getCropLocation() {
        return block.getLocation();
    }

    /**
     * Returns the material of the crop.
     * <br>The method returns the type of the block where the crop is planted.
     *
     * @return Material
     */
    public Material getCropMaterial() {
        return block.getType();
    }

    /**
     * Returns a boolean value indicating whether the crop is allowed or not.
     * <br>The method internally checks whether the crop material is present in the configured crop list or not.
     *
     * @return boolean
     */
    public boolean isCropAllowed() {
        return getConfiguredCropList().contains(getCropMaterial());
    }

    /**
     * Returns a boolean value that indicates whether normally breaking the crop triggers the harvest event or not.
     * <br>The method internally gets the ConfigHandler instance and calls its allowLeftClickReplant() method to retrieve the value.
     */
    public boolean isLeftClickReplantEnabled() {
        return ConfigHandler.getInstance().allowLeftClickReplant();
    }

    /**
     * Returns a boolean value that indicates whether right-click harvest is enabled or not.
     * <br>The method internally gets the ConfigHandler instance and calls its allowRightClickHarvest() method to retrieve the value.
     *
     * @return boolean
     */
    public boolean isRightClickHarvestEnabled() {
        return ConfigHandler.getInstance().allowRightClickHarvest();
    }

    /**
     * Returns a boolean value that specifies whether the configured tool was used or not.
     * <br>The method internally checks whether the used item is present in the configured tool list or not.
     *
     * @return boolean
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted") //I want this named this way for API purposes
    public boolean wasConfiguredToolUsed() {
        return getConfiguredToolList().contains(usedItem);
    }

    /**
     * Returns a boolean value that specifies whether tool right-click harvesting is required or not.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its shouldRequireToolRightClickHarvest() method to retrieve the value.
     *
     * @return boolean
     */
    public boolean getRequireToolRightClickHarvest() {
        return ConfigHandler.getInstance().shouldRequireToolRightClickHarvest();
    }

    /**
     * Returns a list of ItemStack objects that represent the configured tools.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its getConfiguredTools() method to retrieve the list.
     *
     * @return List<ItemStack>
     */
    public List<ItemStack> getConfiguredToolList() {
        return ConfigHandler.getInstance().getConfiguredTools();
    }

    /**
     * Returns a list of Material objects that represent the configured crops.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its getConfiguredCrops() method to retrieve the list.
     *
     * @return List<Material>
     */
    public List<Material> getConfiguredCropList() {
        return ConfigHandler.getInstance().getConfiguredCrops();
    }

    /**
     * Returns a Sound object that represents the configured sound.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its getConfigSound() method to retrieve the sound.
     *
     * @return Sound
     */
    public Sound getConfiguredSound() {
        return ConfigHandler.getInstance().getConfigSound();
    }

    /**
     * Returns an integer that represents the configured sound volume.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its getSoundVolume() method to retrieve the configured sound volume.
     *
     * @return int
     */
    public float getConfiguredVolume() {
        return ConfigHandler.getInstance().getSoundVolume();
    }

    /**
     * Returns an integer that represents the configured sound pitch.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its getSoundPitch() method to retrieve the configured sound pitch.
     *
     * @return int
     */
    public float getConfiguredPitch() {
        return ConfigHandler.getInstance().getSoundPitch();
    }

    /**
     * Returns a Particle object that represents the configured particle.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its getConfigParticle() method to retrieve the particle.
     *
     * @return Particle
     */
    public Particle getConfiguredParticle() {
        return ConfigHandler.getInstance().getConfigParticle();
    }

    /**
     * Returns an integer that represents the configured particle count.
     * <br>The method internally gets the instance of the ConfigHandler class and calls its getParticleCount() method to retrieve the configured particle count.
     *
     * @return int
     */
    public int getConfiguredParticleCount() {
        return ConfigHandler.getInstance().getParticleCount();
    }

    /**
     * Returns a boolean value that indicates whether the player's PDC (Persistent Data Container) toggle is enabled or not.
     * <br>The method obtains the player's PDC using the getPersistentDataContainer() method on the "player" object.
     * <br>It then uses the PersistentDataContainer's getOrDefault() method to retrieve the value of the "functionToggle" key and checks if it is equal to 0.
     *
     * @return boolean
     */
    public boolean playerPDCToggleEnabled() {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.getOrDefault(ToggleCommand.toggleKey, PersistentDataType.BOOLEAN, Boolean.TRUE);
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
    @SuppressWarnings("unused") //API method - not used internally
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
    @SuppressWarnings("unused") //API method - not used internally
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns a Block object that represents the block involved in this event.
     *
     * @return Block
     */
    @SuppressWarnings("unused") //API method - not used internally
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
    @SuppressWarnings("unused") //API method - not used internally
    public boolean isRightClick() {
        return isRightClick;
    }

    /**
     * Returns an ItemStack object that represents the item used during interaction.
     * <br>The method returns the value of the "usedItem" field, which is set by the calling code to represent the item used during interaction.
     * <br>The ItemStack object encapsulates the item's information, such as its type, count and metadata.
     *
     * @return ItemStack
     */
    @SuppressWarnings("unused") //API method - not used internally
    public ItemStack getUsedItem() {
        return usedItem;
    }
}
