using Euclid.Game.Entity.Humanoid;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    class USERS : IMessageComposer
    {
        private List<Humanoid> entities;

        public USERS(List<Humanoid> entities)
        {
            this.entities = entities;
        }

        public override void Write()
        {
            foreach (var entity in entities)
            {
                this.Data.Add(new ArgumentEntry(entity.EntityData.Name));
                this.Data.Add(new DelimeterEntry(entity.EntityData.Figure, " "));
                this.Data.Add(new DelimeterEntry(entity.RoomEntity.Position.X, " "));
                this.Data.Add(new DelimeterEntry(entity.RoomEntity.Position.Y, " "));
                this.Data.Add(new DelimeterEntry((int)entity.RoomEntity.Position.Z, " "));
                this.Data.Add(new DelimeterEntry(entity.EntityData.CustomData, " "));
            }
        }
    }
}
