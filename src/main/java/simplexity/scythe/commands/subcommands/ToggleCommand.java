package simplexity.scythe.commands.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.Message;
import simplexity.scythe.config.ScythePermission;
import simplexity.scythe.events.ToggleEvent;

public class ToggleCommand extends SubCommand {
    private static final MiniMessage miniMessage = Scythe.getMiniMessage();
    public static final NamespacedKey toggleKey = new NamespacedKey(Scythe.getInstance(), "functiontoggle");

    public ToggleCommand() {
        super(ScythePermission.TOGGLE_COMMAND.getPermission());
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(miniMessage.deserialize(Message.NOT_A_PLAYER.getMessage()));
            return;
        }
        if (!((player.hasPermission(ScythePermission.TOGGLE_COMMAND.getPermission())) && player.hasPermission(ScythePermission.USE.getPermission()))) {
            player.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage(), Placeholder.parsed("prefix", Message.PREFIX.getMessage())));
            return;
        }
        ToggleEvent toggleEvent = new ToggleEvent(player, toggleKey);
        Bukkit.getPluginManager().callEvent(toggleEvent);
        if (toggleEvent.isCancelled()) return;
        byte toggleState = toggleEvent.getCurrentToggleState();
        if (toggleState == (byte) 0) {
            toggleEvent.setDisabled();
            return;
        }
        toggleEvent.setEnabled();
    }
}

