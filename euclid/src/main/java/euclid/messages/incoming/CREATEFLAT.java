package euclid.messages.incoming;

import euclid.game.Player;
import euclid.game.room.RoomManager;
import euclid.game.room.RoomModel;
import euclid.messages.IMessageEvent;
import euclid.messages.outgoing.FLATCREATED;
import euclid.network.streams.Request;
import euclid.storage.database.access.RoomDao;
import euclid.storage.database.data.RoomData;

public class CREATEFLAT implements IMessageEvent {

    @Override
    public void handle(Player player, Request request) {
        String name = request.getArgument(2, "/");
        String model = request.getArgument(3, "/");

        RoomModel roomModel = RoomManager.getInstance().getRoomModels().stream()
                .filter(x -> x.getData().getModel().equals(model))
                .findFirst()
                .orElse(null);

        if (roomModel == null) {
            return;
        }

        String modelType = roomModel.getData().getModel().replace("model_", "");

        if (!modelType.equals("a") &&
                !modelType.equals("b") &&
                !modelType.equals("c") &&
                !modelType.equals("d") &&
                !modelType.equals("e") &&
                !modelType.equals("f")) {
            return;
        }

        RoomData roomData = new RoomData();
        roomData.setOwnerId(player.getDetails().getId());
        roomData.setName(name);
        roomData.setModelId(roomModel.getData().getId());
        roomData.setDescription("");

        RoomDao.newRoom(roomData);

        player.send(new FLATCREATED(roomData));
    }
}
