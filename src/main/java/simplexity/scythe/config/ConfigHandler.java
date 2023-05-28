package simplexity.scythe.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import simplexity.scythe.Scythe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ConfigHandler {
    private static ConfigHandler instance;

    private ConfigHandler() {
    }

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }


    private final Logger logger = Scythe.getScytheLogger();
    private boolean requireToolReplant;
    private boolean requireToolRightClickHarvest;
    private boolean rightClickHarvest;
    private boolean playSounds;
    private Sound configSound;
    private Particle configParticle;
    private int soundVolume;
    private int soundPitch;
    private boolean breakParticles;
    private int particleCount;
    private final ArrayList<Material> configuredCrops = new ArrayList<>();
    private final ArrayList<ItemStack> configuredTools = new ArrayList<>();

    public void configParser() {
        FileConfiguration config = Scythe.getInstance().getConfig();
        Message.reloadMessages();
        setConfiguredCrops();
        setConfiguredTools();
        checkSound();
        checkParticle();
        rightClickHarvest = config.getBoolean("right-click-to-harvest");
        requireToolReplant = config.getBoolean("require-tool-for-replant");
        requireToolRightClickHarvest = config.getBoolean("require-tool-for-right-click-harvest");
        playSounds = config.getBoolean("play-sounds");
        soundVolume = config.getInt("sound-volume");
        soundPitch = config.getInt("sound-pitch");
        breakParticles = config.getBoolean("break-particles");
        particleCount = config.getInt("particle-count");
    }

    private void checkSound() {
        String sound = Scythe.getInstance().getConfig().getString("sound");
        try {
            configSound = Sound.valueOf(sound);
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
            Scythe.getScytheLogger().warning("Setting particle to BLOCK_DUST until a valid particle is provided");
        }

    }


    private void setConfiguredCrops() {
        configuredCrops.clear();
        List<String> cropList = Scythe.getInstance().getConfig().getStringList("allowed-crops");
        for (String configCrop : cropList) {
            if (Material.matchMaterial(configCrop) == null) {
                logger.warning(Message.CONSOLE_PREFIX.getMessage() + configCrop + " is not a valid material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            Material crop = Material.matchMaterial(configCrop);
            assert crop != null;
            if (crop == Material.COCOA) {
                configuredCrops.add(Material.COCOA);
                continue;
            }
            if (!crop.isBlock()) {
                logger.warning(Message.CONSOLE_PREFIX.getMessage() + configCrop + " is not a valid block material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            BlockData cropBlock = Bukkit.createBlockData(crop);
            try {
                Ageable ageableCrop = (Ageable) cropBlock;
            } catch (ClassCastException exception) {
                logger.warning(Message.CONSOLE_PREFIX.getMessage() + configCrop + " is not a valid crop material. Please check to be sure you spelled everything correctly.");
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
                ItemStack toolItem = Bukkit.getItemFactory().createItemStack(tool);
                configuredTools.add(toolItem.asOne());
            } catch (IllegalArgumentException e) {
                Scythe.getScytheLogger().warning(tool + " could not be cast to an ItemStack. Please make sure your syntax is correct");
            }
        }
    }


    public boolean allowRightClickHarvest() {
        return rightClickHarvest;
    }

    public boolean shouldRequireToolReplant() {
        return requireToolReplant;
    }

    public boolean shouldRequireToolRightClickHarvest() {
        return requireToolRightClickHarvest;
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

    public List<ItemStack> getConfiguredTools() {
        return Collections.unmodifiableList(configuredTools);
    }

    public Sound getConfigSound() {
        return configSound;
    }

    public int getSoundVolume() {
        return soundVolume;
    }

    public int getSoundPitch() {
        return soundPitch;
    }

    public Particle getConfigParticle() {
        return configParticle;
    }

    public int getParticleCount() {
        return particleCount;
    }
}