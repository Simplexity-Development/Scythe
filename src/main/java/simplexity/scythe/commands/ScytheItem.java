package simplexity.scythe.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import simplexity.scythe.Scythe;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.Message;
import simplexity.scythe.config.MessageUtils;
import simplexity.scythe.config.ScythePermission;

public class ScytheItem implements CommandExecutor {

    public static final NamespacedKey scytheItemKey = new NamespacedKey(Scythe.getInstance(), "scythe-item");
    public static final NamespacedKey commandCooldown = new NamespacedKey(Scythe.getInstance(), "scythe-item-command-cooldown");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!ConfigHandler.getInstance().isCustomItemEnabled()) {
            sender.sendRichMessage(
                    Message.ERROR_CUSTOM_ITEM_NOT_ENABLED.getMessage()
            );
            return false;
        }
        if (args.length < 1) return handleNoArgs(sender);
        if (!sender.hasPermission(ScythePermission.ITEM_OTHERS.getPermission())) {
            sender.sendRichMessage(Message.ERROR_NO_PERMISSION.getMessage());
            return false;
        }
        String playerString = args[0];
        Player targetPlayer = Bukkit.getPlayer(playerString);
        if (targetPlayer == null) {
            sender.sendRichMessage(Message.ERROR_NOT_A_PLAYER.getMessage());
            return false;
        }
        long secondsLeft = cooldownSecondsLeft(sender);
        if (secondsLeft > 0) {
            sender.sendRichMessage(Message.ERROR_COOLDOWN_NOT_EXPIRED.getMessage(),
                    MessageUtils.getTimeFormat(secondsLeft));
            return false;
        }
        giveItem(targetPlayer);
        if (sender instanceof Player playerSender) updateTimestamp(playerSender);
        targetPlayer.sendRichMessage(Message.SCYTHE_RECEIVED.getMessage(),
                Placeholder.parsed("name", sender.getName()));
        sender.sendRichMessage(Message.SCYTHE_GIVEN.getMessage(),
                Placeholder.parsed("name", targetPlayer.getName()));
        return true;
    }

    private boolean handleNoArgs(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.ERROR_NOT_A_PLAYER.getMessage());
            return false;
        }
        long secondsLeft = cooldownSecondsLeft(sender);
        if (secondsLeft > 0) {
            sender.sendRichMessage(Message.ERROR_COOLDOWN_NOT_EXPIRED.getMessage(),
                    MessageUtils.getTimeFormat(secondsLeft));
            return false;
        }
        giveItem(player);
        updateTimestamp(player);
        player.sendRichMessage(Message.SCYTHE_OBTAINED.getMessage());
        return true;
    }


    // this feels like the incorrect way to handle this but I'm not entirely sure how to do it properly

    private long cooldownSecondsLeft(CommandSender sender) {
        if (sender.hasPermission(ScythePermission.BYPASS_COOLDOWN.getPermission()) || (!(sender instanceof Player playerSender))) {
            return -1;
        }
        if (ConfigHandler.getInstance().getCommandCooldownSeconds() <= 0) {
            return -1;
        }
        PersistentDataContainer playerPdc = playerSender.getPersistentDataContainer();
        long currentTime = System.currentTimeMillis();
        long savedTime = playerPdc.getOrDefault(commandCooldown, PersistentDataType.LONG, -1L);
        if (savedTime == -1L) return -1;
        long timeDiff = currentTime - savedTime;
        long cooldownRequired = ConfigHandler.getInstance().getCommandCooldownSeconds() * 1000;
        long millisecondsLeft = cooldownRequired - timeDiff;
        return millisecondsLeft / 1000;
    }

    private void giveItem(Player player) {
        ItemStack scythe = ConfigHandler.getInstance().getScytheItem();
        scythe.editPersistentDataContainer(pdc -> {
            pdc.set(scytheItemKey, PersistentDataType.BYTE, (byte) 1);
        });
        int emptySlot = player.getInventory().firstEmpty();
        if (emptySlot == -1) {
            Location playerLocation = player.getLocation().toCenterLocation();
            playerLocation.getWorld().dropItem(playerLocation, scythe);
            return;
        }
        player.getInventory().setItem(emptySlot, scythe);
    }

    private void updateTimestamp(Player player) {
        if (player.hasPermission(ScythePermission.BYPASS_COOLDOWN.getPermission())) return;
        if (ConfigHandler.getInstance().getCommandCooldownSeconds() <= 0) return;
        long currentTime = System.currentTimeMillis();
        PersistentDataContainer playerPdc = player.getPersistentDataContainer();
        playerPdc.set(commandCooldown, PersistentDataType.LONG, currentTime);
    }
}
