using Euclid.Game;
using Euclid.Network.Streams.Util;
using System.Collections.Generic;

namespace Euclid.Messages.Outgoing
{
    class STRIPINFO : IMessageComposer
    {
        private int stripSlotId;
        private int totalItems;
        private List<Item> items;

        public STRIPINFO(int stripSlotId, int totalItems, List<Item> items)
        {
            this.stripSlotId = stripSlotId;
            this.totalItems = totalItems;
            this.items = items;
        }

        public override void Write()
        {
            foreach (var item in items)
            {
                Data.Add(new ArgumentEntry("SI"));
                Data.Add(new DelimeterEntry(item.Data.Id, ";"));
                Data.Add(new DelimeterEntry(stripSlotId, ";"));

                if (item.Definition.Data.IsFloorItem)
                {
                    Data.Add(new DelimeterEntry("S", ";"));
                }
                
                if (item.Definition.Data.IsWallItem)
                {
                    Data.Add(new DelimeterEntry("I", ";"));
                }

                Data.Add(new DelimeterEntry(item.Data.Id, ";"));
                Data.Add(new DelimeterEntry(item.Definition.Data.Sprite, ";"));
                Data.Add(new DelimeterEntry(item.Definition.Data.Name, ";"));

                if (item.Definition.Data.IsFloorItem)
                {
                    Data.Add(new DelimeterEntry(item.Data.CustomData, ";"));
                    Data.Add(new DelimeterEntry(item.Definition.Data.Length, ";"));
                    Data.Add(new DelimeterEntry(item.Definition.Data.Width, ";"));
                    Data.Add(new DelimeterEntry(item.Definition.Data.Colour, ";"));
                }

                if (item.Definition.Data.IsWallItem)
                {
                    Data.Add(new DelimeterEntry(item.Data.CustomData, ";"));
                    Data.Add(new DelimeterEntry(item.Definition.Data.Name, ";"));
                }

                Data.Add(new DelimeterEntry(string.Empty, "/"));
                stripSlotId++;
            }

            Data.Add(new ArgumentEntry(totalItems));
        }
    }
}
