using Euclid.Game;
using Euclid.Game.Entity.Humanoid;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;
using System.Linq;

namespace Euclid.Messages.Outgoing
{
    class STATUS : IMessageComposer
    {
        public EntityState[] entities;

        public STATUS(ICollection<Humanoid> values)
        {
            this.entities = values.Select(x => new
                EntityState(
                x.EntityData.Id,
                x.RoomEntity.InstanceId,
                x.EntityData,
                x.EntityType,
                x.RoomEntity.Room,
                x.RoomEntity.Position.Copy(),
                x.RoomEntity.Status)
             ).ToArray();
        }

        public override void Write()
        {
            foreach (var entity in this.entities)
            {
                Data.Add(new ArgumentEntry(entity.Details.Name));
                Data.Add(new DelimeterEntry(entity.Position.X, ' '));
                Data.Add(new DelimeterEntry(entity.Position.Y, ','));
                Data.Add(new DelimeterEntry((int)entity.Position.Z, ','));
                Data.Add(new DelimeterEntry(entity.Position.HeadRotation, ','));
                Data.Add(new DelimeterEntry(entity.Position.BodyRotation, ','));
                Data.Add("/");

                foreach (var kvp in entity.Statuses)
                {
                    Data.Add(kvp.Key);

                    if (kvp.Value.Value.Length > 0)
                    {
                        Data.Add(" ");
                        Data.Add(kvp.Value.Value);
                    }

                    Data.Add("/");
                }
            }
        }
    }
}
