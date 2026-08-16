package euclid.game.catalogue;

import euclid.game.entity.IEntity;
import euclid.game.item.ItemDefinition;
import euclid.game.item.ItemManager;
import euclid.storage.database.data.CatalogueData;

public class CatalogueItem implements IEntity {

    private final CatalogueData data;

    public CatalogueItem(CatalogueData data) {
        this.data = data;
    }

    public CatalogueData getData() {
        return data;
    }

    public ItemDefinition getDefinition() {
        return ItemManager.getInstance().getDefinition(data.getDefinitionId());
    }
}
