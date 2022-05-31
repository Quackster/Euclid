using Euclid.Game;
using Euclid.Network.Streams;
using System;

namespace Euclid.Messages.Incoming
{
    class Move : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {

            if (!player.RoomEntity.WalkingAllowed)
                return;

            int x = int.Parse(request.GetArgument(0));
            int y = int.Parse(request.GetArgument(1));

            Console.WriteLine(new Position(x, y).GetTile(player.RoomEntity.Room).HighestItem != null ? new Position(x, y).GetTile(player.RoomEntity.Room).HighestItem.Definition.Data.Sprite : null);

            player.RoomEntity.Move(x, y);
        }
    }
}
