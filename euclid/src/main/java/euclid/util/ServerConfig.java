package euclid.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServerConfig {

    private static final String CONFIG_FILE_NAME = "config/config.xml";

    private static final ServerConfig INSTANCE = new ServerConfig();

    private final String configFileName;

    private final Map<String, String> configValues = new LinkedHashMap<>();

    public static ServerConfig getInstance() {
        return INSTANCE;
    }

    public Map<String, String> getConfigValues() {
        return configValues;
    }

    public String getConnectionString() {
        return "jdbc:mysql://"
                + getString("mysql", "hostname") + ":"
                + getInt("mysql", "port") + "/"
                + getString("mysql", "database")
                + "?user=" + getString("mysql", "username")
                + "&password=" + getString("mysql", "password")
                + "&useSSL=false&allowPublicKeyRetrieval=true";
    }

    public ServerConfig() {
        this(CONFIG_FILE_NAME);
    }

    public ServerConfig(String configFileName) {
        this.configFileName = configFileName;

        if (!Files.exists(Path.of(configFileName))) {
            writeConfig();
        }

        configValues.clear();

        Document document = readDocument();

        setConfig(document, "mysql/hostname");
        setConfig(document, "mysql/username");
        setConfig(document, "mysql/password");
        setConfig(document, "mysql/database");
        setConfig(document, "mysql/port");
        setConfig(document, "mysql/min_connections");
        setConfig(document, "mysql/max_connections");
        setConfig(document, "server/main/ip");
        setConfig(document, "server/main/port");
        setConfig(document, "server/private/ip");
        setConfig(document, "server/private/port");

        NodeList rooms = nodesOf(document, "//configuration/server/public/room");
        for (int i = 0; i < rooms.getLength(); i++) {
            Node room = rooms.item(i);
            String roomId = textOf(room, "room_id");
            String ip = textOf(room, "ip");
            String port = textOf(room, "port");

            configValues.put("server/public/ip/" + roomId, ip);
            configValues.put("server/public/port/" + roomId, port);
        }
    }

    public String getString(String... values) {
        return configValues.getOrDefault(String.join("/", values), null);
    }

    public int getInt(String... values) {
        String value = configValues.get(String.join("/", values));
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private Document readDocument() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(configFileName));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read " + configFileName, e);
        }
    }

    private void setConfig(Document document, String xmlPath) {
        configValues.put(xmlPath, textOf(document, "//configuration/" + xmlPath));
    }

    private NodeList nodesOf(Node node, String xpath) {
        try {
            return (NodeList) XPathFactory.newInstance().newXPath().evaluate(xpath, node, XPathConstants.NODESET);
        } catch (Exception e) {
            return new NodeList() {
                @Override
                public int getLength() {
                    return 0;
                }

                @Override
                public Node item(int index) {
                    return null;
                }
            };
        }
    }

    private String textOf(Node node, String xpath) {
        try {
            String value = XPathFactory.newInstance().newXPath().evaluate(xpath + "/text()", node);
            return value == null ? "" : value;
        } catch (Exception e) {
            return "";
        }
    }

    private void writeConfig() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document document = factory.newDocumentBuilder().newDocument();

            Element configuration = document.createElement("configuration");
            document.appendChild(configuration);

            Element mysql = appendChild(configuration, "mysql");
            appendChild(mysql, "hostname", "localhost");
            appendChild(mysql, "username", "root");
            appendChild(mysql, "password", "password");
            appendChild(mysql, "database", "euclid");
            appendChild(mysql, "port", "3306");
            appendChild(mysql, "min_connections", "5");
            appendChild(mysql, "max_connections", "10");

            Element server = appendChild(configuration, "server");
            Element main = appendChild(server, "main");
            appendChild(main, "ip", "127.0.0.1");
            appendChild(main, "port", "37120");
            Element priv = appendChild(server, "private");
            appendChild(priv, "ip", "127.0.0.1");
            appendChild(priv, "port", "25006");
            Element pub = appendChild(server, "public");
            Element room = appendChild(pub, "room");
            appendChild(room, "ip", "127.0.0.1");
            appendChild(room, "port", "22009");
            appendChild(room, "room_id", "1");

            File configFile = new File(configFileName);
            if (configFile.getParentFile() != null) {
                configFile.getParentFile().mkdirs();
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(configFile));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to write " + configFileName, e);
        }
    }

    private Element appendChild(Element parent, String name, String text) {
        Element element = parent.getOwnerDocument().createElement(name);
        if (text != null) {
            element.setTextContent(text);
        }
        parent.appendChild(element);
        return element;
    }

    private Element appendChild(Element parent, String name) {
        return appendChild(parent, name, null);
    }
}
