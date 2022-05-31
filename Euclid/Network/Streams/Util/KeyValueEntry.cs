namespace Euclid.Network.Streams.Util
{
    public class KeyValueEntry
    {
        #region Private variables

        private string m_Key;
        private string m_Value;
        private string m_Delimiter;

        #endregion

        #region Fields

        public string Key
        {
            get { return m_Key; }
        }

        public string Value
        {
            get { return m_Value; }
        }

        public string Delimiter
        {
            get { return m_Delimiter; }
        }

        #endregion

        #region Constructor

        public KeyValueEntry(string key, object value, object delimiter)
        {
            m_Key = key;
            m_Value = value.ToString();
            m_Delimiter = delimiter.ToString();
        }

        #endregion
    }
}
