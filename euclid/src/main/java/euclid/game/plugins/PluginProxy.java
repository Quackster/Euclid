package euclid.game.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginProxy {

    private final IPlugin plugin;

    public PluginProxy(String file) throws Exception {
        File pluginFile = new File(file);
        URL url = pluginFile.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, PluginProxy.class.getClassLoader());

        IPlugin found = null;
        try (JarFile jar = new JarFile(pluginFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.endsWith(".class") || name.contains("$")) {
                    continue;
                }
                String className = name.substring(0, name.length() - 6).replace('/', '.');
                try {
                    Class<?> type = Class.forName(className, false, classLoader);
                    if (IPlugin.class.isAssignableFrom(type) && !type.isInterface()
                            && !java.lang.reflect.Modifier.isAbstract(type.getModifiers())) {
                        found = (IPlugin) type.getDeclaredConstructor().newInstance();
                        break;
                    }
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException | java.lang.reflect.InvocationTargetException ignored) {
                }
            }
        }

        if (found == null) {
            throw new Exception("No IPlugin implementation found in " + file);
        }

        this.plugin = found;
    }

    public IPlugin getPlugin() {
        return plugin;
    }
}
