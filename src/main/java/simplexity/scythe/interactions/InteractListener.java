package simplexity.scythe.interactions;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import simplexity.scythe.Scythe;
import simplexity.scythe.events.HarvestEvent;

import java.util.logging.Logger;

public class InteractListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void playerHarvest(PlayerInteractEvent event) {
        Logger logger = Scythe.getScytheLogger();
        if (event.getClickedBlock() == null) {
            logger.info("Clicked block is null, returning");
            return;
        }
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            logger.info("No hand detected or the slot is offhand - returning");
            return;
        }
        boolean isRightClick = event.getAction().isRightClick();
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack itemUsed = player.getActiveItem();
        HarvestEvent harvestEvent = new HarvestEvent(player, block, isRightClick, itemUsed);
        logger.info("Calling Event");
        Bukkit.getPluginManager().callEvent(harvestEvent);
        if (harvestEvent.isCancelled()) {
            logger.info("was cancelled before pre-check, returning");
            return;
        }
        harvestEvent.preHarvestChecks();
        logger.info("running pre-checks");
        if (harvestEvent.isCancelled()) {
            logger.info("event was cancelled after checks, returning");
            return;
        }
        if (!harvestEvent.isCropFullGrown() && player.isSneaking()) {
            logger.info("crop is not full grown but player is sneaking, returning");
            return;
        }
        event.setCancelled(true);
        logger.info("cancelled main event");
        harvestEvent.harvestCrop();
        logger.info("ran 'harvest crop'");
    }
}



