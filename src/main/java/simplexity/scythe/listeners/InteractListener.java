package simplexity.scythe.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import simplexity.scythe.events.HarvestEvent;
import simplexity.scythe.events.ReplantEvent;
import simplexity.scythe.handling.CropManager;

public class InteractListener implements Listener {


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getHand() == null) return;
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        CropManager cropManager = CropManager.getInstance();
        boolean rightClick = event.getAction().isRightClick();
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (!cropManager.canHarvest(player, block, rightClick)) return;
        HarvestEvent harvestEvent = runHarvestEvent(player, block, rightClick);
        if (harvestEvent == null) return;
        BlockData replantBlockData = event.getClickedBlock().getBlockData();
        cropManager.harvestCrop(
                harvestEvent.getPlayer(),
                harvestEvent.getBlock(),
                harvestEvent.isRightClick());
        event.setCancelled(true);
        if (!cropManager.canReplant(player, replantBlockData, rightClick)) return;
        ReplantEvent replantEvent = runReplantEvent(player, block, replantBlockData, rightClick);
        if (replantEvent == null) return;
        cropManager.replantCrop(
                replantEvent.getPlayer(),
                replantEvent.getBlock(),
                replantEvent.getBlockData());
    }

    private HarvestEvent runHarvestEvent(Player player, Block block, boolean rightClick) {
        HarvestEvent harvestEvent = new HarvestEvent(player, block, rightClick);
        Bukkit.getPluginManager().callEvent(harvestEvent);
        if (harvestEvent.isCancelled()) return null;
        return harvestEvent;
    }

    private ReplantEvent runReplantEvent(Player player, Block block, BlockData originalBlockData, boolean rightClick) {
        ReplantEvent replantEvent = new ReplantEvent(player, block, originalBlockData, rightClick);
        Bukkit.getPluginManager().callEvent(replantEvent);
        if (replantEvent.isCancelled()) return null;
        return replantEvent;
    }

}



