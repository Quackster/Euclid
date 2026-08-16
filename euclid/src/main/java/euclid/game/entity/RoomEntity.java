package euclid.game.entity;

import euclid.game.Player;
import euclid.game.item.Item;
import euclid.game.pathfinder.Pathfinder;
import euclid.game.pathfinder.Position;
import euclid.game.pathfinder.Rotation;
import euclid.game.room.Room;
import euclid.game.room.RoomTile;
import euclid.game.room.RoomTimerManager;
import euclid.game.room.tasks.ITaskObject;
import euclid.game.util.ChatMessageType;
import euclid.messages.outgoing.CHAT;
import euclid.messages.outgoing.SHOUT;
import euclid.messages.outgoing.WHISPER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomEntity {

    private Humanoid entity;
    private Room room;
    private Position position;
    private Position next;
    private Position goal;
    private List<Position> pathList;
    private boolean isWalking;
    private int instanceId;
    private boolean needsUpdate;
    private RoomTimerManager timerManager;
    private String authenticateTeleporterId;
    private int authenticateRoomId;
    private boolean walkingAllowed = true;
    private ITaskObject taskObject;
    private int danceId;
    private int effectId;
    private boolean beingKicked;
    private Map<String, EntityStatus> statuses;

    public RoomEntity(Humanoid entity) {
        this.entity = entity;
        this.timerManager = new RoomTimerManager();
        this.authenticateTeleporterId = null;
        this.statuses = new HashMap<>();
        this.pathList = new ArrayList<>();
    }

    public Humanoid getEntity() {
        return entity;
    }

    public void setEntity(Humanoid entity) {
        this.entity = entity;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getNext() {
        return next;
    }

    public void setNext(Position next) {
        this.next = next;
    }

    public Position getGoal() {
        return goal;
    }

    public void setGoal(Position goal) {
        this.goal = goal;
    }

    public List<Position> getPathList() {
        return pathList;
    }

    public void setPathList(List<Position> pathList) {
        this.pathList = pathList;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    public boolean isNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public int getRoomId() {
        return room != null ? room.getData().getId() : 0;
    }

    public RoomTile getCurrentTile() {
        return position != null ? position.getTile(room) : null;
    }

    public Item getCurrentItem() {
        RoomTile tile = getCurrentTile();
        return tile != null ? tile.getHighestItem() : null;
    }

    public RoomTimerManager getTimerManager() {
        return timerManager;
    }

    public void setTimerManager(RoomTimerManager timerManager) {
        this.timerManager = timerManager;
    }

    public String getAuthenticateTeleporterId() {
        return authenticateTeleporterId;
    }

    public void setAuthenticateTeleporterId(String authenticateTeleporterId) {
        this.authenticateTeleporterId = authenticateTeleporterId;
    }

    public int getAuthenticateRoomId() {
        return authenticateRoomId;
    }

    public void setAuthenticateRoomId(int authenticateRoomId) {
        this.authenticateRoomId = authenticateRoomId;
    }

    public boolean isWalkingAllowed() {
        return walkingAllowed;
    }

    public void setWalkingAllowed(boolean walkingAllowed) {
        this.walkingAllowed = walkingAllowed;
    }

    public ITaskObject getTaskObject() {
        return taskObject;
    }

    public void setTaskObject(ITaskObject taskObject) {
        this.taskObject = taskObject;
    }

    public int getDanceId() {
        return danceId;
    }

    public void setDanceId(int danceId) {
        this.danceId = danceId;
    }

    public boolean isDancing() {
        return danceId > 0;
    }

    public boolean hasEffect() {
        return effectId > 0;
    }

    public int getEffectId() {
        return effectId;
    }

    public void setEffectId(int effectId) {
        this.effectId = effectId;
    }

    public boolean isBeingKicked() {
        return beingKicked;
    }

    public void setBeingKicked(boolean beingKicked) {
        this.beingKicked = beingKicked;
    }

    public Map<String, EntityStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Map<String, EntityStatus> statuses) {
        this.statuses = statuses;
    }

    public void reset() {
        statuses = new HashMap<>();
        isWalking = false;
        goal = null;
        next = null;
        instanceId = -1;
        danceId = 0;
        room = null;
        timerManager.reset();
        walkingAllowed = true;
    }

    public void talk(ChatMessageType chatMessageType, String chatMsg, List<Player> receiveMessages) {
        if (receiveMessages == null)
            receiveMessages = new ArrayList<>(room.getEntityManager().getEntities(Player.class));

        List<Player> distinct = new ArrayList<>(new java.util.LinkedHashSet<>(receiveMessages));

        for (Player player : distinct) {
            switch (chatMessageType) {
                case CHAT:
                    player.send(new CHAT(entity.getEntityData().getName(), chatMsg));
                    break;
                case SHOUT:
                    player.send(new SHOUT(entity.getEntityData().getName(), chatMsg));
                    break;
                case WHISPER:
                    player.send(new WHISPER(entity.getEntityData().getName(), chatMsg));
                    break;
            }
        }
    }

    public void talk(ChatMessageType chatMessageType, String chatMsg) {
        talk(chatMessageType, chatMsg, null);
    }

    private int getChatGesture(String chatMsg) {
        chatMsg = chatMsg.toLowerCase();

        if (chatMsg.contains(":)") || chatMsg.contains(":d") || chatMsg.contains("=]")
                || chatMsg.contains("=d") || chatMsg.contains(":>")) {
            return 1;
        }

        if (chatMsg.contains(">:(") || chatMsg.contains(":@"))
            return 2;

        if (chatMsg.contains(":o"))
            return 3;

        if (chatMsg.contains(":(") || chatMsg.contains("=[") || chatMsg.contains(":'(") || chatMsg.contains("='["))
            return 4;

        return 0;
    }

    public void move(int x, int y) {
        if (room == null)
            return;

        if (next != null) {
            Position oldPosition = next.copy();
            position.setX(oldPosition.getX());
            position.setY(oldPosition.getY());
            position.setZ(room.getModel().getTileHeights()[oldPosition.getX()][oldPosition.getY()]);
            needsUpdate = true;
        }

        goal = new Position(x, y);

        RoomTile goalTile = goal.getTile(room);

        if (!RoomTile.isValidTile(room, entity, goal))
            return;

        List<Position> pathList = Pathfinder.findPath(entity, room, position, goal);

        if (pathList == null || pathList.isEmpty())
            return;

        this.pathList = pathList;
        isWalking = true;
    }

    public void kick(boolean allowWalking) {
        this.beingKicked = true;
        Position doorLocation = this.room.getModel().getDoor();

        if (this.position.equals(doorLocation)) {
            this.room.getEntityManager().leaveRoom(this.entity);
            return;
        }

        this.move(doorLocation.getX(), doorLocation.getY());
        this.walkingAllowed = allowWalking;

        if (!this.isWalking) {
            this.room.getEntityManager().leaveRoom(this.entity);
        }
    }

    public void kick() {
        kick(true);
    }

    public void stopWalking() {
        if (!this.isWalking)
            return;

        this.isWalking = false;
        this.pathList.clear();
        this.next = null;
        this.removeStatus("mv");
        this.interactItem();
        this.needsUpdate = true;

        boolean leaveRoom = this.beingKicked;
        Position doorPosition = this.room.getModel().getDoor();

        if (doorPosition.equals(this.position)) {
            leaveRoom = true;
        }

        if (leaveRoom || this.beingKicked) {
            this.room.getEntityManager().leaveRoom(this.entity, true);
            return;
        }
    }

    public void interactItem() {
        RoomTile roomTile = getCurrentTile();

        if (roomTile == null)
            return;

        position.setZ(roomTile.getWalkingHeight());

        Item item = getCurrentItem();

        if (item == null ||
                (!item.getDefinition().getData().isChair()
                && !item.getDefinition().getData().isBed())) {
            if (containsStatus("sit") || containsStatus("lay")) {
                removeStatus("sit");
                removeStatus("lay");
            }
        }

        if (item != null) {
            if (item.getDefinition().getData().isChair()) {
                position.setBodyRotation(item.getPosition().getRotation());
                position.setHeadRotation(item.getPosition().getRotation());
                addStatus("sit", String.valueOf((int) item.getHeight()));
                needsUpdate = true;
            }

            if (item.getDefinition().getData().isBed()) {
                position.setBodyRotation(item.getPosition().getRotation());
                position.setHeadRotation(item.getPosition().getRotation());
                addStatus("lay", String.valueOf((int) item.getHeight()));
                needsUpdate = true;
            }
        }

        this.needsUpdate = true;
    }

    public boolean containsStatus(String statusKey) {
        return statuses.containsKey(statusKey);
    }

    public void addStatus(String key, String value) {
        this.removeStatus(key);
        statuses.put(key, new EntityStatus(value));
    }

    public void removeStatus(String key) {
        statuses.remove(key);
    }

    public void warp(Position targetPosition, boolean instantUpdate) {
        RoomTile oldTile = getCurrentTile();

        if (oldTile != null) {
            oldTile.removeEntity(entity);
        }

        if (next != null) {
            RoomTile nextTile = next.getTile(room);
            if (nextTile != null) {
                nextTile.removeEntity(entity);
            }
        }

        position = targetPosition.copy();
        refreshHeight(targetPosition);

        RoomTile newTile = getCurrentTile();

        if (newTile != null) {
            newTile.addEntity(entity);
        }

        if (instantUpdate && room != null) {
            interactItem();
        }
    }

    public void warp(Position targetPosition) {
        warp(targetPosition, false);
    }

    private void refreshHeight(Position newPosition) {
        Position targetPosition = newPosition != null ? newPosition : position;

        RoomTile oldTile = position.getTile(room);
        RoomTile newTile = targetPosition.getTile(room);

        if (oldTile != null && newTile != null && oldTile.getWalkingHeight() != newTile.getWalkingHeight()) {
            position.setZ(newTile.getWalkingHeight());
            needsUpdate = true;
        }
    }
}
