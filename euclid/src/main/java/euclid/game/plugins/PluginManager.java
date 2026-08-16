package euclid.game.plugins;

import euclid.game.ILoadable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PluginManager implements ILoadable {

    private static final PluginManager instance = new PluginManager();
    private static final Logger log = LogManager.getLogger(PluginManager.class);
    private static final String PLUGIN_DIR = "plugins";

    private List<PluginProxy> plugins;

    private PluginManager() {
    }

    public static PluginManager getInstance() {
        return instance;
    }

    @Override
    public void load() {
        reloadPlugins();
    }

    public IPlugin[] getPlugins() {
        if (plugins == null) {
            return new IPlugin[0];
        }
        IPlugin[] result = new IPlugin[plugins.size()];
        for (int i = 0; i < plugins.size(); i++) {
            result[i] = plugins.get(i).getPlugin();
        }
        return result;
    }

    public void reloadPlugins() {
        boolean isReload = plugins != null;

        Path pluginPath = Paths.get(PLUGIN_DIR);
        if (!Files.exists(pluginPath)) {
            try {
                Files.createDirectories(pluginPath);
            } catch (IOException e) {
                log.error("Failed to create plugins directory", e);
                return;
            }
        }

        if (plugins != null) {
            for (PluginProxy proxy : plugins) {
                proxy.getPlugin().onDisable();
            }
            plugins = null;
        }

        plugins = new ArrayList<>();

        File[] files = new File(PLUGIN_DIR).listFiles();
        if (files != null) {
            for (File file : files) {
                if (!file.getName().endsWith(".jar")) {
                    continue;
                }
                log.info("Loading {} as a plugin", file.getName());
                try {
                    plugins.add(new PluginProxy(file.getAbsolutePath()));
                } catch (Exception e) {
                    log.error("Failed to load plugin {}", file.getName(), e);
                }
            }
        }

        for (PluginProxy proxy : plugins) {
            proxy.getPlugin().onEnable();
        }

        if (isReload) {
            log.info("Reloaded {} plugins", plugins.size());
        }
    }
}
