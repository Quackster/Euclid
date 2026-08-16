package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.storage.database.access.UserDao;

public class UPDATE implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        var registerValues = request.getKeyValues();

        if (!registerValues.containsKey("name") ||
                !registerValues.containsKey("password") ||
                !registerValues.containsKey("email") ||
                !registerValues.containsKey("figure") ||
                !registerValues.containsKey("directMail") ||
                !registerValues.containsKey("birthday") ||
                !registerValues.containsKey("phonenumber") ||
                !registerValues.containsKey("customData") ||
                !registerValues.containsKey("has_read_agreement") ||
                !registerValues.containsKey("sex") ||
                !registerValues.containsKey("country")) {
            return;
        }

        var data = player.getDetails();

        data.setName(registerValues.get("name"));
        data.setPassword(registerValues.get("password"));
        data.setEmail(registerValues.get("email"));
        data.setFigure(registerValues.get("figure"));
        data.setDirectMail("1".equals(registerValues.get("directMail")));
        data.setBirthday(registerValues.get("birthday"));
        data.setPhoneNumber(registerValues.get("phonenumber"));
        data.setCustomData(registerValues.get("customData"));
        data.setSex("Male".equals(registerValues.get("sex")) ? "M" : "F");
        data.setCountry(registerValues.get("country"));

        UserDao.saveOrUpdate(data);
    }
}
