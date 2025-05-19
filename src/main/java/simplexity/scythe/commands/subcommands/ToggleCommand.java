package simplexity.scythe.commands.subcommands;

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
    public static final NamespacedKey toggleKey = new NamespacedKey(Scythe.getInstance(), "functiontoggle");

    public ToggleCommand() {
        super(ScythePermission.TOGGLE_COMMAND.getPermission());
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendRichMessage(Message.ERROR_NOT_A_PLAYER.getMessage());
            return;
        }
        if (!player.hasPermission(ScythePermission.TOGGLE_COMMAND.getPermission())) {
            player.sendRichMessage(Message.ERROR_NO_PERMISSION.getMessage());
            return;
        }
        ToggleEvent toggleEvent = new ToggleEvent(player, toggleKey);
        Bukkit.getPluginManager().callEvent(toggleEvent);
        if (toggleEvent.isCancelled()) return;
        boolean toggleIsEnabled = toggleEvent.getCurrentToggleState();
        if (toggleIsEnabled) {
            toggleEvent.setDisabled();
            return;
        }
        toggleEvent.setEnabled();
    }
}

