package simplexity.scythe.handling;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.ScythePermission;

public class ReplantManager {
    private static ReplantManager instance;

    public static ReplantManager getInstance() {
        if (instance == null) instance = new ReplantManager();
        return instance;
    }

    public ReplantManager() {
    }


    public boolean canReplant(Player player, BlockData blockData) {
        if (!player.hasPermission(ScythePermission.USE_REPLANT.getPermission())) return false;
        if (!Util.getInstance().playerHasToggleEnabled(player)) return false;
        if (player.isSneaking()) return false;
        if (ConfigHandler.getInstance().autoReplantRequiresTool() && !Util.getInstance().requiredToolUsed(player))
            return false;
        if (ConfigHandler.getInstance().requireSeeds() && !SeedsManager.getInstance().hasSeeds(player, blockData.getPlacementMaterial()))
            return false;
        return true;
    }


    public void replantCrop(Player player, Block block, BlockData originalBlockData) {
        if (!(originalBlockData instanceof Ageable newCropData)) return;
        if (!originalBlockData.isSupported(block.getLocation())) return;
        if (ConfigHandler.getInstance().replantUsesToolDurability() && !DurabilityManager.getInstance().durabilitySuccessfullyRemoved(player))
            return;
        if (!SeedsManager.getInstance().seedsHandledProperly(player, originalBlockData.getPlacementMaterial())) return;
        newCropData.setAge(0);
        block.setBlockData(newCropData);
        Util.getInstance().logCoreProtectPlacement(player, block);
        playReplantEffects(block, newCropData);
    }

    private void playReplantEffects(Block block, BlockData newBlockData) {
        Util.getInstance().handleSound(block, ConfigHandler.getInstance().getPlantSound());
        if (!ConfigHandler.getInstance().replantParticlesEnabled()) return;
        ParticleManager.getInstance().handleParticles(
                block,
                newBlockData,
                ConfigHandler.getInstance().getReplantParticle(),
                ConfigHandler.getInstance().getReplantParticleCount(),
                ConfigHandler.getInstance().getReplantParticleSpread());
    }


}
