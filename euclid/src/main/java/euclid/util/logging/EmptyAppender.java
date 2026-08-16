package euclid.util.logging;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "EmptyConsole", category = "Core", elementType = "appender", printObject = true)
public class EmptyAppender extends AbstractAppender {

    @PluginFactory
    public static EmptyAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("layout") Layout<?> layout) {
        return new EmptyAppender(name, layout);
    }

    public EmptyAppender(String name, Layout<?> layout) {
        super(name, null, layout);
    }

    @Override
    public void append(LogEvent event) {
    }
}
