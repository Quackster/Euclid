package plugin.example;

import euclid.game.plugins.IPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PluginExample implements IPlugin {

    private static final Logger log = LogManager.getLogger(PluginExample.class);

    @Override
    public void onEnable() {
        log.info("Enabling Plugin Example");
    }

    @Override
    public void onDisable() {
        log.info("Disposing Plugin Example");
    }
}
