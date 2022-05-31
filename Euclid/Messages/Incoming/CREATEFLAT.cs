using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;
using System.Linq;

namespace Euclid.Messages.Incoming
{
    class CREATEFLAT : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            string name = request.GetArgument(2, "/");
            string model = request.GetArgument(3, "/");

            var roomModel = RoomManager.Instance.RoomModels.FirstOrDefault(x => x.Data.Model == model);

            if (roomModel == null)
                return;

            string modelType = roomModel.Data.Model.Replace("model_", "");

            if (modelType != "a" &&
                    modelType != "b" &&
                    modelType != "c" &&
                    modelType != "d" &&
                    modelType != "e" &&
                    modelType != "f")
            {
                return; // Fuck off, scripter.
            }

            RoomData roomData = new RoomData
            {
                OwnerId = player.Details.Id,
                Name = name,
                ModelId = roomModel.Data.Id,
                Description = string.Empty
            };

            RoomDao.NewRoom(roomData);

            player.Send(new FLATCREATED(roomData));
        }
    }
}
