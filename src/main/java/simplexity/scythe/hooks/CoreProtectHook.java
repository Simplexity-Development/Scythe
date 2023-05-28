package simplexity.scythe.hooks;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public class CoreProtectHook {
    private static CoreProtectHook instance;

    private CoreProtectHook() {
    }

    public static CoreProtectHook getInstance() {
        if (instance == null) instance = new CoreProtectHook();
        return instance;
    }

    public CoreProtectAPI getCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");
        // Check that CoreProtect is loaded
        if (!(plugin instanceof CoreProtect)) {
            return null;
        }
        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (!CoreProtect.isEnabled()) {
            return null;
        }
        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 9) {
            return null;
        }
        return CoreProtect;
    }
}
