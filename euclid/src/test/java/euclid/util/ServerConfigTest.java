package euclid.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerConfigTest {

    @TempDir
    Path tempDir;

    private ServerConfig loadConfig(String xml) throws Exception {
        Path file = tempDir.resolve("config.xml");
        Files.writeString(file, xml);
        return new ServerConfig(file.toString());
    }

    @Test
    void flattensSimpleKeys() throws Exception {
        ServerConfig config = loadConfig("""
                <configuration>
                  <mysql>
                    <hostname>dbhost</hostname>
                    <username>root</username>
                    <password>123</password>
                    <database>euclid</database>
                    <port>3307</port>
                    <min_connections>2</min_connections>
                    <max_connections>4</max_connections>
                  </mysql>
                  <server>
                    <main>
                      <ip>10.0.0.1</ip>
                      <port>1111</port>
                    </main>
                    <private>
                      <ip>10.0.0.2</ip>
                      <port>2222</port>
                    </private>
                    <public/>
                  </server>
                </configuration>
                """);

        assertEquals("dbhost", config.getString("mysql", "hostname"));
        assertEquals("root", config.getString("mysql", "username"));
        assertEquals("123", config.getString("mysql", "password"));
        assertEquals("euclid", config.getString("mysql", "database"));
        assertEquals(3307, config.getInt("mysql", "port"));
        assertEquals(2, config.getInt("mysql", "min_connections"));
        assertEquals(4, config.getInt("mysql", "max_connections"));
        assertEquals("10.0.0.1", config.getString("server", "main", "ip"));
        assertEquals(1111, config.getInt("server", "main", "port"));
        assertEquals("10.0.0.2", config.getString("server", "private", "ip"));
        assertEquals(2222, config.getInt("server", "private", "port"));
        assertEquals("jdbc:mysql://dbhost:3307/euclid?user=root&password=123&useSSL=false&allowPublicKeyRetrieval=true", config.getConnectionString());
    }

    @Test
    void flattensPublicRooms() throws Exception {
        ServerConfig config = loadConfig("""
                <configuration>
                  <server>
                    <public>
                      <room>
                        <ip>127.0.0.1</ip>
                        <port>22009</port>
                        <room_id>1</room_id>
                      </room>
                      <room>
                        <ip>127.0.0.1</ip>
                        <port>22014</port>
                        <room_id>6</room_id>
                      </room>
                    </public>
                  </server>
                </configuration>
                """);

        assertEquals("127.0.0.1", config.getString("server", "public", "ip", "1"));
        assertEquals(22009, config.getInt("server", "public", "port", "1"));
        assertEquals("127.0.0.1", config.getString("server", "public", "ip", "6"));
        assertEquals(22014, config.getInt("server", "public", "port", "6"));

        List<String> roomIds = config.getConfigValues().keySet().stream()
                .filter(key -> key.contains("server/public/port/"))
                .map(key -> key.replace("server/public/port/", ""))
                .toList();
        assertEquals(List.of("1", "6"), roomIds);
    }

    @Test
    void writesDefaultConfigWhenMissing() throws Exception {
        Path file = tempDir.resolve("config.xml");
        assertFalse(Files.exists(file));

        ServerConfig config = new ServerConfig(file.toString());

        assertTrue(Files.exists(file));
        assertEquals("localhost", config.getString("mysql", "hostname"));
        assertEquals("root", config.getString("mysql", "username"));
        assertEquals("password", config.getString("mysql", "password"));
        assertEquals("euclid", config.getString("mysql", "database"));
        assertEquals(3306, config.getInt("mysql", "port"));
        assertEquals(5, config.getInt("mysql", "min_connections"));
        assertEquals(10, config.getInt("mysql", "max_connections"));
        assertEquals("127.0.0.1", config.getString("server", "main", "ip"));
        assertEquals(37120, config.getInt("server", "main", "port"));
        assertEquals("127.0.0.1", config.getString("server", "private", "ip"));
        assertEquals(25006, config.getInt("server", "private", "port"));
        assertEquals("127.0.0.1", config.getString("server", "public", "ip", "1"));
        assertEquals(22009, config.getInt("server", "public", "port", "1"));
        long publicRooms = config.getConfigValues().keySet().stream()
                .filter(key -> key.startsWith("server/public/port/"))
                .count();
        assertEquals(1, publicRooms);
    }

    @Test
    void missingKeysAreEmpty() throws Exception {
        ServerConfig config = loadConfig("""
                <configuration>
                  <server>
                    <main>
                      <ip>1.2.3.4</ip>
                    </main>
                  </server>
                </configuration>
                """);

        assertEquals("1.2.3.4", config.getString("server", "main", "ip"));
        assertEquals("", config.getString("mysql", "hostname"));
        assertEquals(0, config.getInt("server", "main", "port"));
        assertNull(config.getString("server", "public", "ip", "1"));
    }
}
