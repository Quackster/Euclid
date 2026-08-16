package euclid.game.item;

import euclid.storage.database.data.ItemDefinitionData;

public class ItemDefinition {

    private final ItemDefinitionData data;

    public ItemDefinition(ItemDefinitionData data) {
        this.data = data;
    }

    public ItemDefinitionData getData() {
        return data;
    }

    public double getPositiveTopHeight() {
        if (data.getHeight() < 0) {
            return 0;
        }
        return data.getHeight();
    }
}
