package euclid.game.room.tasks;

public interface IRoomTask {
    void createTask();
    void stopTask();
    void tick();
}
