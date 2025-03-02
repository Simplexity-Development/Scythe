package simplexity.scythe.handling;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simplexity.scythe.Scythe;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.Message;
import simplexity.scythe.hooks.CoreProtectHook;

import java.util.HashMap;
import java.util.Set;

public class Util {
    private static Util instance;

    public static Util getInstance() {
        if (instance == null) instance = new Util();
        return instance;
    }

    public Util() {
    }

    private final CoreProtectAPI coreProtectAPI = CoreProtectHook.getInstance().getCoreProtect();

    public boolean harvestAllowedWithCurrentTool(Player player) {
        return !ConfigHandler.getInstance().rightClickHarvestRequiresTool() || requiredToolUsed(player);
    }

    public boolean replantAllowedWithCurrentTool(Player player, boolean rightClick) {
        if (rightClick) {
            if (!ConfigHandler.getInstance().rightClickReplantRequiresTool()) return true;
            return requiredToolUsed(player);
        }
        if (!ConfigHandler.getInstance().leftClickReplantRequiresTool()) return true;
        return requiredToolUsed(player);
    }

    public boolean requiredToolUsed(Player player) {
        Material itemUsed = player.getInventory().getItemInMainHand().getType();
        return ConfigHandler.getInstance().getConfiguredTools().contains(itemUsed)
               && hasItemModel(player.getInventory().getItemInMainHand());
    }

    private boolean hasItemModel(ItemStack item) {
        Set<NamespacedKey> itemModels = ConfigHandler.getInstance().getItemModels();
        if (itemModels.isEmpty()) return true;
        Key modelKey = item.getData(DataComponentTypes.ITEM_MODEL);
        return itemModels.contains((NamespacedKey) modelKey);
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean toolHasEnoughDurability(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasData(DataComponentTypes.MAX_DAMAGE) || !item.hasData(DataComponentTypes.DAMAGE)) return;
        int currentDamage = item.getData(DataComponentTypes.DAMAGE);
        int maxDamage = item.getData(DataComponentTypes.MAX_DAMAGE);
        if (ConfigHandler.getInstance().preventToolBreaking() &&
            (maxDamage - currentDamage <= ConfigHandler.getInstance().getMinimumDurability())) {
            player.sendMessage(Scythe.getMiniMessage().deserialize(Message.YOUR_TOOL_IS_ALMOST_BROKEN.getMessage()));
            return false;
        }
        return true;
    }

    public void handleDurability(Player player) {
        if (!ConfigHandler.getInstance().harvestUsesDurability()) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        int currentDamage = item.getData(DataComponentTypes.DAMAGE);
        item.setData(DataComponentTypes.DAMAGE, currentDamage + 1);
    }

    public boolean hasSeeds(Player player, Material seedMaterial) {
        return player.getInventory().containsAtLeast(ItemStack.of(seedMaterial), 1);
    }

    /*
    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
     */
    public boolean seedsHandledProperly(Player player, Material seedMaterial) {
        if (!ConfigHandler.getInstance().replantRequiresSeeds()) return true;
        Integer seedLocation = getSeedLocation(player, seedMaterial);
        if (seedLocation == null) return false;
        ItemStack seeds = player.getInventory().getItem(seedLocation);
        if (seeds == null) return false;
        seeds.subtract(1);
        return true;
    }

    private Integer getSeedLocation(Player player, Material seedMaterial) {
        ItemStack defaultSeeds = ItemStack.of(seedMaterial);
        HashMap<Integer, ? extends ItemStack> slotItems = player.getInventory().all(seedMaterial);
        for (Integer slot : slotItems.keySet()) {
            if (slotItems.get(slot).asOne().equals(defaultSeeds)) return slot;
        }
        return null;
    }

    public void logCoreProtectRemoval(Player player, Block block) {
        if (coreProtectAPI == null) return;
        Location location = block.getLocation();
        Material material = block.getType();
        BlockData data = block.getBlockData();
        coreProtectAPI.logRemoval(player.getName(), location, material, data);
    }

    public void logCoreProtectPlacement(Player player, Block block) {
        if (coreProtectAPI == null) return;
        Location location = block.getLocation();
        BlockData blockData = block.getBlockData();
        coreProtectAPI.logPlacement(player.getName(), location, block.getType(), blockData);
    }

    public void playHarvestEffects(Block block, BlockData originalBlockData) {
        handleSound(block);
        handleParticles(block, originalBlockData);
    }

    public void handleSound(Block block) {
        if (!ConfigHandler.getInstance().shouldPlaySounds()) return;
        Location location = block.getLocation();
        Sound sound = ConfigHandler.getInstance().getConfigSound();
        float volume = ConfigHandler.getInstance().getSoundVolume();
        float pitch = ConfigHandler.getInstance().getSoundPitch();
        location.getWorld().playSound(location, sound, SoundCategory.BLOCKS, volume, pitch);
    }

    private void handleParticles(Block block, BlockData originalBlockData) {
        if (!ConfigHandler.getInstance().showBreakParticles()) return;
        Location location = block.getLocation().toCenterLocation();
        Particle particle = ConfigHandler.getInstance().getConfigParticle();
        if (particle.getDataType().isInstance(BlockData.class)) {
            spawnParticlesWithBlockData(location, originalBlockData, particle);
        } else {
            spawnParticlesWithoutBlockData(location, particle);
        }


    }

    private void spawnParticlesWithBlockData(Location location, BlockData blockData, Particle particle) {
        int particleCount = ConfigHandler.getInstance().getParticleCount();
        double particleSpread = ConfigHandler.getInstance().getParticleSpread();

        World world = location.getWorld();
        for (int i = 0; i < particleCount; i++) {
            double offsetX = (Math.random() - 0.5) * particleSpread;
            double offsetY = (Math.random() - 0.5) * particleSpread;
            double offsetZ = (Math.random() - 0.5) * particleSpread;
            world.spawnParticle(
                    particle,
                    location.getX() + offsetX,
                    location.getY() + offsetY,
                    location.getZ() + offsetZ,
                    1,
                    blockData
            );
        }
    }

    private void spawnParticlesWithoutBlockData(Location location, Particle particle) {
        int particleCount = ConfigHandler.getInstance().getParticleCount();
        double particleSpread = ConfigHandler.getInstance().getParticleSpread();

        World world = location.getWorld();
        for (int i = 0; i < particleCount; i++) {
            double offsetX = (Math.random() - 0.5) * particleSpread;
            double offsetY = (Math.random() - 0.5) * particleSpread;
            double offsetZ = (Math.random() - 0.5) * particleSpread;
            world.spawnParticle(
                    particle,
                    location.getX() + offsetX,
                    location.getY() + offsetY,
                    location.getZ() + offsetZ,
                    1
            );
        }
    }


}
