package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.catalogue.CatalogueItem;
import euclid.game.catalogue.CatalogueManager;
import euclid.game.catalogue.CatalogueOrder;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.ORDERINFO;
import euclid.network.streams.Request;

public class GETORDERINFO implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        if (!player.isAuthenticated()) {
            return;
        }

        request.skip(1);

        String section = request.getArgument(0);
        String id = request.getArgument(1);
        String extra = request.getArgument(2);

        CatalogueItem item = CatalogueManager.getInstance().getOffers().stream()
                .filter(x -> x.getData().getId().equals(id))
                .findFirst()
                .orElse(null);

        if (item == null || !item.getData().getSection().equals(section)) {
            // Invalid catalogue article
        } else {
            String customData = "";

            if (item.getDefinition().getData().isDecoration() || item.getDefinition().getData().getSprite().equals("poster")) {
                customData = extra;
            }

            CatalogueManager.getInstance().getOrderHistory().put(player.getDetails().getId(),
                    new CatalogueOrder(item, customData));
            player.send(new ORDERINFO(item.getData().getId(), item.getData().getPrice(),
                    customData, item.getDefinition().getData().getName()));
        }
    }
}
