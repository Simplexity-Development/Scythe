package simplexity.scythe.commands.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.ConfigHandler;
import simplexity.scythe.config.Message;
import simplexity.scythe.config.ScythePermission;

public class ReloadCommand extends SubCommand {
    MiniMessage miniMessage = Scythe.getMiniMessage();

    public ReloadCommand() {
        super(ScythePermission.RELOAD_COMMAND.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission(ScythePermission.RELOAD_COMMAND.getPermission())) {
            Scythe.getInstance().reloadConfig();
            ConfigHandler.getInstance().configParser();
            sender.sendMessage(miniMessage.deserialize(Message.CONFIG_RELOAD.getMessage()));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(Message.NO_PERMISSION.getMessage()));
    }
}
