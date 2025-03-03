package simplexity.scythe.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import simplexity.scythe.Scythe;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.events.ReplantEvent;
import simplexity.scythe.handling.ReplantManager;
import simplexity.scythe.handling.Util;

public class BlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!ConfigHandler.getInstance().getConfiguredCrops().contains(block.getType())) return;
        if (!(block.getBlockData() instanceof Ageable ageableBlock)) return;
        if (!Util.getInstance().isCropFullGrown(ageableBlock)) return;

        Player player = event.getPlayer();
        if (!ReplantManager.getInstance().canReplant(player, ageableBlock)) return;
        BlockData copyOfAgeableBlock = ageableBlock.clone();
        ReplantEvent replantEvent = runReplantEvent(player, block, copyOfAgeableBlock);
        if (replantEvent == null) return;
        Bukkit.getScheduler().runTaskLater(Scythe.getInstance(), () -> {
            ReplantManager.getInstance().replantCrop(
                    replantEvent.getPlayer(),
                    replantEvent.getBlock(),
                    replantEvent.getBlockData());
        }, ConfigHandler.getInstance().getDelayTicks());
    }

    private ReplantEvent runReplantEvent(Player player, Block block, BlockData originalBlockData) {
        ReplantEvent replantEvent = new ReplantEvent(player, block, originalBlockData);
        Bukkit.getPluginManager().callEvent(replantEvent);
        if (replantEvent.isCancelled()) return null;
        return replantEvent;
    }

}
