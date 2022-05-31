using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;
using Euclid.Storage.Database.Data;

namespace Euclid.Messages.Incoming
{
    class REGISTER : IMessageEvent
    {
        #region Properties

        public bool AuthenticationRequired => false;

        #endregion

        public void Handle(Player player, Request request)
        {
            var registerValues = request.GetKeyValues();

            if (!registerValues.ContainsKey("name") ||
                !registerValues.ContainsKey("password") ||
                !registerValues.ContainsKey("email") ||
                !registerValues.ContainsKey("figure") ||
                !registerValues.ContainsKey("directMail") ||
                !registerValues.ContainsKey("birthday") ||
                !registerValues.ContainsKey("phonenumber") ||
                !registerValues.ContainsKey("customData") ||
                !registerValues.ContainsKey("has_read_agreement") ||
                !registerValues.ContainsKey("sex") ||
                !registerValues.ContainsKey("country"))
            {
                return;
            }

            var data = new PlayerData
            {
                Name = registerValues["name"],
                Password = registerValues["password"],
                Email = registerValues["email"],
                Figure = registerValues["figure"],
                DirectMail = registerValues["directMail"] == "1",
                Birthday = registerValues["birthday"],
                PhoneNumber = registerValues["phonenumber"],
                CustomData = registerValues["customData"],
                Sex = registerValues["sex"] == "Male" ? "M" : "F",
                Country = registerValues["country"]
            };

            UserDao.SaveOrUpdate(data);
        }
    }
}
