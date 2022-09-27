package adhdmc.scythe;

import adhdmc.scythe.Commands.SubCommands.ToggleCommand;
import com.destroystokyo.paper.MaterialSetTag;
import com.destroystokyo.paper.MaterialTags;
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

import java.util.ArrayList;

public class InteractListener implements Listener {

    private static final ArrayList<Material> configuredCrops = ConfigHandler.configuredCrops;
    private static final NamespacedKey functionToggle = ToggleCommand.functionToggle;
    private static final String usePermission = Scythe.usePermission;
    private static final boolean requireHoe = ConfigHandler.requireHoe;
    private static final boolean rightClickHarvest = ConfigHandler.rightClickHarvest;

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerHarvest(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Material clickedMaterial = event.getClickedBlock().getType();
        if (!configuredCrops.contains(clickedMaterial)) {
            return;
        }
        if (rightClickHarvest && event.getAction().isLeftClick()){
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
        if (!player.hasPermission(usePermission)){
            return;
        }
        if (playerPDC.has(functionToggle, PersistentDataType.STRING) && playerPDC.get(functionToggle, PersistentDataType.STRING).equals(Scythe.replantingDisabled)){
            return;
        }
        if (requireHoe && !MaterialTags.HOES.isTagged(itemUsed)){
            return;
        }
        if (clickedMaterial.equals(Material.COCOA)){
            Cocoa clickedCocoa = (Cocoa) event.getClickedBlock().getBlockData();
            facing = clickedCocoa.getFacing();
            Cocoa cocoaData = (Cocoa) Bukkit.createBlockData(Material.COCOA);
            event.setCancelled(true);
            event.getClickedBlock().breakNaturally(itemUsed);
            cocoaData.setFacing(facing);
            clickedSpot.setBlockData(cocoaData);
            return;
            }
        event.setCancelled(true);
        event.getClickedBlock().breakNaturally(itemUsed);
        event.getClickedBlock().setType(clickedMaterial);
    }
}



