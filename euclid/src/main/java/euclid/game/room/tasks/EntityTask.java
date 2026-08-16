package euclid.game.room.tasks;

import euclid.game.entity.Humanoid;
import euclid.game.room.Room;

import java.util.List;

public class EntityTask implements IRoomTask {

    private final Room room;
    private java.util.concurrent.ScheduledFuture<?> scheduledFuture;

    public EntityTask(Room room) {
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
            if (entity.getRoomEntity().getTaskObject() != null) {
                entity.getRoomEntity().getTaskObject().tick();
            }
        }
    }
}
