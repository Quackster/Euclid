package euclid.game.entity;

import euclid.game.pathfinder.Position;
import euclid.storage.database.data.IEntityData;

import java.util.HashMap;
import java.util.Map;

public class EntityState {

    private final int id;
    private final int instanceId;
    private final IEntityData details;
    private final EntityType entityType;
    private final Position position;
    private final Map<String, EntityStatus> statuses;

    public EntityState(int id, int instanceId, IEntityData details, EntityType entityType,
                       euclid.game.room.Room room, Position position, Map<String, EntityStatus> statuses) {
        this.id = id;
        this.instanceId = instanceId;
        this.details = details;
        this.entityType = entityType;
        this.position = position;
        this.statuses = statuses != null ? statuses : new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public IEntityData getDetails() {
        return details;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Position getPosition() {
        return position;
    }

    public Map<String, EntityStatus> getStatuses() {
        return statuses;
    }
}
