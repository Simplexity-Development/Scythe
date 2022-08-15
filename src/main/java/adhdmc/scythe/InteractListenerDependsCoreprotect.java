package adhdmc.scythe;

import com.destroystokyo.paper.MaterialTags;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Cocoa;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class InteractListenerDependsCoreprotect implements Listener {

    CoreProtectAPI api = getCoreProtect();

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void rightClickFarmable(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Material clickedMaterial = event.getClickedBlock().getType();
        if (!Tag.CROPS.isTagged(clickedMaterial) && clickedMaterial != Material.COCOA && clickedMaterial != Material.NETHER_WART) {
            return;
        }
        Player player = event.getPlayer();
        Block clickedSpot = event.getClickedBlock();
        Ageable clickedCrop = (Ageable) clickedSpot.getBlockData();
        ItemStack itemUsed = player.getInventory().getItemInMainHand();
        BlockFace facing;
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        if (clickedCrop.getMaximumAge() != clickedCrop.getAge()) {
            return;
        }
        if (!player.hasPermission("scythe.use")){
            return;
        }
        if (playerPDC.has(new NamespacedKey(Scythe.plugin, "toggle"), PersistentDataType.STRING) && playerPDC.get(new NamespacedKey(Scythe.plugin, "toggle"), PersistentDataType.STRING).equals("false")){
            return;
        }
        if (MessageHandler.requireHoe && !MaterialTags.HOES.isTagged(itemUsed)) {
            return;
        }
        if (clickedMaterial.equals(Material.COCOA)){
            Cocoa clickedCocoa = (Cocoa) event.getClickedBlock().getBlockData();
            facing = clickedCocoa.getFacing();
            Cocoa cocoaData = (Cocoa) Bukkit.createBlockData(Material.COCOA);
            event.setCancelled(true);
            api.logRemoval(
                player.getName(),
                clickedSpot.getLocation(),
                clickedSpot.getType(),
                clickedCocoa);
            event.getClickedBlock().breakNaturally(itemUsed);
            cocoaData.setFacing(facing);
            clickedSpot.setBlockData(cocoaData);
            api.logPlacement(
                player.getName(),
                clickedSpot.getLocation(),
                clickedSpot.getType(),
                cocoaData);
            return;
            }
        event.setCancelled(true);
        api.logRemoval(
            player.getName(),
            clickedSpot.getLocation(),
            clickedSpot.getType(),
             null);
        event.getClickedBlock().breakNaturally(itemUsed);
        event.getClickedBlock().setType(clickedMaterial);
        api.logPlacement(
            player.getName(),
            clickedSpot.getLocation(),
            clickedSpot.getType(),
            null);
    }
    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
        // Check that CoreProtect is loaded
        if (!(plugin instanceof CoreProtect)) {
            return null;
        }
        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (!CoreProtect.isEnabled()) {
            return null;
        }
        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 9) {
            return null;
        }
        return CoreProtect;
    }
}


