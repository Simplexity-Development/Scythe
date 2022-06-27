package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.MessageHandler;
import adhdmc.scythe.Scythe;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

public class ToggleCommand extends SubCommand {
    public ToggleCommand() {
        super("toggle", "Toggles scythe on and off","/scythe toggle");
    }
    public void doThing(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageHandler.notAPlayer);
            return;
        }
        if (!(sender.hasPermission("scythe.toggle")) || !(sender.hasPermission("scythe.use"))) {
            sender.sendMessage(MessageHandler.noPermission);
            return;
        }
        if (toggleSetting((Player) sender)) {
            sender.sendMessage(MessageHandler.prefix + " " + MessageHandler.toggleOn);
            return;
        }
        sender.sendMessage(MessageHandler.prefix + " " + MessageHandler.toggleOff);
    }

    private boolean toggleSetting(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (pdc.has(new NamespacedKey(Scythe.plugin, "toggle"), PersistentDataType.STRING)) {
            String pdcToggle = pdc.get(new NamespacedKey(Scythe.plugin, "toggle"), PersistentDataType.STRING);
            if (pdcToggle.equals("true")) {
                pdc.set(new NamespacedKey(Scythe.plugin, "toggle"), PersistentDataType.STRING, "false");
                return false;
            }
            pdc.set(new NamespacedKey(Scythe.plugin, "toggle"), PersistentDataType.STRING, "true");
            return true;
        }
        pdc.set(new NamespacedKey(Scythe.plugin, "toggle"), PersistentDataType.STRING, "false");
        return false;
    }
    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
    }

