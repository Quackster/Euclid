using MySql.Data.MySqlClient;
using System.Collections.Generic;
using System.IO;
using System.Xml;

namespace Euclid.Util
{
    class ServerConfig
    {
        #region Fields

        private static readonly string m_ConfigFileName = "config.xml";
        private static readonly ServerConfig m_ServerConfig = new ServerConfig();

        #endregion

        #region Properties

        /// <summary>
        /// Get the singleton instance
        /// </summary>
        public static ServerConfig Instance
        {
            get
            {
                return m_ServerConfig;
            }
        }

        public Dictionary<string, string> ConfigValues { get; }

        public string ConnectionString
        {
            get
            {
                var connectionString = new MySqlConnectionStringBuilder();
                connectionString.Server = ServerConfig.Instance.GetString("mysql", "hostname");
                connectionString.Port = (uint)ServerConfig.Instance.GetInt("mysql", "port");
                connectionString.UserID = ServerConfig.Instance.GetString("mysql", "username");
                connectionString.Password = ServerConfig.Instance.GetString("mysql", "password");
                connectionString.Database = ServerConfig.Instance.GetString("mysql", "database");
                connectionString.MinimumPoolSize = (uint)ServerConfig.Instance.GetInt("mysql", "min_connections");
                connectionString.MaximumPoolSize = (uint)ServerConfig.Instance.GetInt("mysql", "max_connections");
                return connectionString.ToString();
            }
        }

        #endregion

        #region Constructors

        /// <summary>
        /// Attempt to read configuration file
        /// </summary>
        public ServerConfig()
        {
            if (ConfigValues == null)
                ConfigValues = new Dictionary<string, string>();

            if (!File.Exists(m_ConfigFileName))
            {
                WriteConfig();
            }

            ConfigValues.Clear();

            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(m_ConfigFileName);

            SetConfig(xmlDoc, "mysql/hostname");
            SetConfig(xmlDoc, "mysql/username");
            SetConfig(xmlDoc, "mysql/password");
            SetConfig(xmlDoc, "mysql/database");
            SetConfig(xmlDoc, "mysql/port");
            SetConfig(xmlDoc, "mysql/min_connections");
            SetConfig(xmlDoc, "mysql/max_connections");
            SetConfig(xmlDoc, "server/main/ip");
            SetConfig(xmlDoc, "server/main/port");
            SetConfig(xmlDoc, "server/private/ip");
            SetConfig(xmlDoc, "server/private/port");

            var nodes = xmlDoc.SelectNodes("//configuration/server/public/room");

            for (int i = 0; i < nodes.Count; i++)
            {
                var roomId = nodes.Item(i).SelectSingleNode("room_id").InnerText;
                var ip = nodes.Item(i).SelectSingleNode("ip").InnerText;
                var port = nodes.Item(i).SelectSingleNode("port").InnerText;

                ConfigValues[$"server/public/ip/{roomId}"] = ip;
                ConfigValues[$"server/public/port/{roomId}"] = port;
            }
        }

        private void SetConfig(XmlDocument xmlDoc, string xmlPath, string overrideValue = null)
        {
            try
            {
                ConfigValues[xmlPath] = overrideValue != null ? overrideValue :  xmlDoc.SelectSingleNode("//configuration/" + xmlPath).InnerText;
            }
            catch
            {
                ConfigValues[xmlPath] = string.Empty;
            }
        }

        #endregion

        #region Private methods

        /// <summary>
        /// Attempts to write configuration file
        /// </summary>
        private void WriteConfig()
        {
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.Indent = true;
            settings.IndentChars = ("   ");
            settings.OmitXmlDeclaration = true;

            XmlWriter xmlWriter = XmlWriter.Create(m_ConfigFileName, settings);

            xmlWriter.WriteStartDocument();
            xmlWriter.WriteStartElement("configuration");
            xmlWriter.WriteStartElement("mysql");

            xmlWriter.WriteStartElement("hostname");
            xmlWriter.WriteString("localhost");
            xmlWriter.WriteEndElement();

            xmlWriter.WriteStartElement("username");
            xmlWriter.WriteString("root");
            xmlWriter.WriteEndElement();

            xmlWriter.WriteStartElement("password");
            xmlWriter.WriteString("password");
            xmlWriter.WriteEndElement();

            xmlWriter.WriteStartElement("database");
            xmlWriter.WriteString("euclid");
            xmlWriter.WriteEndElement();

            xmlWriter.WriteStartElement("port");
            xmlWriter.WriteString("3306");
            xmlWriter.WriteEndElement();

            xmlWriter.WriteStartElement("min_connections");
            xmlWriter.WriteString("5");
            xmlWriter.WriteEndElement();

            xmlWriter.WriteStartElement("max_connections");
            xmlWriter.WriteString("10");
            xmlWriter.WriteEndElement();

            xmlWriter.WriteEndElement();
            xmlWriter.WriteStartElement("server");

            xmlWriter.WriteStartElement("main");
            xmlWriter.WriteStartElement("ip");
            xmlWriter.WriteString("127.0.0.1");
            xmlWriter.WriteEndElement();
            xmlWriter.WriteStartElement("port");
            xmlWriter.WriteString("37120");
            xmlWriter.WriteEndElement();
            xmlWriter.WriteEndElement();

            xmlWriter.WriteStartElement("private");
            xmlWriter.WriteStartElement("ip");
            xmlWriter.WriteString("127.0.0.1");
            xmlWriter.WriteEndElement();
            xmlWriter.WriteStartElement("port");
            xmlWriter.WriteString("25006");
            xmlWriter.WriteEndElement();
            xmlWriter.WriteEndElement();

            var dict = new Dictionary<int, int>();
            dict[1] = 22009;

            xmlWriter.WriteStartElement("public");

            foreach (var kvp in dict)
            {
                xmlWriter.WriteStartElement("room");
                xmlWriter.WriteStartElement("ip");
                xmlWriter.WriteString("127.0.0.1");
                xmlWriter.WriteEndElement();
                xmlWriter.WriteStartElement("port");
                xmlWriter.WriteString(kvp.Value.ToString());
                xmlWriter.WriteEndElement();
                xmlWriter.WriteStartElement("room_id");
                xmlWriter.WriteString(kvp.Key.ToString());
                xmlWriter.WriteEndElement();
                xmlWriter.WriteEndElement();
            }

            xmlWriter.WriteEndElement();
            xmlWriter.WriteEndElement();

            xmlWriter.WriteEndDocument();
            xmlWriter.Close();
        }

        #endregion

        #region Public methods

        /// <summary>
        /// Get string by key
        /// </summary>
        /// <param name="key">the key</param>
        /// <returns>the string</returns>
        public string GetString(params string[] values)
        {
            return ConfigValues.GetValueOrDefault(string.Join("/", values));
        }

        /// <summary>
        /// Get integer by key
        /// </summary>
        /// <param name="key">the key</param>
        /// <returns>the integer</returns>
        public int GetInt(params string[] values)
        {
            int number = 0;
            int.TryParse(ConfigValues.GetValueOrDefault(string.Join("/", values)), out number);
            return number;
        }

        #endregion
    }
}
