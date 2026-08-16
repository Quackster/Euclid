package euclid.game.room;

import euclid.game.ILoadable;
import euclid.storage.database.access.RoomDao;
import euclid.storage.database.data.RoomData;
import euclid.storage.database.data.RoomModelData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager implements ILoadable {

    public static final RoomManager INSTANCE = new RoomManager();

    private final ConcurrentHashMap<Integer, Room> rooms;
    private List<RoomModel> roomModels;

    private RoomManager() {
        this.rooms = new ConcurrentHashMap<>();
    }

    public static RoomManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void load() {
        List<RoomModelData> models = RoomDao.getModels();
        roomModels = new ArrayList<>();
        for (RoomModelData modelData : models) {
            roomModels.add(new RoomModel(modelData));
        }
    }

    public ConcurrentHashMap<Integer, Room> getRooms() {
        return rooms;
    }

    public List<RoomModel> getRoomModels() {
        return roomModels;
    }

    public boolean hasRoom(int roomId) {
        return rooms.containsKey(roomId);
    }

    public void removeRoom(int roomId) {
        rooms.remove(roomId);
    }

    public void addRoom(Room room) {
        if (room == null)
            return;
        if (rooms.containsKey(room.getData().getId()))
            return;
        rooms.putIfAbsent(room.getData().getId(), room);
    }

    public Room getRoom(int roomId) {
        Room room = rooms.get(roomId);
        if (room != null)
            return room;

        RoomData data = RoomDao.getRoomData(roomId);
        if (data != null) {
            return new Room(data);
        }

        return null;
    }

    public List<Room> replaceQueryRooms(List<RoomData> roomsFromDatabase) {
        List<Room> rooms = new ArrayList<>();

        for (RoomData roomData : roomsFromDatabase) {
            Room room = this.rooms.get(roomData.getId());
            if (room != null)
                rooms.add(room);
            else
                rooms.add(Room.wrap(roomData));
        }

        return rooms;
    }

    public static List<Room> sortRooms(List<Room> list) {
        return list.stream()
                .sorted(Comparator.comparingInt((Room r) -> r.getData().getRating()).reversed()
                        .thenComparing(Comparator.comparingInt((Room r) -> r.getData().getUsersNow()).reversed()))
                .toList();
    }
}
