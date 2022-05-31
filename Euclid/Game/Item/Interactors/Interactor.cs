using System;
using System.Collections.Generic;

namespace Euclid.Game
{
    public class QueuedEvent
    {
        #region Properties

        public string EventName { get; set; }
        public long TicksTimer { get; set; } 
        public Action<QueuedEvent> Action { get; set; }
        private Dictionary<object, object> Attributes { get; set; }

        #endregion

        #region Constructor

        public QueuedEvent(string eventName, Action<QueuedEvent> action, long ticksTimer, Dictionary<object, object> attributes)
        {
            EventName = eventName;
            Action = action;
            TicksTimer = ticksTimer;
            Attributes = attributes;
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Has an attribute
        /// </summary>
        public bool HasAttribute(object key)
        {
            return Attributes.ContainsKey(key);
        }

        /// <summary>
        /// Get attribute by class it expects
        /// </summary>
        public T GetAttribute<T>(object key)
        {
            if (Attributes.ContainsKey(key))
            {
                return (T)Attributes[key];
            }

            return default(T);
        }

        #endregion
    }
}
