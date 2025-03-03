package simplexity.scythe.handling;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.coreprotect.CoreProtectAPI;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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


    public boolean requiredToolUsed(Player player) {
        Material itemUsed = player.getInventory().getItemInMainHand().getType();
        return ConfigHandler.getInstance().getEnabledTools().contains(itemUsed)
               && hasItemModel(player.getInventory().getItemInMainHand());
    }

    private boolean hasItemModel(ItemStack item) {
        Set<NamespacedKey> itemModels = ConfigHandler.getInstance().getRequiredItemModels();
        if (itemModels.isEmpty()) return true;
        Key modelKey = item.getData(DataComponentTypes.ITEM_MODEL);
        return itemModels.contains((NamespacedKey) modelKey);
    }

    public void handleSound(Block block, Sound sound) {
        if (!ConfigHandler.getInstance().soundsEnabled()) return;
        Location location = block.getLocation();
        float volume = ConfigHandler.getInstance().getSoundVolume();
        float pitch = ConfigHandler.getInstance().getSoundPitch();
        location.getWorld().playSound(location, sound, SoundCategory.BLOCKS, volume, pitch);
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


    public boolean playerHasToggleEnabled(Player player) {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.getOrDefault(ToggleCommand.toggleKey, PersistentDataType.BOOLEAN, Boolean.TRUE);
    }

    public boolean isCropFullGrown(Ageable ageable) {
        return ageable.getAge() == ageable.getMaximumAge();
    }
}
