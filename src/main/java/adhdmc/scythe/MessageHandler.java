package adhdmc.scythe;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandler {
    private static final Pattern hexPattern = Pattern.compile("(&#[a-fA-F0-9]{6})");
    public static String prefix;
    public static String toggleOn;
    public static String toggleOff;
    public static String noCommand;
    public static String configReload;
    public static String noPermission;
    public static String notAPlayer;
    public static String helpMain;
    public static String helpToggle;
    public static String helpReload;

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
    prefix = colorParse("&6&l[&eScythe&6&l]&r ");
    toggleOn = colorParse("&aScythe toggled on!");
    toggleOff = colorParse("&cScythe toggled off!");
    noCommand = colorParse("&cUnknown Command");
    configReload = colorParse("&aScythe Config Reloaded!");
    noPermission = colorParse("&cYou do not have the required permissions to run this command");
    notAPlayer = "Sorry! This command can only be run by a player";
    helpMain = colorParse("&7Scythe allows players to harvest grown crops without needing to replant");
    helpToggle = colorParse("&6/scythe toggle \n&7Toggle scythe on or off");
    helpReload = colorParse("&6/scythe reload \n&7Reloads config settings");
    }
}
