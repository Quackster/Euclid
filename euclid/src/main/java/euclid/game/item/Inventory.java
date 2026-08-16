package euclid.game.item;

import euclid.game.ILoadable;
import euclid.game.Player;
import euclid.messages.outgoing.STRIPINFO;
import euclid.storage.database.access.ItemDao;
import euclid.storage.database.data.ItemData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory implements ILoadable {

    public static final int HAND_SIZE = 6;

    private final Player player;
    private ConcurrentHashMap<Integer, Item> items;
    private int currentPage;

    public Inventory(Player player) {
        this.player = player;
        this.items = new ConcurrentHashMap<>();
    }

    @Override
    public void load() {
        items = new ConcurrentHashMap<>();
        List<ItemData> userItems = ItemDao.getUserItems(player.getDetails().getId());
        for (ItemData itemData : userItems) {
            Item item = new Item(itemData);
            items.put(itemData.getId(), item);
        }
        currentPage = 0;
    }

    public ConcurrentHashMap<Integer, Item> getItems() {
        return items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void turnPage(String mode) {
        int inventoryPages = countPages(items.size(), HAND_SIZE) - 1;

        switch (mode) {
            case "last":
                currentPage = inventoryPages;
                break;
            case "new":
                currentPage = 0;
                break;
            case "next":
                currentPage++;
                break;
        }

        if (currentPage > inventoryPages)
            currentPage = 0;

        List<Item> pagedItems = new ArrayList<>(items.values()).subList(
                Math.min(currentPage * HAND_SIZE, items.size()),
                Math.min((currentPage + 1) * HAND_SIZE, items.size()));

        player.send(new STRIPINFO(currentPage, items.size(), pagedItems));
    }

    private int countPages(int count, int pageSize) {
        return (int) Math.ceil((double) count / pageSize);
    }

    public Item getItem(int itemId) {
        return items.get(itemId);
    }

    public void addItem(Item item) {
        items.putIfAbsent(item.getData().getId(), item);
    }

    public void removeItem(Item item) {
        items.remove(item.getData().getId());
    }
}
