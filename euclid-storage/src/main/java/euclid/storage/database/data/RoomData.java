package euclid.storage.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "room")
public class RoomData {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category_id", insertable = false)
    private int categoryId;

    @Column(name = "visitors_now", insertable = false)
    private int usersNow;

    @Column(name = "visitors_max", insertable = false)
    private int usersMax;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "access_type", insertable = false)
    private RoomStatus accessType;

    @Column(name = "password", insertable = false)
    private String password;

    @Column(name = "model_id")
    private int modelId;

    @Column(name = "ccts", insertable = false)
    private String ccts;

    @Column(name = "wallpaper", insertable = false)
    private int wallpaper;

    @Column(name = "floor", insertable = false)
    private int floor;

    @Column(name = "rating", insertable = false)
    private int rating;

    @Column(name = "show_name", insertable = false)
    private boolean showName;

    @Column(name = "super_users", insertable = false)
    private boolean superUsers;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private PlayerData ownerData;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private NavigatorCategoryData category;

    @Transient
    private String landscape;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUsersNow() {
        return usersNow;
    }

    public void setUsersNow(int usersNow) {
        this.usersNow = usersNow;
    }

    public int getUsersMax() {
        return usersMax;
    }

    public void setUsersMax(int usersMax) {
        this.usersMax = usersMax;
    }

    public RoomStatus getAccessType() {
        return accessType;
    }

    public void setAccessType(RoomStatus accessType) {
        this.accessType = accessType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getCcts() {
        return ccts;
    }

    public void setCcts(String ccts) {
        this.ccts = ccts;
    }

    public int getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(int wallpaper) {
        this.wallpaper = wallpaper;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public boolean isSuperUsers() {
        return superUsers;
    }

    public void setSuperUsers(boolean superUsers) {
        this.superUsers = superUsers;
    }

    public PlayerData getOwnerData() {
        return ownerData;
    }

    public void setOwnerData(PlayerData ownerData) {
        this.ownerData = ownerData;
    }

    public NavigatorCategoryData getCategory() {
        return category;
    }

    public void setCategory(NavigatorCategoryData category) {
        this.category = category;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    public boolean isPrivateRoom() {
        return ownerId > 0;
    }

    public boolean isPublicRoom() {
        return ownerId == 0;
    }

    public static RoomStatus toStatusEnum(int roomAccess) {
        return RoomStatus.toStatusEnum(roomAccess);
    }
}
