package euclid;

import euclid.game.catalogue.CatalogueManager;
import euclid.game.item.ItemManager;
import euclid.game.navigator.NavigatorManager;
import euclid.game.permissions.PermissionsManager;
import euclid.game.plugins.PluginManager;
import euclid.game.room.RoomManager;
import euclid.game.values.ValueManager;
import euclid.messages.MessageHandler;
import euclid.network.GameServer;
import euclid.network.streams.GameAddress;
import euclid.storage.database.SessionFactoryBuilder;
import euclid.storage.database.access.RoomDao;
import euclid.storage.database.data.RoomData;
import euclid.util.ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Euclid {

    private static final Logger log = LogManager.getLogger(Euclid.class);

    public static final boolean ENCRYPTION = true;

    public static final String CLIENT_VERSION = "RELEASE63-201302211227-193109692";

    public static Logger getLogger() {
        return log;
    }

    public static void main(String[] args) {
        initialiseLogging();
        setConsoleTitle("Euclid - Habbo Hotel Emulation");

        log.info("Booting Euclid - Written by Quackster");
        log.info("Emulation of Habbo Hotel 2007 Shockwave client");

        try {
            tryDatabaseConnection();
            tryGameData();
            tryCreateServer();

            log.info("Server has started!");

            while (true) {
                Thread.sleep(100);
            }
        } catch (Exception ex) {
            log.error("Server failed to start", ex);
        }
    }

    private static void initialiseLogging() {
        for (String path : new String[] { "config/log4j2.xml", "log4j2.xml" }) {
            if (Files.exists(Path.of(path))) {
                Configurator.initialize((String) null, path);
                return;
            }
        }
    }

    private static void setConsoleTitle(String title) {
        System.out.print("\u001b]0;" + title + "\u0007");
        System.out.flush();
    }

    private static void tryDatabaseConnection() {
        log.info("Attempting to connect to database");
        SessionFactoryBuilder.getInstance().initialiseSessionFactory(ServerConfig.getInstance().getConnectionString());
        log.info("Connection successful!");
    }

    private static void tryGameData() {
        RoomDao.resetVisitorCounts();

        PermissionsManager.getInstance().load();
        ValueManager.getInstance().load();
        ItemManager.getInstance().load();
        CatalogueManager.getInstance().load();
        RoomManager.getInstance().load();
        NavigatorManager.getInstance().load();
        MessageHandler.getInstance().load();
        PluginManager.getInstance().load();
    }

    private static void tryCreateServer() {
        log.info("Starting server");

        ServerConfig config = ServerConfig.getInstance();
        List<String> roomIds = new ArrayList<>();
        for (String key : config.getConfigValues().keySet()) {
            if (key.startsWith("server/public/port/")) {
                roomIds.add(key.substring("server/public/port/".length()));
            }
        }

        List<GameAddress> gameAddresses = new ArrayList<>();
        for (String roomId : roomIds) {
            gameAddresses.add(new GameAddress(
                    config.getString("server", "public", "ip", roomId),
                    config.getInt("server", "public", "port", roomId),
                    Integer.parseInt(roomId)
            ));
        }

        GameServer.createServer(
                new GameAddress(config.getString("server", "main", "ip"), config.getInt("server", "main", "port")),
                new GameAddress(config.getString("server", "private", "ip"), config.getInt("server", "private", "port")),
                gameAddresses
        );

        GameServer.getInstance().initialiseServer();

        for (GameAddress gameAddress : gameAddresses) {
            RoomData roomData = RoomDao.getRoomData(gameAddress.getRoomId());
            if (roomData == null) {
                continue;
            }
            log.info("[{}] is listening on port: {}:{}!", roomData.getName(), gameAddress.getIpAddress(), gameAddress.getPort());
        }

        GameServer mainServer = GameServer.getInstance();
        log.info("Private server is now listening on port: {}:{}!", mainServer.getPrivateServer().getIpAddress(), mainServer.getPrivateServer().getPort());
        log.info("Main server is now listening on port: {}:{}!", mainServer.getMainServer().getIpAddress(), mainServer.getMainServer().getPort());
    }

    public static final class Game {
    }
}
