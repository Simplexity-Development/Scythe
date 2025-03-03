package simplexity.scythe.handling;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplexity.scythe.Scythe;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.Message;

@SuppressWarnings("DataFlowIssue")
public class DurabilityManager {

    private static DurabilityManager instance;

    public DurabilityManager() {
    }

    public static DurabilityManager getInstance() {
        if (instance == null) instance = new DurabilityManager();
        return instance;
    }

    private final NamespacedKey beenNotified = new NamespacedKey(Scythe.getInstance(), "durability-notif");

    public boolean durabilitySuccessfullyRemoved(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!toolHasEnoughDurability(item)) {
            if (!hasBeenNotified(player)) {
                player.sendMessage(Scythe.getMiniMessage().deserialize(Message.YOUR_TOOL_IS_ALMOST_BROKEN.getMessage()));
                setNowNotified(player);
                scheduleFlagRemoval(player);
                return false;
            }
            return false;
        }
        removeDurability(item);
        return true;
    }


    public boolean toolHasEnoughDurability(ItemStack item) {
        if (!ConfigHandler.getInstance().preventToolBreaking()) return true;
        if (!item.hasData(DataComponentTypes.MAX_DAMAGE) || !item.hasData(DataComponentTypes.DAMAGE)) return false;
        int currentDamage = item.getData(DataComponentTypes.DAMAGE);
        int maxDamage = item.getData(DataComponentTypes.MAX_DAMAGE);
        return (maxDamage - currentDamage) > ConfigHandler.getInstance().getMinimumDurability();
    }

    public void removeDurability(ItemStack item) {
        int currentDamage = item.getData(DataComponentTypes.DAMAGE);
        item.setData(DataComponentTypes.DAMAGE, currentDamage + 1);
    }

    private void scheduleFlagRemoval(Player player){
        Bukkit.getScheduler().runTaskLater(Scythe.getInstance(), () ->{
            removeNotifyFlag(player);
        }, 100);
    }

    private void removeNotifyFlag(Player player){
        player.getPersistentDataContainer().set(beenNotified, PersistentDataType.BOOLEAN, Boolean.FALSE);
    }

    private void setNowNotified(Player player){
        player.getPersistentDataContainer().set(beenNotified, PersistentDataType.BOOLEAN, Boolean.TRUE);
    }

    public boolean hasBeenNotified(Player player) {
        PersistentDataContainer playerPDC = player.getPersistentDataContainer();
        return playerPDC.getOrDefault(beenNotified, PersistentDataType.BOOLEAN, Boolean.FALSE);
    }
}
