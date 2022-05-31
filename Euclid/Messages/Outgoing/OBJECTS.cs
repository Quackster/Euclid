using Euclid.Game;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    class OBJECTS : IMessageComposer
    {
        private RoomModel model;
        private List<PassiveItem> passiveItems;

        public OBJECTS(RoomModel model, List<PassiveItem> passiveItems)
        {
            this.model = model;
            this.passiveItems = passiveItems;
        }

        public override void Write()
        {
            this.Data.Add(new DelimeterEntry("WORLD", " "));
            this.Data.Add(new DelimeterEntry("0", " "));
            this.Data.Add(new DelimeterEntry(model.Data.Model, " "));

            foreach (var item in passiveItems)
            {
                this.Data.Add(new ArgumentEntry(item.Data.CustomData));
                this.Data.Add(new DelimeterEntry(item.Definition.Data.Sprite, " "));
                this.Data.Add(new DelimeterEntry(item.Position.X, " "));
                this.Data.Add(new DelimeterEntry(item.Position.Y, " "));
                this.Data.Add(new DelimeterEntry(item.Position.Z, " "));

                if (item.Definition.Data.Length > 1 ||
                    item.Definition.Data.Width > 1)
                {
                    this.Data.Add(new DelimeterEntry(item.Definition.Data.Length, " "));
                    this.Data.Add(new DelimeterEntry(item.Definition.Data.Width, " "));
                }
                else
                {
                    this.Data.Add(new DelimeterEntry(item.Position.Rotation, " "));
                }
            }
        }
    }
}
