package simplexity.scythe.handling;

import com.destroystokyo.paper.ParticleBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simplexity.scythe.Scythe;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.Message;
import simplexity.scythe.hooks.CoreProtectHook;

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
        return !ConfigHandler.getInstance().doesRightClickHarvestRequireTool() || requiredToolUsed(player);
    }

    public boolean replantAllowedWithCurrentTool(Player player, boolean rightClick) {
        if (rightClick && !ConfigHandler.getInstance().shouldRequireToolLeftClickReplant()) return true;
        else if (!rightClick && !ConfigHandler.getInstance().shouldRequireToolRightClickReplant()) return true;
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
    public void handleDurability(Player player) {
        if (!ConfigHandler.getInstance().doesHarvestUseDurability()) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasData(DataComponentTypes.MAX_DAMAGE) || !item.hasData(DataComponentTypes.DAMAGE)) return;
        int currentDamage = item.getData(DataComponentTypes.DAMAGE);
        int maxDamage = item.getData(DataComponentTypes.MAX_DAMAGE);
        if (ConfigHandler.getInstance().shouldPreventToolBreak() &&
            (maxDamage - currentDamage <= ConfigHandler.getInstance().getMinimumDurability())){
            player.sendMessage(Scythe.getMiniMessage().deserialize(Message.YOUR_TOOL_IS_ALMOST_BROKEN.getMessage()));
            return;
        }
        item.setData(DataComponentTypes.DAMAGE, currentDamage + 1);
    }

    public boolean hasSeeds(Player player, Material seedMaterial) {
        return player.getInventory().contains(ItemStack.of(seedMaterial));
    }

    public void handleSeeds(Player player, boolean rightClick, BlockData blockData) {
        if (rightClick && !ConfigHandler.getInstance().doesRightClickReplantRequireSeeds()) return;
        if (!rightClick && !ConfigHandler.getInstance().doesLeftClickReplantRequireSeeds()) return;
        int location = player.getInventory().first(ItemStack.of(blockData.getPlacementMaterial()));
        ItemStack seeds = player.getInventory().getItem(location);
        if (seeds == null) return;
        seeds.subtract(1);
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

    public void playHarvestEffects(Block block) {
        handleSound(block);
        handleParticles(block);
    }

    public void handleSound(Block block) {
        if (!ConfigHandler.getInstance().shouldPlaySounds()) return;
        Location location = block.getLocation();
        Sound sound = ConfigHandler.getInstance().getConfigSound();
        float volume = ConfigHandler.getInstance().getSoundVolume();
        float pitch = ConfigHandler.getInstance().getSoundPitch();
        location.getWorld().playSound(location, sound, SoundCategory.BLOCKS, volume, pitch);
    }

    private void handleParticles(Block block) {
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

}
