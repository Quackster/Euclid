using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Util.Extensions;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Messages.Incoming
{
    class WHISPER : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            if (!player.Authenticated)
            {
                return;
            }

            if (player.RoomEntity.Room == null)
            {
                return;
            }

            string name = request.GetArgument(0, " ");
            string message = request.Content.Substring(name.Length + 1).FilterInput();

            Player target = name.Length > 0 ? player.RoomEntity.Room.EntityManager.GetEntities<Player>().FirstOrDefault(x => x.EntityData.Name == name) : player;

            if (target == null)
            {
                return;
            }

            player.RoomEntity.Talk(ChatMessageType.WHISPER, message, List.Create(target, player));
        }
    }
}
