package euclid.messages.incoming;

import euclid.game.Player;
import euclid.messages.IMessageEvent;
import euclid.network.streams.Request;
import euclid.util.RegisterUtil;
import euclid.util.extensions.StringExtensions;
import euclid.messages.outgoing.NAME_APPROVED;
import euclid.messages.outgoing.NAME_UNACCEPTABLE;

public class APPROVENAME implements IMessageEvent {

    @Override
    public boolean authenticationRequired() {
        return false;
    }

    @Override
    public void handle(Player player, Request request) {
        if (!(request.getArgumentAmount() > 0)) {
            return;
        }

        String name = StringExtensions.filterInput(request.getArgument(0));

        if (RegisterUtil.isValidName(name)) {
            player.send(new NAME_APPROVED());
        } else {
            player.send(new NAME_UNACCEPTABLE());
        }
    }
}
