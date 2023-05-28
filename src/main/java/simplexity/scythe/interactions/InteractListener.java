package simplexity.scythe.interactions;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.events.HarvestEvent;
import simplexity.scythe.events.PlantEvent;

public class InteractListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerHarvest(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        boolean isRightClick = event.getAction().isRightClick();
        Block block = event.getClickedBlock();
        BlockData replantBlockData = event.getClickedBlock().getBlockData();
        Player player = event.getPlayer();
        ItemStack itemUsed = player.getInventory().getItemInMainHand();
        HarvestEvent harvestEvent = new HarvestEvent(player, block, isRightClick, itemUsed);
        Bukkit.getPluginManager().callEvent(harvestEvent);
        if (harvestEvent.isCancelled()) {
            return;
        }
        harvestEvent.preHarvestChecks();
        if (harvestEvent.isCancelled()) {
            return;
        }
        if (!harvestEvent.isCropFullGrown() && player.isSneaking()) {
            return;
        }
        event.setCancelled(true);
        harvestEvent.harvestCrop();
        if (harvestEvent.isCancelled()) return;
        PlantEvent plantEvent = new PlantEvent(player, block, replantBlockData);
        Bukkit.getPluginManager().callEvent(plantEvent);
        if (plantEvent.isCancelled()) return;
        if (ConfigHandler.getInstance().shouldRequireToolReplant() && !harvestEvent.wasConfiguredToolUsed()) {
            plantEvent.setCancelled(true);
            return;
        }
        plantEvent.prePlantChecks();
        if (plantEvent.isCancelled()) return;
        plantEvent.replantCrop();
    }
}



