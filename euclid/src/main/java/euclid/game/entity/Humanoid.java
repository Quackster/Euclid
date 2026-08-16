package euclid.game.entity;

import euclid.storage.database.data.IEntityData;

public class Humanoid implements IEntity {

    protected IEntityData entityData;
    protected EntityType entityType;
    protected RoomEntity roomEntity;

    public IEntityData getEntityData() {
        return entityData;
    }

    public void setEntityData(IEntityData entityData) {
        this.entityData = entityData;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }
}
