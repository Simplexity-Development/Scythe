package adhdmc.scythe.listeners;

import adhdmc.scythe.Scythe;
import adhdmc.scythe.commands.subcommands.ToggleCommand;
import adhdmc.scythe.config.ConfigHandler;
import adhdmc.scythe.config.Defaults;
import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

import java.util.ArrayList;
import java.util.List;

public class InteractListener implements Listener {

    private final List<Material> configuredCrops = ConfigHandler.getConfiguredCrops();
    private final NamespacedKey functionToggle = ToggleCommand.functionToggle;
    private static final String usePermission = Defaults.getPermMap().get(Defaults.permissions.USE);
    private final boolean requireHoe = ConfigHandler.isRequireHoe();
    private final boolean rightClickHarvest = ConfigHandler.isRightClickHarvest();

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
        if (!rightClickHarvest && event.getAction().isRightClick()){
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
        Byte playerPDCValue = playerPDC.get(functionToggle, PersistentDataType.BYTE);
        if (playerPDCValue != null && playerPDCValue.equals((byte)0)){
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



