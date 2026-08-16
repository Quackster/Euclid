package euclid.game.entity;

import euclid.game.Player;
import euclid.game.room.tasks.PlayerTaskObject;

public class RoomPlayer extends RoomEntity {

    private final Player player;

    public RoomPlayer(Player player) {
        super(player);
        this.player = player;
        this.setTaskObject(new PlayerTaskObject(player));
    }

    public Player getPlayer() {
        return player;
    }
}
