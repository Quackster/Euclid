namespace Euclid.Network.Streams.Util
{
    public class DelimeterEntry
    {
        #region Private variables

        private string m_Value;
        private string m_Delimiter;

        #endregion

        #region Fields

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

        public DelimeterEntry(object value, object delimeter)
        {
            m_Value = (value == null ? string.Empty : value).ToString();
            m_Delimiter = (delimeter == null ? string.Empty : delimeter).ToString();
        }

        #endregion
    }
}
