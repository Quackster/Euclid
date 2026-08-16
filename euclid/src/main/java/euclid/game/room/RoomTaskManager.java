package euclid.game.room;

import euclid.game.ILoadable;
import euclid.game.room.tasks.EntityTask;
import euclid.game.room.tasks.IRoomTask;

import java.util.ArrayList;
import java.util.List;

public class RoomTaskManager implements ILoadable {

    private final Room room;
    private final List<IRoomTask> tasks;

    public RoomTaskManager(Room room) {
        this.room = room;
        this.tasks = new ArrayList<>();
    }

    public List<IRoomTask> getTasks() {
        return tasks;
    }

    @Override
    public void load() {
        stopTasks();
        tasks.clear();
        registerTasks();

        for (IRoomTask task : tasks)
            task.createTask();
    }

    private void registerTasks() {
        tasks.add(new EntityTask(room));
    }

    public void stopTasks() {
        for (IRoomTask task : tasks)
            task.stopTask();
    }

    public static int getProcessTime(double time) {
        return (int) (time / 0.5);
    }
}
