package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.MessageHandler;
import adhdmc.scythe.Scythe;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ToggleCommand extends SubCommand {
    Map<MessageHandler.Message, String> msgs = MessageHandler.getMessageMap();
    MiniMessage mM = MiniMessage.miniMessage();
    NamespacedKey toggle = new NamespacedKey(Scythe.plugin, "toggle");

    public ToggleCommand() {
        super("toggle", "Toggles scythe on and off","/scythe toggle");
    }
    public void doThing(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.NOT_A_PLAYER)));
            return;
        }
        if (!(sender.hasPermission(Scythe.togglePermission) && sender.hasPermission(Scythe.usePermission))) {
            sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.PREFIX) + msgs.get(MessageHandler.Message.NO_PERMISSION)));
            return;
        }
        if (toggleSetting((Player) sender)) {
            sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.PREFIX) + msgs.get(MessageHandler.Message.TOGGLE_ON)));
            return;
        }
        sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.PREFIX) + msgs.get(MessageHandler.Message.TOGGLE_OFF)));
    }

    private boolean toggleSetting(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (pdc.has(toggle, PersistentDataType.STRING)) {
            String pdcToggle = pdc.get(toggle, PersistentDataType.STRING);
            if (pdcToggle != null && pdcToggle.equals("true")) {
                pdc.set(toggle, PersistentDataType.STRING, "false");
                return false;
            }
            pdc.set(toggle, PersistentDataType.STRING, "true");
            return true;
        }
        pdc.set(toggle, PersistentDataType.STRING, "false");
        return false;
    }
    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
    }

