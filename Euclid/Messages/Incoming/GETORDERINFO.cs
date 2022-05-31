using Euclid.Game;
using Euclid.Messages.Outgoing;
using Euclid.Network.Streams;
using System.Linq;

namespace Euclid.Messages.Incoming
{
    class GETORDERINFO : IMessageEvent
    {
        public void Handle(Player player, Request request)
        {
            if (!player.Authenticated)
                return;

            request.Skip(1);

            string section = request.GetArgument(0);
            string id = request.GetArgument(1);
            string extra = request.GetArgument(2);

            CatalogueItem item = CatalogueManager.Instance.Offers.Where(x => x.Data.Id == id).FirstOrDefault();

            if (item == null || item.Data.Section != section)
            {
                if (id == "deal")
                {
                    //m_session.getServer().getLogger().info("Catalogue", "Article Page: " + section + " arcticleID: " + articleID + " " + extra);
                }
                else
                {
                    //m_session.getServer().getLogger().info("Catalogue", "Article Page: " + section + " arcticleID: " + articleID);
                }
                //m_session.systemMsg("Invalid catalogue article!");
            }
            else
            {
                var customData = string.Empty;

                if (item.Definition.Data.IsDecoration || item.Definition.Data.Sprite == "poster")
                    customData = extra;

                CatalogueManager.Instance.OrderHistory[player.Details.Id] = new CatalogueOrder(item, customData);
                player.Send(new ORDERINFO(item.Data.Id, item.Data.Price, customData, item.Definition.Data.Name));
            }
        }
    }
}

