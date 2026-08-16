package euclid.messages.outgoing;

import euclid.game.entity.Humanoid;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

import java.util.List;

public class USERS extends IMessageComposer {

    private final List<Humanoid> entities;

    public USERS(List<Humanoid> entities) {
        this.entities = entities;
    }

    @Override
    public void write() {
        for (Humanoid entity : entities) {
            getData().add(new ArgumentEntry(entity.getEntityData().getName()));
            getData().add(new DelimeterEntry(entity.getEntityData().getFigure(), " "));
            getData().add(new DelimeterEntry(entity.getRoomEntity().getPosition().getX(), " "));
            getData().add(new DelimeterEntry(entity.getRoomEntity().getPosition().getY(), " "));
            getData().add(new DelimeterEntry((int) entity.getRoomEntity().getPosition().getZ(), " "));
            getData().add(new DelimeterEntry(entity.getEntityData().getCustomData(), " "));
        }
    }
}
