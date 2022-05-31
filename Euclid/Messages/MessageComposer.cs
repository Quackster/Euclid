using System.Collections.Generic;

namespace Euclid.Messages
{
    public abstract class IMessageComposer
    {

        /// <summary>
        /// Get the data appended
        /// </summary>
        public List<object> Data { get; set; }

        /// <summary>
        /// Get whether the packet is composed
        /// </summary>
        public bool Composed { get; set; }

        public IMessageComposer()
        {
            Data = new List<object>();
        }

        public abstract void Write();

        public object[] GetMessageArray()
        {
            return Data.ToArray();
        }
    }
}