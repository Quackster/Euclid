package euclid.game.room.tasks;

import euclid.game.entity.Humanoid;
import euclid.game.entity.RoomEntity;
import euclid.game.item.ActiveItem;
import euclid.game.item.Item;
import euclid.game.pathfinder.Position;
import euclid.game.room.Room;
import euclid.game.room.RoomTile;
import euclid.game.room.RoomTimerManager;

import java.util.List;

public class MaintenanceTask implements IRoomTask {

    private final Room room;
    private java.util.concurrent.ScheduledFuture<?> scheduledFuture;

    public MaintenanceTask(Room room) {
        this.room = room;
    }

    @Override
    public void createTask() {
        scheduledFuture = euclid.network.GameServer.getInstance().getScheduledExecutorService().scheduleAtFixedRate(
                this::tick, 0, 500, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @Override
    public void stopTask() {
        if (scheduledFuture != null)
            scheduledFuture.cancel(true);
    }

    @Override
    public void tick() {
        List<Humanoid> entities = room.getEntityManager().getEntities(Humanoid.class);

        for (Humanoid entity : entities) {
            RoomEntity roomEntity = entity.getRoomEntity();
            RoomTimerManager timerManager = roomEntity.getTimerManager();

            if (timerManager.getSpeechBubbleDate() > 0 && timerManager.getSpeechBubbleDate() < euclid.util.DateUtil.getUnixTimestamp()) {
                timerManager.resetSpeechBubbleTimer();
            }

            if (roomEntity.isDancing())
                continue;

            if (roomEntity.isWalking()) {
                Position next = roomEntity.getPathList().isEmpty() ? null : roomEntity.getPathList().get(0);
                if (next != null) {
                    RoomTile nextTile = next.getTile(room);
                    if (nextTile != null) {
                        RoomTile currentTile = roomEntity.getPosition().getTile(room);

                        if (currentTile != null && !currentTile.getPosition().equals(roomEntity.getPosition())) {
                            currentTile.removeEntity(entity);
                            nextTile.addEntity(entity);
                        }
                    }
                }
            }

            if (!roomEntity.isWalking())
                continue;

            roomEntity.setNeedsUpdate(true);

            if (roomEntity.getPathList().isEmpty()) {
                roomEntity.setWalking(false);
                continue;
            }

            Position next = roomEntity.getPathList().get(0);
            roomEntity.setNext(next);

            if (roomEntity.isBeingKicked() && roomEntity.getNext().equals(room.getModel().getDoor())) {
                roomEntity.setNeedsUpdate(true);
            }
        }
    }
}
