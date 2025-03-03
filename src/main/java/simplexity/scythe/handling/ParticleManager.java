package simplexity.scythe.handling;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import simplexity.scythe.config.ConfigHandler;

public class ParticleManager {
    private static ParticleManager instance;

    public ParticleManager() {
    }

    public static ParticleManager getInstance() {
        if (instance == null) instance = new ParticleManager();
        return instance;
    }

    public void handleParticles(Block block, BlockData originalBlockData, Particle particle, int count, double spread) {
        if (!ConfigHandler.getInstance().harvestParticlesEnabled()) return;
        Location location = block.getLocation().toCenterLocation();
        if (particle.getDataType() == BlockData.class) {
            spawnParticles(location, originalBlockData, particle, count, spread);
        } else {
            spawnParticles(location, particle, count, spread);
        }
    }

    private void spawnParticles(Location location, BlockData blockData, Particle particle, int count, double spread) {
        World world = location.getWorld();
        for (int i = 0; i < count; i++) {
            double offsetX = (Math.random() - 0.5) * spread;
            double offsetY = (Math.random() - 0.5) * spread;
            double offsetZ = (Math.random() - 0.5) * spread;
            world.spawnParticle(
                    particle,
                    location.getX() + offsetX,
                    location.getY() + offsetY,
                    location.getZ() + offsetZ,
                    1,
                    blockData
            );
        }
    }

    private void spawnParticles(Location location, Particle particle, int count, double spread) {
        World world = location.getWorld();
        for (int i = 0; i < count; i++) {
            double offsetX = (Math.random() - 0.5) * spread;
            double offsetY = (Math.random() - 0.5) * spread;
            double offsetZ = (Math.random() - 0.5) * spread;
            world.spawnParticle(
                    particle,
                    location.getX() + offsetX,
                    location.getY() + offsetY,
                    location.getZ() + offsetZ,
                    1
            );
        }
    }
}
