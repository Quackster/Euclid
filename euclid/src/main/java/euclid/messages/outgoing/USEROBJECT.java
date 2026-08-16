package euclid.messages.outgoing;

import euclid.messages.IMessageComposer;
import euclid.network.streams.util.KeyValueEntry;
import euclid.storage.database.data.PlayerData;

public class USEROBJECT extends IMessageComposer {

    private final PlayerData playerData;

    public USEROBJECT(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public void write() {
        getData().add(new KeyValueEntry("name", playerData.getName(), "="));
        getData().add(new KeyValueEntry("figure", playerData.getFigure(), "="));
        getData().add(new KeyValueEntry("email", playerData.getEmail(), "="));
        getData().add(new KeyValueEntry("birthday", playerData.getBirthday(), "="));
        getData().add(new KeyValueEntry("phonenumber", playerData.getPhoneNumber(), "="));
        getData().add(new KeyValueEntry("customData", playerData.getCustomData(), "="));
        getData().add(new KeyValueEntry("has_read_agreement", "1", "="));
        getData().add(new KeyValueEntry("sex", playerData.getSex(), "="));
        getData().add(new KeyValueEntry("country", playerData.getCountry(), "="));
        getData().add(new KeyValueEntry("has_special_rights", "1", "="));
        getData().add(new KeyValueEntry("badge_type", "", "="));
    }
}
