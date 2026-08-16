package euclid.game.room.tasks;

import euclid.game.Player;
import euclid.game.entity.Humanoid;
import euclid.game.item.ActiveItem;
import euclid.game.item.Item;
import euclid.game.pathfinder.Position;
import euclid.game.room.Room;
import euclid.game.room.RoomTile;

import java.util.List;

public class PlayerTaskObject implements ITaskObject {

    private final Player player;

    public PlayerTaskObject(Player player) {
        this.player = player;
    }

    @Override
    public void tick() {
        if (player.getRoomEntity().isNeedsUpdate()) {
            player.getRoomEntity().setNeedsUpdate(false);

            if (player.getRoomEntity().isBeingKicked()) {
                player.getRoomEntity().setBeingKicked(false);
                return;
            }

            player.getRoomEntity().interactItem();
        }
    }
}
