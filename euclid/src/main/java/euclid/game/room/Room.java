package euclid.game.room;

import euclid.game.Player;
import euclid.game.PlayerManager;
import euclid.game.entity.IEntity;
import euclid.messages.IMessageComposer;
import euclid.network.GameServer;
import euclid.network.streams.GameAddress;
import euclid.util.ServerConfig;
import euclid.storage.database.data.RoomData;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private final RoomData data;
    private final RoomEntityManager entityManager;
    private final RoomTaskManager taskManager;
    private final RoomItemManager itemManager;
    private RoomFurniture furnitureManager;
    private RoomMapping mapping;
    private final ConcurrentHashMap<IEntity, Integer> entities;
    private boolean isActive;
    private GameAddress address;

    public Room(RoomData data) {
        this.data = data;
        this.entities = new ConcurrentHashMap<>();
        this.entityManager = new RoomEntityManager(this);
        this.mapping = new RoomMapping(this);
        this.taskManager = new RoomTaskManager(this);
        this.itemManager = new RoomItemManager(this);
        this.furnitureManager = new RoomFurniture(this);

        if (data.isPrivateRoom())
            address = GameServer.getInstance().getPrivateServer();

        if (data.isPublicRoom()) {
            String ip = ServerConfig.getInstance().getString("server", "public", "ip", String.valueOf(data.getId()));
            int port = ServerConfig.getInstance().getInt("server", "public", "port", String.valueOf(data.getId()));
            if (ip != null)
                address = new GameAddress(ip, port, data.getId());
        }
    }

    public static Room wrap(RoomData roomData) {
        return new Room(roomData);
    }

    public RoomData getData() {
        return data;
    }

    public RoomEntityManager getEntityManager() {
        return entityManager;
    }

    public RoomTaskManager getTaskManager() {
        return taskManager;
    }

    public RoomItemManager getItemManager() {
        return itemManager;
    }

    public RoomFurniture getFurnitureManager() {
        return furnitureManager;
    }

    public void setFurnitureManager(RoomFurniture furnitureManager) {
        this.furnitureManager = furnitureManager;
    }

    public RoomMapping getMapping() {
        return mapping;
    }

    public void setMapping(RoomMapping mapping) {
        this.mapping = mapping;
    }

    public RoomModel getModel() {
        return RoomManager.getInstance().getRoomModels().stream()
                .filter(x -> x.getData().getId() == data.getModelId())
                .findFirst()
                .orElse(null);
    }

    public ConcurrentHashMap<IEntity, Integer> getEntities() {
        return entities;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public GameAddress getAddress() {
        return address;
    }

    public void setAddress(GameAddress address) {
        this.address = address;
    }

    public boolean hasRights(int userId, boolean checkOwner) {
        if (checkOwner && data.getOwnerId() == userId)
            return true;

        Player player = PlayerManager.getInstance().getPlayerById(userId);
        if (player != null) {
            if (player.getUserGroup().hasPermission("room.rights"))
                return true;
        }

        return false;
    }

    public boolean hasRights(int userId) {
        return hasRights(userId, true);
    }

    public boolean isOwner(int userId) {
        if (data.getOwnerId() == userId)
            return true;

        Player player = PlayerManager.getInstance().getPlayerById(userId);
        if (player != null) {
            if (player.getUserGroup().hasPermission("room.owner"))
                return true;
        }

        return false;
    }

    public void tryDispose() {
        List<Player> playerList = entityManager.getEntities(Player.class);

        if (!playerList.isEmpty())
            return;

        taskManager.stopTasks();
        RoomManager.getInstance().removeRoom(data.getId());

        isActive = false;
    }

    public void send(IMessageComposer composer, List<Player> specificUsers) {
        if (specificUsers == null)
            specificUsers = entityManager.getEntities(Player.class);

        for (Player player : specificUsers)
            player.send(composer);
    }

    public void send(IMessageComposer composer) {
        send(composer, null);
    }
}
