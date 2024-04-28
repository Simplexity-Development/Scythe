package simplexity.scythe.commands.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import simplexity.scythe.Scythe;
import simplexity.scythe.commands.SubCommand;
import simplexity.scythe.config.LocaleHandler;
import simplexity.scythe.config.ScythePermission;

public class HelpCommand extends SubCommand {
    MiniMessage miniMessage = Scythe.getMiniMessage();

    public HelpCommand() {
        super(ScythePermission.USE.getPermission());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission(ScythePermission.USE.getPermission())) {
            sender.sendMessage(miniMessage.deserialize((LocaleHandler.getInstance().getPrefix()
                    + "\n" + LocaleHandler.getInstance().getHelpMain()
                    + "\n" + LocaleHandler.getInstance().getHelpReload()
                    + "\n" + LocaleHandler.getInstance().getHelpToggle())));
            return;
        }
        sender.sendMessage(miniMessage.deserialize(LocaleHandler.getInstance().getNoPermission()));
    }
}
