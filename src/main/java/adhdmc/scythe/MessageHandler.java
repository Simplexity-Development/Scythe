package adhdmc.scythe;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandler {
    public static FileConfiguration config = Scythe.plugin.getConfig();
    private static final Pattern hexPattern = Pattern.compile("(&#[a-fA-F0-9]{6})");
    public static String prefix;
    public static String toggleOn, toggleOff, configReload;
    public static String noCommand, noPermission, notAPlayer;
    public static String helpMain, helpToggle, helpReload;

    public static String colorParse(String s) {
        Matcher matcher = hexPattern.matcher(s);
        while (matcher.find()) {
            String colorReplace = s.substring(matcher.start(), matcher.end());
            String colorHex = s.substring(matcher.start()+1, matcher.end());
            s = s.replace(colorReplace, "" + net.md_5.bungee.api.ChatColor.of(colorHex));
            matcher = hexPattern.matcher(s);
        }
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    public static void loadPluginMsgs(){
    prefix = colorParse(config.getString("Plugin Prefix"));
    toggleOn = colorParse(config.getString("Toggle On"));
    toggleOff = colorParse(config.getString("Toggle Off"));
    noCommand = colorParse(config.getString("No Command"));
    configReload = colorParse(config.getString("Config Reload"));
    noPermission = colorParse(config.getString("No Permission"));
    notAPlayer = colorParse(config.getString("Not A Player"));
    helpMain = colorParse(config.getString("Help Main"));
    helpToggle = colorParse(config.getString("Help Toggle"));
    helpReload = colorParse(config.getString("Help Reload"));
    }
}
