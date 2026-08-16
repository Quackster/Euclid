package euclid.game.values;

import euclid.game.ILoadable;
import euclid.storage.database.access.SettingDao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValueManager implements ILoadable {

    public static final ValueManager INSTANCE = new ValueManager();
    private Map<String, String> clientValues;

    private ValueManager() {
    }

    public static ValueManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void load() {
        clientValues = SettingDao.getSettings();

        for (Map.Entry<String, String> entry : getDefaultValues().entrySet()) {
            if (!SettingDao.hasSetting(entry.getKey())) {
                SettingDao.saveSetting(entry.getKey(), entry.getValue());
                clientValues.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new LinkedHashMap<>();
        defaults.put("max.friends.normal", "300");
        defaults.put("max.friends.hc", "600");
        defaults.put("max.friends.vip", "1100");
        defaults.put("max.rooms.allowed", "100");
        defaults.put("max.rooms.allowed.subscribed", "200");
        defaults.put("timer.speech.bubble", "15");
        defaults.put("inventory.items.per.page", "500");
        defaults.put("catalogue.subscription.page", "63");
        defaults.put("club.gift.interval", "1");
        defaults.put("club.gift.interval.type", "MONTH");
        return defaults;
    }

    public int getInt(String key) {
        String value = clientValues.get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public String getString(String key) {
        return clientValues.get(key);
    }
}
