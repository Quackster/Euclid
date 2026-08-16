package euclid.game.catalogue;

import euclid.game.entity.IEntity;

public class CatalogueOrder implements IEntity {

    private final CatalogueItem item;
    private final String customData;

    public CatalogueOrder(CatalogueItem item, String customData) {
        this.item = item;
        this.customData = customData;
    }

    public CatalogueItem getItem() {
        return item;
    }

    public String getCustomData() {
        return customData;
    }
}
