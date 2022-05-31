using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;

namespace Euclid.Messages.Incoming
{
    class MOVESTUFF : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            int itemId = int.Parse(request.GetArgument(0));

            if (player.RoomEntity.Room == null)
                return;

            Room room = player.RoomEntity.Room;

            if (room == null)
                return;

            Item item = room.ItemManager.GetItem(itemId);

            if (item == null || item.Data.OwnerId != player.Details.Id) // TODO: Staff check
                return;


            int x = int.Parse(request.GetArgument(1));
            int y = int.Parse(request.GetArgument(2));
            int rotation = int.Parse(request.GetArgument(3));

            var oldPosition = item.Position.Copy();

            if ((oldPosition.X == x &&
                oldPosition.Y == y &&
                oldPosition.Rotation == rotation) || !item.IsValidMove(item, room, x, y, rotation))
            {
                if (new Position(x, y) != item.Position)
                {
                    player.Send(new ACTIVEOBJECT_UPDATE(item));
                    return;
                }
            }

            if (rotation % 2 != 0 || rotation < 0 || rotation > 6)
                rotation = 0;

            room.FurnitureManager.MoveItem(item, new Position
            {
                X = x,
                Y = y,
                Rotation = rotation
            });
        }
    }
}
