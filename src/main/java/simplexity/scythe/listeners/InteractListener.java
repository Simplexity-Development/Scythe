package simplexity.scythe.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.events.HarvestEvent;
import simplexity.scythe.handling.HarvestManager;

public class InteractListener implements Listener {


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getHand() == null) return;
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (event.getAction().isLeftClick()) return;
        if (!ConfigHandler.getInstance().allowRightClickHarvest()) return;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (!HarvestManager.getInstance().canHarvest(player, block)) return;
        HarvestEvent harvestEvent = runHarvestEvent(player, block);
        if (harvestEvent == null) return;
        event.setCancelled(true);
        HarvestManager.getInstance().harvestCrop(
                harvestEvent.getPlayer(),
                harvestEvent.getBlock());
    }

    private HarvestEvent runHarvestEvent(Player player, Block block) {
        HarvestEvent harvestEvent = new HarvestEvent(player, block);
        Bukkit.getPluginManager().callEvent(harvestEvent);
        if (harvestEvent.isCancelled()) return null;
        return harvestEvent;
    }


}



