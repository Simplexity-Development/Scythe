package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.ConfigHandler;
import adhdmc.scythe.Scythe;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class ToggleCommand extends SubCommand {
    Map<ConfigHandler.Message, String> msgs = ConfigHandler.getMessageMap();
    MiniMessage mM = MiniMessage.miniMessage();
    public static NamespacedKey functionToggle = new NamespacedKey(Scythe.plugin, "functiontoggle");
    String enabled = Scythe.replantingEnabled;
    String disabled = Scythe.replantingDisabled;

    public ToggleCommand() {
        super("toggle", "Toggles scythe on and off","/scythe toggle");
    }
    public void doThing(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(mM.deserialize(msgs.get(ConfigHandler.Message.NOT_A_PLAYER)));
            return;
        }
        if (!(sender.hasPermission(Scythe.togglePermission) && sender.hasPermission(Scythe.usePermission))) {
            sender.sendMessage(mM.deserialize(msgs.get(ConfigHandler.Message.PREFIX) + msgs.get(ConfigHandler.Message.NO_PERMISSION)));
            return;
        }
        if (toggleSetting((Player) sender)) {
            sender.sendMessage(mM.deserialize(msgs.get(ConfigHandler.Message.PREFIX) + msgs.get(ConfigHandler.Message.TOGGLE_ON)));
            return;
        }
        sender.sendMessage(mM.deserialize(msgs.get(ConfigHandler.Message.PREFIX) + msgs.get(ConfigHandler.Message.TOGGLE_OFF)));
    }

    private boolean toggleSetting(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (pdc.get(functionToggle, PersistentDataType.STRING) != null) {
            String pdcToggle = pdc.get(functionToggle, PersistentDataType.STRING);
            if (pdcToggle != null && pdcToggle.equals(enabled)) {
                pdc.set(functionToggle, PersistentDataType.STRING, disabled);
                return false;
            }
            pdc.set(functionToggle, PersistentDataType.STRING, enabled);
            return true;
        }
        pdc.set(functionToggle, PersistentDataType.STRING, disabled);
        return false;
    }
    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
    }

