package simplexity.scythe.commands.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.LocaleHandler;
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
            sender.sendMessage(miniMessage.deserialize(LocaleHandler.getInstance().getNotAPlayer()));
            return;
        }
        if (!player.hasPermission(ScythePermission.TOGGLE_COMMAND.getPermission())) {
            player.sendMessage(miniMessage.deserialize(LocaleHandler.getInstance().getNoPermission(), Placeholder.parsed("prefix", LocaleHandler.getInstance().getPrefix())));
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

