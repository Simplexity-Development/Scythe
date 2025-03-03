package simplexity.scythe.config;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import simplexity.scythe.Scythe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private boolean autoReplantEnabled, requireSeeds, autoReplantRequiresTool, rightClickHarvest,
            rightClickHarvestRequiresTool, harvestUsesToolDurability, replantUsesToolDurability, preventToolBreak,
            soundsEnabled, harvestParticlesEnabled, replantParticlesEnabled;
    private Sound breakSound, plantSound;
    private Particle breakParticle, replantParticle;
    private float soundVolume, soundPitch;
    private double harvestParticleSpread, replantParticleSpread;
    private int harvestParticleCount, replantParticleCount, minimumDurability, delayTicks;
    private final ArrayList<Material> configuredCrops = new ArrayList<>();
    private final ArrayList<Material> enabledTools = new ArrayList<>();
    private final HashSet<NamespacedKey> requiredItemModels = new HashSet<>();

    public void configParser() {
        FileConfiguration config = Scythe.getInstance().getConfig();
        LocaleHandler.getInstance().reloadLocale();
        setConfiguredCrops(config);
        autoReplantEnabled = config.getBoolean("auto-replant.enabled", true);
        requireSeeds = config.getBoolean("auto-replant.require-seeds", false);
        autoReplantRequiresTool = config.getBoolean("auto-replant.require-tool", false);
        delayTicks = config.getInt("auto-replant.delay-ticks", 1);
        rightClickHarvest = config.getBoolean("right-click-harvest.enabled", true);
        rightClickHarvestRequiresTool = config.getBoolean("right-click-harvest.require-tool", false);
        soundsEnabled = config.getBoolean("sounds.enabled", true);
        harvestParticlesEnabled = config.getBoolean("particles.harvest.enabled", true);
        replantParticlesEnabled = config.getBoolean("particles.replant.enabled", true);
        if (soundsEnabled) loadSoundConfigurations(config);
        if (replantParticlesEnabled || harvestParticlesEnabled) loadParticleInfo(config);
        if (autoReplantRequiresTool || rightClickHarvestRequiresTool) loadToolInfo(config);
    }

    private void loadSoundConfigurations(FileConfiguration config) {
        double configuredVolume = config.getDouble("sounds.volume");
        if (0 < configuredVolume && configuredVolume < 2) {
            soundVolume = (float) configuredVolume;
        } else {
            logger.warning("Configured Volume of " + configuredVolume
                           + " was unable to be set. Please be sure you've chosen a number between 0 and 2");
            soundVolume = 1f;
        }
        double configuredPitch = config.getDouble("sounds.pitch");
        if (0 < configuredPitch && configuredPitch < 2) {
            soundPitch = (float) configuredPitch;
        } else {
            logger.warning("Configured Pitch of " + configuredPitch
                           + " was unable to be set. Please be sure you've chosen a number between 0 and 2");
            soundPitch = 1f;
        }
        checkSound(config);
    }

    private void loadParticleInfo(FileConfiguration config) {
        harvestParticleCount = config.getInt("particles.harvest.count", 20);
        harvestParticleSpread = config.getDouble("particles.harvest.spread", 0.5);
        replantParticleCount = config.getInt("particles.replant.count", 2);
        replantParticleSpread = config.getDouble("particles.replant.spread", 0.5);
        checkParticle(config);
    }

    private void loadToolInfo(FileConfiguration config) {
        setConfiguredTools(config);
        verifyItemModels(config);
        harvestUsesToolDurability = config.getBoolean("tools.durability.harvest-uses-durability", false);
        replantUsesToolDurability = config.getBoolean("tools.durability.replant-uses-durability", false);
        preventToolBreak = config.getBoolean("tools.durability.prevent-tool-break", true);
        minimumDurability = config.getInt("tools.durability.minimum-durability", 10);

    }

    @SuppressWarnings("deprecation")
    private void checkSound(FileConfiguration config) {
        String configuredBreakSound = config.getString("sounds.break-sound");
        String configuredReplantSound = config.getString("sounds.plant-sound");
        if (configuredBreakSound == null) {
            logger.warning("No break-sound was found! Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
            logger.warning("Setting sound to BLOCK_CROP_BREAK until a valid sound is provided");
            breakSound = Sound.BLOCK_CROP_BREAK;
        } else try {
            breakSound = Sound.valueOf(configuredBreakSound);
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warning(configuredBreakSound + " could not be cast to a sound. Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
            logger.warning("Setting sound to BLOCK_CROP_BREAK until a valid sound is provided");
            breakSound = Sound.BLOCK_CROP_BREAK;
        }
        if (configuredReplantSound == null) {
            logger.warning("No replant sound was fount! Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
            logger.warning("Setting sound to ITEM_CROP_PLANT until a valid sound is provided");
            plantSound = Sound.ITEM_CROP_PLANT;
        } else try {
            plantSound = Sound.valueOf(configuredReplantSound);
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warning(configuredReplantSound + " could not be cast to a sound! Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html");
            logger.warning("Setting sound to ITEM_CROP_PLANT until a valid sound is provided");
            plantSound = Sound.ITEM_CROP_PLANT;
        }
    }

    private void checkParticle(FileConfiguration config) {
        String configuredBreakParticle = config.getString("particles.harvest.particle");
        String configuredReplantParticle = config.getString("particles.replant.particle");
        try {
            breakParticle = Particle.valueOf(configuredBreakParticle);
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warning(configuredBreakParticle + " could not be cast to a particle. Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Particle.html");
            logger.warning("Setting particle to BLOCK until a valid particle is provided");
            breakParticle = Particle.BLOCK;
        }
        try {
            replantParticle = Particle.valueOf(configuredReplantParticle);
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.warning(configuredBreakParticle + " could not be cast to a particle. Please check your syntax and be sure you are choosing a sound from https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Particle.html");
            logger.warning("Setting particle to BLOCK until a valid particle is provided");
            breakParticle = Particle.BLOCK;
        }

    }

    private void verifyItemModels(FileConfiguration config) {
        requiredItemModels.clear();
        List<String> itemModelStrings = config.getStringList("replant-tools-item-models");
        if (itemModelStrings.isEmpty()) return;
        for (String itemModel : itemModelStrings) {
            String[] split = itemModel.split(":");
            if (split.length != 2) {
                logger.warning(itemModel + " is not a valid item model, these must be declared as \"namespace:location\", please check your syntax");
                continue;
            }
            NamespacedKey key = new NamespacedKey(split[0], split[1]);
            requiredItemModels.add(key);
        }
    }


    private void setConfiguredCrops(FileConfiguration config) {
        configuredCrops.clear();
        List<String> cropList = config.getStringList("allowed-crops");
        for (String configCrop : cropList) {
            Material crop = Material.matchMaterial(configCrop);
            if (crop == null) {
                logger.warning(configCrop + " is not a valid material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            if (crop == Material.COCOA) {
                configuredCrops.add(Material.COCOA);
                continue;
            }
            if (!crop.isBlock()) {
                logger.warning(configCrop + " is not a valid block material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            BlockData cropBlock = Bukkit.createBlockData(crop);
            if (!(cropBlock instanceof Ageable)) {
                logger.warning(configCrop + " is not a valid ageable material. Please check to be sure you spelled everything correctly.");
                continue;
            }
            configuredCrops.add(crop);
        }
    }

    private void setConfiguredTools(FileConfiguration config) {
        enabledTools.clear();
        List<String> stringToolList = config.getStringList("replant-tools");
        for (String tool : stringToolList) {
            try {
                Material material = Material.matchMaterial(tool);
                if (material == null) {
                    logger.warning(tool + " is not a valid material. Please check your syntax");
                    continue;
                }
                enabledTools.add(material);
            } catch (IllegalArgumentException e) {
                logger.warning(tool + " could not be cast to an ItemStack. Please make sure your syntax is correct");
            }
        }
    }


    public boolean allowRightClickHarvest() {
        return rightClickHarvest;
    }


    public List<Material> getConfiguredCrops() {
        return Collections.unmodifiableList(configuredCrops);
    }

    public List<Material> getEnabledTools() {
        return Collections.unmodifiableList(enabledTools);
    }

    public Set<NamespacedKey> getRequiredItemModels() {
        return Collections.unmodifiableSet(requiredItemModels);
    }

    public Sound getBreakSound() {
        return breakSound;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public float getSoundPitch() {
        return soundPitch;
    }

    public Particle getBreakParticle() {
        return breakParticle;
    }

    public int getHarvestParticleCount() {
        return harvestParticleCount;
    }


    public boolean preventToolBreaking() {
        return preventToolBreak;
    }

    public int getMinimumDurability() {
        return minimumDurability;
    }

    public double getHarvestParticleSpread() {
        return harvestParticleSpread;
    }

    public boolean autoReplantEnabled() {
        return autoReplantEnabled;
    }

    public boolean requireSeeds() {
        return requireSeeds;
    }

    public boolean harvestUsesToolDurability() {
        return harvestUsesToolDurability;
    }

    public boolean autoReplantRequiresTool() {
        return autoReplantRequiresTool;
    }

    public boolean rightClickHarvestRequiresTool() {
        return rightClickHarvestRequiresTool;
    }

    public boolean soundsEnabled() {
        return soundsEnabled;
    }

    public boolean harvestParticlesEnabled() {
        return harvestParticlesEnabled;
    }

    public boolean replantParticlesEnabled() {
        return replantParticlesEnabled;
    }

    public boolean replantUsesToolDurability() {
        return replantUsesToolDurability;
    }

    public int getDelayTicks() {
        return delayTicks;
    }

    public Sound getPlantSound() {
        return plantSound;
    }

    public Particle getReplantParticle() {
        return replantParticle;
    }

    public double getReplantParticleSpread() {
        return replantParticleSpread;
    }

    public int getReplantParticleCount() {
        return replantParticleCount;
    }
}
