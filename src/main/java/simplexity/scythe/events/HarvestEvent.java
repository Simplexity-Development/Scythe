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
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.subcommands.ToggleCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.ScythePermission;
import simplexity.scythe.hooks.CoreProtectHook;

import java.util.List;
import java.util.logging.Logger;

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

    public void preHarvestChecks() {
        Logger logger = Scythe.getScytheLogger();
        if (!isCropAllowed()) {
            logger.info("returning due to crop not being allowed");
            setCancelled(true);
            return;
        }
        if (isRightClick && !isRightClickHarvestEnabled()) {
            logger.info("Returning due to the type being right-click, and right click not being allowed");
            setCancelled(true);
            return;
        }
        if ((isRightClick && getRequireToolRightClickHarvest()) && !wasConfiguredToolUsed()) {
            logger.info("Returning due to right click and require right click being enabled, and required tool not being used");
            setCancelled(true);
            return;
        }
        if (!isAgeableBlock()) {
            logger.info("returning due to block not being ageable");
            setCancelled(true);
            return;
        }
        if (!player.hasPermission(ScythePermission.USE.getPermission())) {
            logger.info("returning due to player not having permission");
            setCancelled(true);
            return;
        }
        if (!playerPDCToggleEnabled()) {
            logger.info("returning due to player having their toggle disabled");
            setCancelled(true);
        }
    }

    public void harvestCrop() {
        preHarvestChecks();
        if (cancelled) return;
        if (!isCropFullGrown()) {
            cancelled = true;
            return;
        }
        block.breakNaturally(usedItem);
        if (isCoreProtectEnabled()) {
            coreProtectAPI.logRemoval(player.getName(), getCropLocation(), getCropMaterial(), getCropBlockData());
        }
        if (shouldPlaySound()) {
            getCropLocation().getWorld().playSound(getCropLocation(), getConfiguredSound(), getConfiguredVolume(), getConfiguredPitch());
        }
        if (shouldShowParticles()) {
            if (getConfiguredParticle().equals(Particle.BLOCK_DUST)) {
                Scythe.getScytheLogger().info("Configured particle is dust particle");
                getCropLocation().getWorld().spawnParticle(Particle.BLOCK_DUST, getCropLocation(), getConfiguredParticleCount(), getCropBlockData());
            } else {
                Scythe.getScytheLogger().info("Configured particle is " + getConfiguredParticle());
                getCropLocation().getWorld().spawnParticle(getConfiguredParticle(), getCropLocation(), getConfiguredParticleCount());
            }
        }
    }

    public boolean isCoreProtectEnabled() {
        return coreProtectAPI != null;
    }

    public boolean isAgeableBlock() {
        try {
            ageable = (Ageable) block.getBlockData();
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isCropFullGrown() {
        int maxAge = ageable.getMaximumAge();
        int currentAge = ageable.getAge();
        return currentAge == maxAge;
    }

    public boolean shouldPlaySound() {
        return ConfigHandler.getInstance().shouldPlaySounds();
    }

    public boolean shouldShowParticles() {
        return ConfigHandler.getInstance().showBreakParticles();
    }

    public BlockData getCropBlockData() {
        return block.getBlockData();
    }

    public Location getCropLocation() {
        return block.getLocation();
    }

    public Material getCropMaterial() {
        return block.getType();
    }

    public boolean isCropAllowed() {
        return getConfiguredCropList().contains(getCropMaterial());
    }

    public boolean isRightClickHarvestEnabled() {
        return ConfigHandler.getInstance().allowRightClickHarvest();
    }

    public boolean wasConfiguredToolUsed() {
        return getConfiguredToolList().contains(usedItem);
    }

    public boolean getRequireToolRightClickHarvest() {
        return ConfigHandler.getInstance().shouldRequireToolRightClickHarvest();
    }

    public List<ItemStack> getConfiguredToolList() {
        return ConfigHandler.getInstance().getConfiguredTools();
    }

    public List<Material> getConfiguredCropList() {
        return ConfigHandler.getInstance().getConfiguredCrops();
    }

    public Sound getConfiguredSound() {
        return ConfigHandler.getInstance().getConfigSound();
    }

    public int getConfiguredVolume() {
        return ConfigHandler.getInstance().getSoundVolume();
    }

    public int getConfiguredPitch() {
        return ConfigHandler.getInstance().getSoundPitch();
    }

    public Particle getConfiguredParticle() {
        return ConfigHandler.getInstance().getConfigParticle();
    }

    public int getConfiguredParticleCount() {
        return ConfigHandler.getInstance().getParticleCount();
    }

    public boolean playerPDCToggleEnabled() {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.getOrDefault(ToggleCommand.functionToggle, PersistentDataType.BYTE, (byte) 0) == (byte) 0;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public boolean isRightClick() {
        return isRightClick;
    }

    public ItemStack getUsedItem() {
        return usedItem;
    }
}
