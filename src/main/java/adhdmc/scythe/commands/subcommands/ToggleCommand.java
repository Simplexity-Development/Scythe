package adhdmc.scythe.commands.subcommands;

import adhdmc.scythe.commands.SubCommand;
import adhdmc.scythe.config.ConfigHandler;
import adhdmc.scythe.Scythe;
import adhdmc.scythe.config.Defaults;
import adhdmc.scythe.config.Message;
import adhdmc.scythe.config.ScythePermission;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class ToggleCommand extends SubCommand {
    private static final MiniMessage miniMessage = Scythe.getMiniMessage();
    public static final NamespacedKey functionToggle = new NamespacedKey(Scythe.getInstance(), "functiontoggle");

    public ToggleCommand() {
        super("toggle", "Toggles scythe on and off","/scythe toggle");
    }
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(miniMessage.deserialize(Message.NOT_A_PLAYER.getMessage()));
            return;
        }
        if (!(sender.hasPermission(ScythePermission.TOGGLE_COMMAND.getPermission()))
                && sender.hasPermission(ScythePermission.USE.getPermission())) {
            sender.sendMessage(miniMessage.deserialize(Message.PREFIX.getMessage()) + Message.NO_PERMISSION.getMessage());
            return;
        }
        if (toggleSetting((Player) sender)) {
            sender.sendMessage(miniMessage.deserialize(Message.PREFIX.getMessage()) + Message.TOGGLE_ON.getMessage());
            return;
        }
        sender.sendMessage(miniMessage.deserialize(Message.PREFIX.getMessage()) + Message.TOGGLE_OFF.getMessage());
    }

    private boolean toggleSetting(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (pdc.get(functionToggle, PersistentDataType.BYTE) != null) {
            Byte pdcToggle = pdc.get(functionToggle, PersistentDataType.BYTE);
            if (pdcToggle != null && pdcToggle.equals((byte)1)) {
                pdc.set(functionToggle, PersistentDataType.BYTE, (byte)0);
                return false;
            }
            pdc.set(functionToggle, PersistentDataType.BYTE, (byte)1);
            return true;
        }
        pdc.set(functionToggle, PersistentDataType.BYTE, (byte)0);
        return false;
    }
    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
    }

