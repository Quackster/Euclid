package euclid.game;

import euclid.game.entity.Humanoid;
import euclid.game.entity.EntityType;
import euclid.game.entity.RoomEntity;
import euclid.game.entity.RoomPlayer;
import euclid.game.item.Inventory;
import euclid.game.permissions.PermissionsManager;
import euclid.game.permissions.UserGroup;
import euclid.messages.IMessageComposer;
import euclid.messages.outgoing.SYSTEMBROADCAST;
import euclid.network.session.ConnectionMode;
import euclid.network.session.ConnectionSession;
import euclid.storage.database.access.UserDao;
import euclid.storage.database.data.PlayerData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends Humanoid {

    private Logger log;
    private final ConnectionSession connection;
    private PlayerData playerData;
    private RoomEntity roomEntity;
    private Inventory inventory;
    private boolean authenticated;
    private LocalDateTime authenticationTime;
    private boolean disconnected;

    public Player(ConnectionSession connectionSession) {
        this.connection = connectionSession;
        this.playerData = new PlayerData();
        this.roomEntity = new RoomPlayer(this);
        this.entityData = playerData;
        this.entityType = EntityType.PLAYER;

        if (getConnectionMode() == ConnectionMode.MAIN)
            log = LogManager.getLogger("[Main] Connection " + connectionSession.getChannel().id().asLongText());
        else if (getConnectionMode() == ConnectionMode.PRIVATE)
            log = LogManager.getLogger("[Private] Connection " + connectionSession.getChannel().id().asLongText());
        else
            log = LogManager.getLogger("[Public] Connection " + connectionSession.getChannel().id().asLongText());
    }

    @Override
    public PlayerData getEntityData() {
        return playerData;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }

    @Override
    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    @Override
    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public ConnectionSession getConnection() {
        return connection;
    }

    public ConnectionMode getConnectionMode() {
        return connection.getMode();
    }

    public Logger getLog() {
        return log;
    }

    public PlayerData getDetails() {
        return playerData;
    }

    public void setDetails(PlayerData details) {
        this.playerData = details;
        this.entityData = details;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public LocalDateTime getAuthenticationTime() {
        return authenticationTime;
    }

    public void setAuthenticationTime(LocalDateTime authenticationTime) {
        this.authenticationTime = authenticationTime;
    }

    public UserGroup getUserGroup() {
        return PermissionsManager.getInstance().getRanks().get(playerData.getRank());
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public Player getMainServer() {
        List<Player> players = PlayerManager.getInstance().getPlayers();
        return players.stream()
                .filter(x -> x.getDetails().getId() == playerData.getId() && x.getConnectionMode() == ConnectionMode.MAIN)
                .findFirst()
                .orElse(null);
    }

    public Player getPrivateServer() {
        List<Player> players = PlayerManager.getInstance().getPlayers();
        return players.stream()
                .filter(x -> x.getDetails().getId() == playerData.getId() && x.getConnectionMode() == ConnectionMode.PRIVATE)
                .findFirst()
                .orElse(null);
    }

    public Player getPublicServer() {
        List<Player> players = PlayerManager.getInstance().getPlayers();
        return players.stream()
                .filter(x -> x.getDetails().getId() == playerData.getId() && x.getConnectionMode() == ConnectionMode.PUBLIC)
                .findFirst()
                .orElse(null);
    }

    public boolean tryLogin(String username, String password) {
        playerData = UserDao.login(username, password);

        if (playerData == null) {
            send(new SYSTEMBROADCAST("Your username or password was incorrect."));
            return false;
        }

        if (getConnectionMode() == ConnectionMode.MAIN)
            log = LogManager.getLogger("[Main] Player " + playerData.getName());
        else if (getConnectionMode() == ConnectionMode.PRIVATE)
            log = LogManager.getLogger("[Private] Player " + playerData.getName());
        else
            log = LogManager.getLogger("[Public] Player " + playerData.getName());

        log.debug("Player {} has logged in", playerData.getName());

        playerData.setLastOnline(LocalDateTime.now());
        this.entityData = playerData;

        inventory = new Inventory(this);

        if (getConnectionMode() == ConnectionMode.PRIVATE) {
            inventory.load();
        }

        UserDao.saveLastOnline(playerData);
        PlayerManager.getInstance().addPlayer(this);

        authenticated = true;
        authenticationTime = LocalDateTime.now();

        return true;
    }

    public void send(IMessageComposer composer) {
        connection.send(composer);
    }

    public void onDisconnect() {
        if (!authenticated)
            return;

        PlayerManager.getInstance().removePlayer(this);

        playerData.setLastOnline(LocalDateTime.now());
        UserDao.saveLastOnline(playerData);

        if (roomEntity.getRoom() != null) {
            if (roomEntity.isBeingKicked()) {
                roomEntity.kick(false);
            } else {
                roomEntity.getRoom().getEntityManager().leaveRoom(this);
            }
        }

        this.disconnected = true;
    }

    public boolean disconnect() {
        connection.disconnect();
        return true;
    }
}
