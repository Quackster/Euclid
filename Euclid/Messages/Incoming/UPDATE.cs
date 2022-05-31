using Euclid.Game;
using Euclid.Network.Streams;
using Euclid.Storage.Database.Access;

namespace Euclid.Messages.Incoming
{
    class UPDATE : IMessageEvent
    {
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

            var data = player.Details;

            data.Name = registerValues["name"];
            data.Password = registerValues["password"];
            data.Email = registerValues["email"];
            data.Figure = registerValues["figure"];
            data.DirectMail = registerValues["directMail"] == "1";
            data.Birthday = registerValues["birthday"];
            data.PhoneNumber = registerValues["phonenumber"];
            data.CustomData = registerValues["customData"];
            data.Sex = registerValues["sex"] == "Male" ? "M" : "F";
            data.Country = registerValues["country"];

            UserDao.SaveOrUpdate(data);
        }
    }
}
