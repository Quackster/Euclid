using Euclid.Game;
using Euclid.Messages.Headers;
using Euclid.Network.Streams;
using log4net;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;

namespace Euclid.Messages
{
    public class MessageHandler : ILoadable
    {
        #region Fields

        private static readonly ILog log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static readonly MessageHandler Instance = new MessageHandler();

        #endregion

        #region Properties

        private Dictionary<string, IMessageEvent> Events { get; }
        private Dictionary<string, string> Composers { get; }


        #endregion

        #region Constructors

        public MessageHandler()
        {
            Events = new Dictionary<string, IMessageEvent>();
            Composers = new Dictionary<string, string>();
        }

        public void Load()
        {
            ResolveMessages();
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Resolve events, instead of assigning to every event file, associate by file name instead
        /// </summary>
        public void ResolveMessages()
        {
            Type incomingEventType = typeof(IncomingEvents);
            Type outgoingEventType = typeof(OutgoingEvents);

            var types = AppDomain.CurrentDomain.GetAssemblies()
                .SelectMany(s => s.GetTypes())
                .Where(p => 
                (typeof(IMessageEvent).IsAssignableFrom(p) && p != typeof(IMessageEvent)) || 
                (typeof(IMessageComposer).IsAssignableFrom(p) && p != typeof(IMessageComposer)));

            foreach (var packetType in types)
            {
                if (typeof(IMessageEvent).IsAssignableFrom(packetType)) {
                    var incomingField = incomingEventType.GetField(packetType.Name);

                    if (incomingField != null)
                    {
                        string header = incomingField.GetValue(null).ToString();
                        Events[header] = (IMessageEvent)Activator.CreateInstance(packetType);
                    }

                    else
                        log.Error($"Event {packetType.Name} has no header defined");
                }

                if (typeof(IMessageComposer).IsAssignableFrom(packetType)) {
                    var composerField = outgoingEventType.GetField(packetType.Name);

                    if (composerField != null)
                        Composers[packetType.Name] = composerField.GetValue(null).ToString();
                    else
                        log.Error($"Composer {packetType.Name} has no header defined");
                }
                /**/
            }
        }

        /// <summary>
        /// Get composer id for type
        /// </summary>
        internal string GetComposerId(IMessageComposer composer)
        {
            string header;

            if (Composers.TryGetValue(composer.GetType().Name, out header))
                return header;

            return null;
        }

        /// <summary>
        /// Handler for incoming message
        /// </summary>
        /// <param name="player"></param>
        /// <param name="request"></param>
        public void HandleMesage(Player player, Request request)
        {
            try
            {
                if (Events.ContainsKey(request.Header))
                {
                    var messageEvent = Events [request.Header];

                    if (messageEvent.AuthenticationRequired &&
                        !player.Authenticated)
                    {
                        player.Log.Debug($"User attempted request without authentication: [ {request.Header} ] / {request.MessageBody}");
                        return;
                    }

                    player.Log.Debug($"RECEIVED {messageEvent.GetType().Name}: [ {request.Header} ] / {request.MessageBody}");
                    messageEvent.Handle(player, request);
                } 
                else
                {
                    player.Log.Debug($"Unknown: [ {request.Header} ] / {request.MessageBody}");
                }
            }
            catch (Exception ex)
            {
                log.Error("Error occurred: ", ex);
            }
        }

        #endregion
    }
}
