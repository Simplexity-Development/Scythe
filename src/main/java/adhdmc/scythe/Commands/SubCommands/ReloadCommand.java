package adhdmc.scythe.Commands.SubCommands;

import adhdmc.scythe.MessageHandler;
import adhdmc.scythe.Scythe;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ReloadCommand extends SubCommand {
    Map<MessageHandler.Message, String> msgs = MessageHandler.getMessageMap();
    MiniMessage mM = MiniMessage.miniMessage();

    public ReloadCommand(){
        super("reload", "Reloads the Scythe Plugin", "/scythe reload");
    }

    @Override
    public void doThing(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)|| sender.hasPermission(Scythe.reloadPermission)) {
            Scythe.plugin.reloadConfig();
            MessageHandler.configParser();
            sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.CONFIG_RELOAD)));
            return;
        }
        sender.sendMessage(mM.deserialize(msgs.get(MessageHandler.Message.NO_PERMISSION)));
    }

    @Override
    public List<String> getSubcommandArguments(CommandSender sender, String[] args) {
        return null;
    }
}
