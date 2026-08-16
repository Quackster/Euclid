package euclid.messages.outgoing;

import euclid.game.entity.EntityState;
import euclid.game.entity.Humanoid;
import euclid.messages.IMessageComposer;
import euclid.network.streams.util.ArgumentEntry;
import euclid.network.streams.util.DelimeterEntry;

import java.util.Collection;

public class STATUS extends IMessageComposer {

    private final EntityState[] entities;

    public STATUS(Collection<Humanoid> values) {
        this.entities = values.stream()
                .map(x -> new EntityState(
                        x.getEntityData().getId(),
                        x.getRoomEntity().getInstanceId(),
                        x.getEntityData(),
                        x.getEntityType(),
                        x.getRoomEntity().getRoom(),
                        x.getRoomEntity().getPosition().copy(),
                        x.getRoomEntity().getStatuses()
                ))
                .toArray(EntityState[]::new);
    }

    @Override
    public void write() {
        for (EntityState entity : entities) {
            getData().add(new ArgumentEntry(entity.getDetails().getName()));
            getData().add(new DelimeterEntry(entity.getPosition().getX(), " "));
            getData().add(new DelimeterEntry(entity.getPosition().getY(), ","));
            getData().add(new DelimeterEntry((int) entity.getPosition().getZ(), ","));
            getData().add(new DelimeterEntry(entity.getPosition().getHeadRotation(), ","));
            getData().add(new DelimeterEntry(entity.getPosition().getBodyRotation(), ","));
            getData().add("/");

            entity.getStatuses().forEach((key, status) -> {
                getData().add(key);
                if (status.getValue().length() > 0) {
                    getData().add(" ");
                    getData().add(status.getValue());
                }
                getData().add("/");
            });
        }
    }
}
