package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.catalogue.CatalogueManager;
import euclid.game.catalogue.CatalogueOrder;
import euclid.game.item.Item;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.ADDSTRIPITEM;
import euclid.network.streams.Request;
import euclid.storage.database.access.ItemDao;
import euclid.storage.database.data.ItemData;

public class PURCHASE implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (!CatalogueManager.getInstance().getOrderHistory().containsKey(player.getDetails().getId())) {
            return;
        }

        CatalogueOrder order = CatalogueManager.getInstance().getOrderHistory().get(player.getDetails().getId());
        CatalogueManager.getInstance().getOrderHistory().remove(player.getDetails().getId());

        if (order == null) {
            return;
        }

        ItemData itemData = new ItemData();
        itemData.setOwnerId(player.getDetails().getId());
        itemData.setDefinitionId(order.getItem().getDefinition().getData().getId());
        itemData.setCustomData(order.getCustomData());

        ItemDao.createItem(itemData);

        player.getInventory().addItem(new Item(itemData));
        player.send(new ADDSTRIPITEM());
    }
}
