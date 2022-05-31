using Euclid.Util.Extensions;
using System;
using System.Collections.Generic;

namespace Euclid.Network.Streams
{
    public class Request
    {

        /// <summary>
        /// Get the message header
        /// </summary>
        public string Header
        {
            get; private set;
        }

        /// <summary>
        /// Get the message buffer
        /// </summary>
        public string Content
        {
            get; private set;
        }

        /// <summary>
        /// Get the message body with characters replaced
        /// </summary>
        public string MessageBody
        {
            get
            {
                string consoleText = Content;

                for (int i = 0; i < 14; i++)
                {
                    consoleText = consoleText.Replace(Convert.ToString((char)i), "[" + i + "]");
                }

                return consoleText;
            }
        }

        #region Constructors

        public Request(string header, string request)
        {
            this.Header = header;
            this.Content = request;
        }

        #endregion

        #region Methods

        /// <summary>
        /// Skip some of the content
        /// </summary>
        /// <param name="v"></param>
        public void Skip(int num)
        {
            if (Content.Length < num)
                return;

            Content = Content.Substring(num);
        }

        /// <summary>
        /// Packet handle to get count of arguments by delimeter
        /// </summary>
        public int GetArgumentAmount(string delimeter = " ")
        {
            if (this.Content.Length == 0) return 0;
            return this.Content.Split(delimeter).Length;
        }

        /// <summary>
        /// Packet handle to get specific argument
        /// </summary>
        public string GetArgument(int index, string delimeter = " ")
        {
            if (index < 0)
                index = 0;

            if (GetArgumentAmount(delimeter) < 1)
            {
                return null;
            }

            return this.Content.Split(delimeter)[index];
        }

        /// <summary>
        /// Get key/values separated by '=' 
        /// </summary>
        public Dictionary<string, string> GetKeyValues(string content = null)
        {
            var values = new Dictionary<string, string>();

            if (content == null)
                content = this.Content;

            foreach (var line in content.Split((char)13))
            {
                if (line.Trim().Length < 1)
                {
                    continue;
                }

                if (!line.Contains('='))
                {
                    continue;
                }

                int offset = line.IndexOf('=');
                values[line.Substring(0, offset)] = line.Substring(offset + 1).FilterInput();
            }

            return values;
        }

        #endregion

        /// <summary>
        /// Dispose handler of request
        /// </summary>
        public void Dispose()
        {

        }
    }
}
