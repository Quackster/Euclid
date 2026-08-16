package euclid.storage.database.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_category")
public class NavigatorCategoryData {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "parent_id")
    private int parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "is_node")
    private boolean node;

    @Column(name = "visible_rank")
    private int visibleRank;

    @Column(name = "is_public_spaces")
    private boolean publicSpaces;

    @Column(name = "is_trading_allowed")
    private boolean tradingAllowed;

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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNode() {
        return node;
    }

    public void setNode(boolean node) {
        this.node = node;
    }

    public int getVisibleRank() {
        return visibleRank;
    }

    public void setVisibleRank(int visibleRank) {
        this.visibleRank = visibleRank;
    }

    public boolean isPublicSpaces() {
        return publicSpaces;
    }

    public void setPublicSpaces(boolean publicSpaces) {
        this.publicSpaces = publicSpaces;
    }

    public boolean isTradingAllowed() {
        return tradingAllowed;
    }

    public void setTradingAllowed(boolean tradingAllowed) {
        this.tradingAllowed = tradingAllowed;
    }
}
