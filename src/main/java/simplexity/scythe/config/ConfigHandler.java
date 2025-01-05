package simplexity.scythe.config;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import simplexity.scythe.Scythe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Verifies configuration and sets values
 */
public class ConfigHandler {
    private static ConfigHandler instance;

    private ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }


    private final Logger logger = Scythe.getScytheLogger();
    private boolean leftClickReplantRequireTool, rightClickReplantRequireTool, rightClickHarvestRequireTool,
            leftClickReplant, rightClickHarvest, rightClickReplant, playSounds;
    private Sound configSound;
    private Particle configParticle;
    private float soundVolume;
    private float soundPitch;
    private boolean breakParticles;
    private int particleCount;
    private final ArrayList<Material> configuredCrops = new ArrayList<>();
    private final ArrayList<Material> configuredTools = new ArrayList<>();

    public void configParser() {
        FileConfiguration config = Scythe.getInstance().getConfig();
        LocaleHandler.getInstance().reloadLocale();
        setConfiguredCrops();
        setConfiguredTools();
        checkSound();
        checkParticle();
        leftClickReplant = config.getBoolean("left-click.replant.enabled", true);
        leftClickReplantRequireTool = config.getBoolean("left-click.replant.require-tool", false);
        rightClickHarvest = config.getBoolean("right-click.harvest.enabled", true);
        rightClickHarvestRequireTool = config.getBoolean("right-click.harvest.require-tool", false);
        rightClickReplant = config.getBoolean("right-click.replant.enabled", true);
        rightClickReplantRequireTool = config.getBoolean("right-click.replant.require-tool", false);
        playSounds = config.getBoolean("play-sounds");
        if (0 < config.getDouble("sound-volume") && config.getDouble("sound-volume") < 2) {
            soundVolume = (float) config.getDouble("sound-volume");
        } else {
            logger.warning("Configured of " + config.getDouble("sound-volume") + " was unable to be set. Please be sure you've chosen a number between 0 and 2");
            soundVolume  = 1f;
        }
        if (0 < config.getDouble("sound-pitch") && config.getDouble("sound-pitch") < 2) {
            soundPitch = (float) config.getDouble("sound-pitch");
        } else {
            logger.warning("Configured of " + config.getDouble("sound-pitch") + " was unable to be set. Please be sure you've chosen a number between 0 and 2");
            soundPitch  = 1f;
        }
        breakParticles = config.getBoolean("break-particles");
        particleCount = config.getInt("particle-count");
    }

    private void checkSound() {
        String sound = Scythe.getInstance().getConfig().getString("sound");
        try {
            configSound = Registry.SOUNDS.get(NamespacedKey.fromString(sound.toLowerCase(Locale.ROOT)));
        } catch (IllegalArgumentException | NullPointerException e) {
            Scythe.getScytheLogger().warning(sound + " could not be cast to a sound. Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
            Scythe.getScytheLogger().warning("Setting sound to BLOCK_CROP_BREAK until a valid sound is provided");
            configSound = Sound.BLOCK_CROP_BREAK;
        }
    }

    private void checkParticle() {
        String particle = Scythe.getInstance().getConfig().getString("particle");
        try {
            configParticle = Particle.valueOf(particle);
        } catch (IllegalArgumentException | NullPointerException e) {
            Scythe.getScytheLogger().warning(particle + " could not be cast to a particle. Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Particle.html");
            Scythe.getScytheLogger().warning("Setting particle to CRIT until a valid particle is provided");
            configParticle = Particle.CRIT;
        }

    }


    private void setConfiguredCrops() {
        configuredCrops.clear();
        List<String> cropList = Scythe.getInstance().getConfig().getStringList("allowed-crops");
        for (String configCrop : cropList) {
            if (Material.matchMaterial(configCrop) == null) {
                logger.warning( configCrop + " is not a valid material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            Material crop = Material.matchMaterial(configCrop);
            assert crop != null;
            if (crop == Material.COCOA) {
                configuredCrops.add(Material.COCOA);
                continue;
            }
            if (!crop.isBlock()) {
                logger.warning(configCrop + " is not a valid block material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            BlockData cropBlock = Bukkit.createBlockData(crop);
            try {
                //noinspection unused - This is needed to check that the config is valid
                Ageable ageableCrop = (Ageable) cropBlock;
            } catch (ClassCastException exception) {
                logger.warning(configCrop + " is not a valid crop material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            configuredCrops.add(crop);
        }
    }

    private void setConfiguredTools() {
        configuredTools.clear();
        List<String> stringToolList = Scythe.getInstance().getConfig().getStringList("replant-tools");
        for (String tool : stringToolList) {
            try {
                Material material = Material.matchMaterial(tool);
                if (material == null) {
                    Scythe.getScytheLogger().warning(tool + " is not a valid material. Please check your syntax");
                    continue;
                }
                configuredTools.add(material);
            } catch (IllegalArgumentException e) {
                Scythe.getScytheLogger().warning(tool + " could not be cast to an ItemStack. Please make sure your syntax is correct");
            }
        }
    }

    public boolean allowLeftClickReplant() {
        return leftClickReplant;
    }

    public boolean allowRightClickHarvest() {
        return rightClickHarvest;
    }

    public boolean shouldRequireToolLeftClickReplant() {
        return leftClickReplantRequireTool;
    }

    public boolean shouldRequireToolRightClickReplant() {
        return rightClickReplantRequireTool;
    }

    public boolean showBreakParticles() {
        return breakParticles;
    }

    public boolean shouldPlaySounds() {
        return playSounds;
    }

    public List<Material> getConfiguredCrops() {
        return Collections.unmodifiableList(configuredCrops);
    }

    public List<Material> getConfiguredTools() {
        return Collections.unmodifiableList(configuredTools);
    }

    public Sound getConfigSound() {
        return configSound;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public float getSoundPitch() {
        return soundPitch;
    }

    public Particle getConfigParticle() {
        return configParticle;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public boolean isRightClickReplant() {
        return rightClickReplant;
    }

    public boolean isRightClickHarvestRequireTool() {
        return rightClickHarvestRequireTool;
    }
}
