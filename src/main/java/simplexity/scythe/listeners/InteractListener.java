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
import org.bukkit.inventory.ItemStack;
import simplexity.scythe.events.HarvestEvent;
import simplexity.scythe.events.ReplantEvent;
import simplexity.scythe.handling.HarvestHandler;
import simplexity.scythe.handling.ReplantHandler;

public class InteractListener implements Listener {


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerHarvest(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getHand() == null) return;
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        BlockData replantBlockData = event.getClickedBlock().getBlockData();
        boolean rightClick = event.getAction().isRightClick();
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (!harvestCrop(event, player, block, rightClick)) return;

        replantCrop(player, block, replantBlockData, rightClick);


    }

    private boolean harvestCrop(PlayerInteractEvent event, Player player, Block block, boolean rightClick) {
        if (!HarvestHandler.getInstance().passesPreHarvestChecks(player, block, rightClick)) return false;


        HarvestEvent harvestEvent = runHarvestEvent(player, block, rightClick);
        if (harvestEvent == null) return false;

        HarvestHandler.getInstance().harvestCrop(player, block);

        event.setCancelled(true);
        return true;
    }

    private HarvestEvent runHarvestEvent(Player player, Block block, boolean rightClick) {
        ItemStack itemUsed = player.getInventory().getItemInMainHand();
        HarvestEvent harvestEvent = new HarvestEvent(player, block, rightClick, itemUsed);
        Bukkit.getPluginManager().callEvent(harvestEvent);
        if (harvestEvent.isCancelled()) return null;
        return harvestEvent;
    }

    private void replantCrop(Player player, Block block, BlockData blockData, boolean rightClick) {
        if (!ReplantHandler.getInstance().passesPreChecks(player, rightClick)) return;
        ReplantEvent replantEvent = runReplantEvent(player, block, blockData);
        if (replantEvent == null) return;
        ReplantHandler.getInstance().replantCrop(block, blockData, player);
    }

    private ReplantEvent runReplantEvent(Player player, Block block, BlockData originalBlockData) {
        ReplantEvent replantEvent = new ReplantEvent(player, block, originalBlockData);
        Bukkit.getPluginManager().callEvent(replantEvent);
        if (replantEvent.isCancelled()) return null;
        return replantEvent;
    }
}



