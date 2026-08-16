package euclid.messages;

import euclid.game.Player;
import euclid.network.streams.Request;

public interface IMessageEvent {

    default boolean authenticationRequired() {
        return true;
    }

    void handle(Player player, Request request);
}
