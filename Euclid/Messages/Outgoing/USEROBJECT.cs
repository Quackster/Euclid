using Euclid.Network.Streams.Util;
using Euclid.Storage.Database.Data;

namespace Euclid.Messages.Outgoing
{
    class USEROBJECT : IMessageComposer
    {
        private PlayerData playerData;

        public USEROBJECT(PlayerData playerData)
        {
            this.playerData = playerData;
        }

        public override void Write()
        {
            this.Data.Add(new KeyValueEntry("name", this.playerData.Name, "="));
            this.Data.Add(new KeyValueEntry("figure", this.playerData.Figure, "="));
            this.Data.Add(new KeyValueEntry("email", this.playerData.Email, "="));
            this.Data.Add(new KeyValueEntry("birthday", this.playerData.Birthday, "="));
            this.Data.Add(new KeyValueEntry("phonenumber", this.playerData.PhoneNumber, "="));
            this.Data.Add(new KeyValueEntry("customData", this.playerData.CustomData, "="));
            this.Data.Add(new KeyValueEntry("has_read_agreement", "1", "="));
            this.Data.Add(new KeyValueEntry("sex", this.playerData.Sex, "="));
            this.Data.Add(new KeyValueEntry("country", this.playerData.Country, "="));
            this.Data.Add(new KeyValueEntry("has_special_rights", "1", "="));
            this.Data.Add(new KeyValueEntry("badge_type", "", "="));
        }
    }
}
