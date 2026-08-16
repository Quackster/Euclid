package euclid.util.logging;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(name = "ColorConsole", category = "Core", elementType = "appender", printObject = true)
public class ConsoleAppenderWithColorSwitching extends AbstractAppender {

    private static final Pattern COLOR_MARKER = Pattern.compile("(\\|\\w+\\|)");

    private static final Map<String, String> CONSOLE_COLORS = Map.ofEntries(
            Map.entry("Black", "\u001b[30m"),
            Map.entry("DarkBlue", "\u001b[34m"),
            Map.entry("DarkGreen", "\u001b[32m"),
            Map.entry("DarkCyan", "\u001b[36m"),
            Map.entry("DarkRed", "\u001b[31m"),
            Map.entry("DarkMagenta", "\u001b[35m"),
            Map.entry("DarkYellow", "\u001b[33m"),
            Map.entry("Gray", "\u001b[37m"),
            Map.entry("Blue", "\u001b[94m"),
            Map.entry("Green", "\u001b[92m"),
            Map.entry("Cyan", "\u001b[96m"),
            Map.entry("Red", "\u001b[91m"),
            Map.entry("Magenta", "\u001b[95m"),
            Map.entry("Yellow", "\u001b[93m"),
            Map.entry("White", "\u001b[97m"),
            Map.entry("DarkGray", "\u001b[90m"));

    private final Layout<?> layout;

    @PluginFactory
    public static ConsoleAppenderWithColorSwitching createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("layout") Layout<?> layout) {
        return new ConsoleAppenderWithColorSwitching(name, layout);
    }

    public ConsoleAppenderWithColorSwitching(String name, Layout<?> layout) {
        super(name, null, layout);
        this.layout = layout;
    }

    @Override
    public void append(LogEvent event) {
        if (layout == null) {
            return;
        }
        String rendered = replaceLogLevel(new String(layout.toByteArray(event), StandardCharsets.UTF_8));
        Matcher matcher = COLOR_MARKER.matcher(rendered);
        int last = 0;
        while (matcher.find()) {
            System.out.print(rendered.substring(last, matcher.start()));
            String name = matcher.group(1).substring(1, matcher.group(1).length() - 1);
            String color = CONSOLE_COLORS.get(name);
            if (color != null) {
                System.out.print(color);
            }
            last = matcher.end();
        }
        System.out.print(rendered.substring(last));
        System.out.flush();
    }

    private String replaceLogLevel(String rendered) {
        rendered = rendered.replace(" INFO ", " |Green|INFO|Gray| ");
        rendered = rendered.replace(" DEBUG ", " |DarkYellow|DEBUG|Gray|  ");
        rendered = rendered.replace(" WARN ", " |Yellow|WARN|Gray|  ");
        rendered = rendered.replace(" ERROR ", " |DarkYellow|ERROR|Gray|  ");
        rendered = rendered.replace(" FATAL ", " |DarkRed|FATAL|Gray|  ");
        return rendered;
    }
}
