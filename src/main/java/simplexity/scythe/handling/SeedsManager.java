package simplexity.scythe.handling;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simplexity.scythe.config.ConfigHandler;

import java.util.HashMap;

public class SeedsManager {
    private static SeedsManager instance;

    public SeedsManager() {
    }

    public static SeedsManager getInstance() {
        if (instance == null) instance = new SeedsManager();
        return instance;
    }

    /*
    AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
     */
    public boolean seedsHandledProperly(Player player, Material seedMaterial) {
        if (!ConfigHandler.getInstance().requireSeeds()) return true;
        Integer seedSlot = getSeedSlot(player, seedMaterial);
        if (seedSlot == null) return false;
        ItemStack seeds = player.getInventory().getItem(seedSlot);
        if (seeds == null) return false;
        seeds.subtract(1);
        return true;
    }

    private Integer getSeedSlot(Player player, Material seedMaterial) {
        ItemStack defaultSeeds = ItemStack.of(seedMaterial);
        HashMap<Integer, ? extends ItemStack> slotItems = player.getInventory().all(seedMaterial);
        for (Integer slot : slotItems.keySet()) {
            if (slotItems.get(slot).asOne().equals(defaultSeeds)) return slot;
        }
        return null;
    }

    public boolean hasSeeds(Player player, Material seedMaterial) {
        return player.getInventory().containsAtLeast(ItemStack.of(seedMaterial), 1);
    }
}
