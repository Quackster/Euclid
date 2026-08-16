package euclid.storage.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "item")
public class ItemData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "user_id")
    private int ownerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private PlayerData ownerData;

    @Column(name = "room_id")
    private int roomId;

    @Column(name = "definition_id")
    private int definitionId;

    @Column(name = "x", insertable = false)
    private int x;

    @Column(name = "y", insertable = false)
    private int y;

    @Column(name = "z", insertable = false)
    private double z;

    @Column(name = "wall_position", insertable = false)
    private String wallPosition;

    @Column(name = "rotation", insertable = false)
    private int rotation;

    @Column(name = "custom_data")
    private String customData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public PlayerData getOwnerData() {
        return ownerData;
    }

    public void setOwnerData(PlayerData ownerData) {
        this.ownerData = ownerData;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(int definitionId) {
        this.definitionId = definitionId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getWallPosition() {
        return wallPosition;
    }

    public void setWallPosition(String wallPosition) {
        this.wallPosition = wallPosition;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }
}
