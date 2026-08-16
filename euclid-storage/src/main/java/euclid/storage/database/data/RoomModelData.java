package euclid.storage.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_model")
public class RoomModelData {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "model")
    private String model;

    @Column(name = "door_x")
    private int doorX;

    @Column(name = "door_y")
    private int doorY;

    @Column(name = "door_z")
    private int doorZ;

    @Column(name = "door_dir")
    private int doorDirection;

    @Column(name = "heightmap")
    private String heightmap;

    @Column(name = "is_club_only")
    private boolean clubOnly;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getDoorX() {
        return doorX;
    }

    public void setDoorX(int doorX) {
        this.doorX = doorX;
    }

    public int getDoorY() {
        return doorY;
    }

    public void setDoorY(int doorY) {
        this.doorY = doorY;
    }

    public int getDoorZ() {
        return doorZ;
    }

    public void setDoorZ(int doorZ) {
        this.doorZ = doorZ;
    }

    public int getDoorDirection() {
        return doorDirection;
    }

    public void setDoorDirection(int doorDirection) {
        this.doorDirection = doorDirection;
    }

    public String getHeightmap() {
        return heightmap;
    }

    public void setHeightmap(String heightmap) {
        this.heightmap = heightmap;
    }

    public boolean isClubOnly() {
        return clubOnly;
    }

    public void setClubOnly(boolean clubOnly) {
        this.clubOnly = clubOnly;
    }
}
