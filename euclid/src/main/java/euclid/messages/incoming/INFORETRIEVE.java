package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.storage.database.access.UserDao;
import euclid.messages.outgoing.USEROBJECT;
import euclid.storage.database.data.PlayerData;

public class INFORETRIEVE implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        String username = request.getArgument(0);
        String password = request.getArgument(1);

        PlayerData playerData = UserDao.login(username, password);

        if (playerData == null) {
            return;
        }

        player.send(new USEROBJECT(playerData));
    }
}
