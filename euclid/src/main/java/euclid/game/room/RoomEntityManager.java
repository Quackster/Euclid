package euclid.game.room;

import euclid.game.Player;
import euclid.game.entity.Humanoid;
import euclid.game.entity.IEntity;
import euclid.game.item.ActiveItem;
import euclid.game.item.Item;
import euclid.game.item.PassiveItem;
import euclid.game.pathfinder.Position;
import euclid.messages.outgoing.ACTIVE_OBJECTS;
import euclid.messages.outgoing.HEIGHTMAP;
import euclid.messages.outgoing.ITEMS;
import euclid.messages.outgoing.LOGOUT;
import euclid.messages.outgoing.OBJECTS;
import euclid.messages.outgoing.STATUS;
import euclid.messages.outgoing.USERS;
import euclid.messages.outgoing.FLATPROPERTY;
import euclid.messages.outgoing.YOUARECONTROLLER;
import euclid.messages.outgoing.YOUAREOWNER;
import euclid.network.session.ConnectionMode;
import euclid.storage.database.access.RoomDao;

import java.util.ArrayList;
import java.util.List;

public class RoomEntityManager {

    private final Room room;
    private int instanceCounter;

    public RoomEntityManager(Room room) {
        this.room = room;
    }

    private int generateInstanceId() {
        return instanceCounter++;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getEntities(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (IEntity entity : room.getEntities().keySet()) {
            if (type.isInstance(entity)) {
                result.add((T) entity);
            }
        }
        return result;
    }

    public void enterRoom(Humanoid entity, Position entryPosition) {
        Humanoid existingPlayer = null;
        for (Humanoid h : getEntities(Humanoid.class)) {
            if (h.getEntityData().getName().equals(entity.getEntityData().getName())) {
                existingPlayer = h;
                break;
            }
        }

        if (existingPlayer != null) {
            leaveRoom(existingPlayer);
        }

        silentlyEntityRoom(entity, entryPosition);

        if (!(entity instanceof Player player))
            return;

        if (!room.getData().isPublicRoom()) {
            if (room.isOwner(player.getDetails().getId())) {
            } else if (room.hasRights(player.getDetails().getId(), false)) {
            }
        }
    }

    public void enterRoom(Humanoid entity) {
        enterRoom(entity, null);
    }

    public void silentlyEntityRoom(Humanoid entity, Position entryPosition) {
        if (entity.getRoomEntity().getRoom() != null)
            entity.getRoomEntity().getRoom().getEntityManager().leaveRoom(entity);

        if (!RoomManager.getInstance().hasRoom(room.getData().getId()))
            RoomManager.getInstance().addRoom(room);

        entity.getRoomEntity().reset();
        entity.getRoomEntity().setRoom(room);
        entity.getRoomEntity().setInstanceId(generateInstanceId());
        entity.getRoomEntity().setPosition(entryPosition != null ? entryPosition : room.getModel().getDoor());
        entity.getRoomEntity().setAuthenticateRoomId(-1);

        if (!room.isActive())
            tryInitialise();

        if (entity instanceof Player player) {
            room.getData().setUsersNow(room.getData().getUsersNow() + 1);
            RoomDao.setVisitorCount(room.getData().getId(), room.getData().getUsersNow());

            List<PassiveItem> passiveItems = new ArrayList<>();
            for (PassiveItem item : getEntities(PassiveItem.class)) {
                if (item.getDefinition().getData().isFloorItem() && item.getDefinition().getData().isVisible())
                    passiveItems.add(item);
            }
            player.send(new OBJECTS(room.getModel(), passiveItems));

            List<ActiveItem> floorActiveItems = new ArrayList<>();
            List<ActiveItem> wallActiveItems = new ArrayList<>();
            for (ActiveItem item : getEntities(ActiveItem.class)) {
                if (item.getDefinition().getData().isFloorItem())
                    floorActiveItems.add(item);
                if (item.getDefinition().getData().isWallItem())
                    wallActiveItems.add(item);
            }
            player.send(new ACTIVE_OBJECTS(floorActiveItems));
            player.send(new ITEMS(wallActiveItems));
            player.send(new HEIGHTMAP(room.getModel().getHeightmap()));

            if (room.getData().getWallpaper() > 0)
                player.send(new FLATPROPERTY("wallpaper", String.valueOf(room.getData().getWallpaper())));

            if (room.getData().getFloor() > 0)
                player.send(new FLATPROPERTY("floor", String.valueOf(room.getData().getFloor())));

            player.send(new USERS(getEntities(Humanoid.class)));
            player.send(new STATUS(getEntities(Humanoid.class)));

            if (room.isOwner(player.getDetails().getId())) {
                player.getRoomEntity().addStatus("flatctrl", "1");
                player.send(new YOUAREOWNER());
            } else if (room.hasRights(player.getDetails().getId()) || room.getData().isSuperUsers()) {
                player.getRoomEntity().addStatus("flatctrl", "1");
                player.send(new YOUARECONTROLLER());
            }
        }

        room.getEntities().putIfAbsent(entity, entity.getRoomEntity().getInstanceId());

        List<Humanoid> single = new ArrayList<>();
        single.add(entity);
        room.send(new USERS(single));

        entity.getRoomEntity().setNeedsUpdate(true);
    }

    public void silentlyEntityRoom(Humanoid entity) {
        silentlyEntityRoom(entity, null);
    }

    private void tryInitialise() {
        if (room.isActive())
            return;

        room.getItemManager().load();
        room.getTaskManager().load();
        room.getMapping().load();
        room.setActive(true);
    }

    public void leaveRoom(Humanoid entity, boolean hotelView) {
        room.getEntities().remove(entity);
        room.send(new LOGOUT(entity.getEntityData().getName()));

        RoomTile currentTile = entity.getRoomEntity().getPosition().getTile(room);
        if (currentTile != null)
            currentTile.removeEntity(entity);

        Position nextPosition = entity.getRoomEntity().getNext();
        if (nextPosition != null) {
            RoomTile nextTile = nextPosition.getTile(room);
            if (nextTile != null)
                nextTile.removeEntity(entity);
        }

        entity.getRoomEntity().reset();

        if (entity instanceof Player player) {
            room.getData().setUsersNow(room.getData().getUsersNow() - 1);
            RoomDao.setVisitorCount(room.getData().getId(), room.getData().getUsersNow());

            if (hotelView) {
                if (player.getConnectionMode() == ConnectionMode.PUBLIC
                        || player.getConnectionMode() == ConnectionMode.PRIVATE) {
                    player.getConnection().disconnect();
                }
            }
        }

        room.tryDispose();
    }

    public void leaveRoom(Humanoid entity) {
        leaveRoom(entity, false);
    }
}
