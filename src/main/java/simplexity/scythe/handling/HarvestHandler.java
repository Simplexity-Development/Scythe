package simplexity.scythe.handling;

import com.destroystokyo.paper.ParticleBuilder;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.scythe.commands.subcommands.ToggleCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.ScythePermission;
import simplexity.scythe.hooks.CoreProtectHook;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class HarvestHandler {
    private static HarvestHandler instance;

    public static HarvestHandler getInstance() {
        if (instance == null) instance = new HarvestHandler();
        return instance;
    }

    public HarvestHandler() {
    }

    private final CoreProtectAPI coreProtectAPI = CoreProtectHook.getInstance().getCoreProtect();


    public boolean passesPreHarvestChecks(Player player, Block block, boolean rightClick) {
        if (!playerPDCToggleEnabled(player)) return false;
        if (!player.hasPermission(ScythePermission.USE_HARVEST.getPermission())) return false;
        if (!isCropAllowed(block)) return false;
        if (!(block.getBlockData() instanceof Ageable ageable)) return false;
        if (!isCropFullGrown(ageable)) return false;
        if (rightClick && !passesRightClickChecks(player)) return false;

        return true;
    }


    public void harvestCrop(Player player, Block block) {
        player.breakBlock(block);
        Location location = block.getLocation();
        Material material = block.getType();
        BlockData data = block.getBlockData();
        if (isCoreProtectEnabled()) {
            coreProtectAPI.logRemoval(player.getName(), location, material, data);
        }
        handleSound(block);
        handleParticle(block);
    }

    private boolean passesRightClickChecks(Player player) {
        if (!ConfigHandler.getInstance().allowRightClickHarvest()) return false;
        if (!ConfigHandler.getInstance().isRightClickHarvestRequireTool()) return true;
        ItemStack itemUsed = player.getInventory().getItemInMainHand();
        return wasConfiguredToolUsed(itemUsed);
    }

    private void handleSound(Block block) {
        if (!ConfigHandler.getInstance().shouldPlaySounds()) return;
        Location location = block.getLocation();
        Sound sound = ConfigHandler.getInstance().getConfigSound();
        float volume = ConfigHandler.getInstance().getSoundVolume();
        float pitch = ConfigHandler.getInstance().getSoundPitch();
        location.getWorld().playSound(location, sound, SoundCategory.BLOCKS, volume, pitch);
    }

    private void handleParticle(Block block) {
        if (!ConfigHandler.getInstance().showBreakParticles()) return;
        Location location = block.getLocation().toCenterLocation();
        BlockData blockData = block.getBlockData();
        int particleCount = ConfigHandler.getInstance().getParticleCount();
        Particle particle = ConfigHandler.getInstance().getConfigParticle();
        ParticleBuilder particleBuilder = new ParticleBuilder(particle);
        if (particle.getDataType().equals(BlockData.class)) {
            particleBuilder.data(blockData);
        }
        particleBuilder.location(location);
        particleBuilder.count(particleCount);
        particleBuilder.spawn();
    }

    public boolean isCoreProtectEnabled() {
        return coreProtectAPI != null;
    }

    public boolean isCropFullGrown(Ageable ageable) {
        int maxAge = ageable.getMaximumAge();
        int currentAge = ageable.getAge();
        return currentAge == maxAge;
    }

    public boolean wasConfiguredToolUsed(ItemStack itemStack) {
        if (ConfigHandler.getInstance().getConfiguredTools().contains(itemStack.getType())) return true;
        return false;
    }

    public boolean playerPDCToggleEnabled(Player player) {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.getOrDefault(ToggleCommand.toggleKey, PersistentDataType.BOOLEAN, Boolean.TRUE);
    }

    public boolean isCropAllowed(Block block) {
        return ConfigHandler.getInstance().getConfiguredCrops().contains(block.getType());
    }

}
