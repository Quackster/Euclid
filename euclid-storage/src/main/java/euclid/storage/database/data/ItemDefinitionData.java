package euclid.storage.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "item_definitions")
public class ItemDefinitionData {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "sprite")
    private String sprite;

    @Column(name = "colour")
    private String colour;

    @Column(name = "length")
    private int length;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @Column(name = "data_class")
    private String dataClass;

    @Column(name = "is_floor_item")
    private boolean floorItem;

    @Column(name = "is_wall_item")
    private boolean wallItem;

    @Column(name = "is_stackable")
    private boolean stackable;

    @Column(name = "is_bed")
    private boolean bed;

    @Column(name = "is_chair")
    private boolean chair;

    @Column(name = "is_walkable")
    private boolean walkable;

    @Column(name = "is_decoration")
    private boolean decoration;

    @Column(name = "is_teleporter")
    private boolean teleporter;

    @Column(name = "is_post_it")
    private boolean postIt;

    @Column(name = "requires_touching_for_interaction")
    private boolean requiresTouchingForInteraction;

    @Column(name = "requires_rights_for_interaction")
    private boolean requiresRightsForInteraction;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Transient
    private boolean visible;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    public boolean isFloorItem() {
        return floorItem;
    }

    public void setFloorItem(boolean floorItem) {
        this.floorItem = floorItem;
    }

    public boolean isWallItem() {
        return wallItem;
    }

    public void setWallItem(boolean wallItem) {
        this.wallItem = wallItem;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public boolean isBed() {
        return bed;
    }

    public void setBed(boolean bed) {
        this.bed = bed;
    }

    public boolean isChair() {
        return chair;
    }

    public void setChair(boolean chair) {
        this.chair = chair;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isDecoration() {
        return decoration;
    }

    public void setDecoration(boolean decoration) {
        this.decoration = decoration;
    }

    public boolean isTeleporter() {
        return teleporter;
    }

    public void setTeleporter(boolean teleporter) {
        this.teleporter = teleporter;
    }

    public boolean isPostIt() {
        return postIt;
    }

    public void setPostIt(boolean postIt) {
        this.postIt = postIt;
    }

    public boolean isRequiresTouchingForInteraction() {
        return requiresTouchingForInteraction;
    }

    public void setRequiresTouchingForInteraction(boolean requiresTouchingForInteraction) {
        this.requiresTouchingForInteraction = requiresTouchingForInteraction;
    }

    public boolean isRequiresRightsForInteraction() {
        return requiresRightsForInteraction;
    }

    public void setRequiresRightsForInteraction(boolean requiresRightsForInteraction) {
        this.requiresRightsForInteraction = requiresRightsForInteraction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
