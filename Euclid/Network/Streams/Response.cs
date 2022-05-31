using DotNetty.Buffers;
using Euclid.Util;
using Euclid.Util.Specialised;
using System;

namespace Euclid.Network.Streams
{
    public class Response
    {
        #region Properties

        /// <summary>
        /// Get the message header
        /// </summary>
        public string Header { get; private set; }

        /// <summary>
        /// Get the buffer
        /// </summary>
        public IByteBuffer Buffer { get; }

        /// <summary>
        /// Get the message body with characters replaced
        /// </summary>
        public string MessageBody
        {
            get
            {
                string consoleText = Buffer.ToString(StringUtil.GetEncoding());

                for (int i = 0; i < 14; i++)
                    consoleText = consoleText.Replace(Convert.ToString((char)i), "[" + i + "]");

                return consoleText;
            }
        }

        #endregion

        #region Constructors

        /// <summary>
        /// Constructor for response
        /// </summary>
        /// <param name="header"></param>
        /// <param name="buffer"></param>
        public Response(string header, IByteBuffer buffer)
        {
            this.Header = header;
            this.Buffer = buffer;

            this.Buffer.WriteBytes(StringUtil.GetEncoding().GetBytes("#"));
            this.Buffer.WriteBytes(StringUtil.GetEncoding().GetBytes(" "));
            this.Buffer.WriteBytes(StringUtil.GetEncoding().GetBytes(Header));
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Write string for client
        /// </summary>
        /// <param name="obj"></param>
        public void WriteString(object obj)
        {
            Buffer.WriteBytes(StringUtil.GetEncoding().GetBytes(obj.ToString()));
            Buffer.WriteByte(2);
        }

        /// <summary>
        /// Write raw object to buffer
        /// </summary>
        public void Write(object obj)
        {
            Buffer.WriteBytes(StringUtil.GetEncoding().GetBytes(obj.ToString()));
        }

        /// <summary>
        /// Write int for client
        /// </summary>
        /// <param name="obj"></param>
        public void WriteInt(int obj)
        {
            Buffer.WriteBytes(WireEncoding.EncodeInt32(obj));
        }

        /// <summary>
        /// Write boolean for client
        /// </summary>
        /// <param name="obj"></param>
        public void WriteBool(bool obj)
        {
            WriteInt(obj ? 1 : 0);
        }

        #endregion
    }
}
