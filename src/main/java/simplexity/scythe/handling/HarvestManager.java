package simplexity.scythe.handling;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.ScythePermission;

public class HarvestManager {

    private static HarvestManager instance;

    public HarvestManager() {
    }

    public static HarvestManager getInstance() {
        if (instance == null) instance = new HarvestManager();
        return instance;
    }

    public boolean canHarvest(Player player, Block block) {
        if (!player.hasPermission(ScythePermission.USE_HARVEST.getPermission())) return false;
        if (!Util.getInstance().playerHasToggleEnabled(player)) return false;
        if (!ConfigHandler.getInstance().autoReplantEnabled()) return false;
        if (!(block.getBlockData() instanceof Ageable ageable)) return false;
        if (!Util.getInstance().isCropFullGrown(ageable)) return false;
        if (!ConfigHandler.getInstance().getConfiguredCrops().contains(block.getType())) return false;
        if (ConfigHandler.getInstance().rightClickHarvestRequiresTool() && !Util.getInstance().requiredToolUsed(player))
            return false;
        return true;
    }

    public void harvestCrop(Player player, Block block) {
        if (ConfigHandler.getInstance().harvestUsesToolDurability() &&
            !DurabilityManager.getInstance().durabilitySuccessfullyRemoved(player)) return;
        BlockData originalBlockData = block.getBlockData().clone();
        player.breakBlock(block);
        Util.getInstance().logCoreProtectRemoval(player, block);
        playHarvestEffects(block, originalBlockData);
    }

    private void playHarvestEffects(Block block, BlockData originalBlockData) {
        Util.getInstance().handleSound(block, ConfigHandler.getInstance().getBreakSound());
        ParticleManager.getInstance().handleParticles(
                block,
                originalBlockData,
                ConfigHandler.getInstance().getBreakParticle(),
                ConfigHandler.getInstance().getHarvestParticleCount(),
                ConfigHandler.getInstance().getHarvestParticleSpread());
    }


}
