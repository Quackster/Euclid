namespace Euclid.Network.Streams.Util
{
    public class ArgumentEntry
    {
        #region Private variables

        private string m_Value;

        #endregion

        #region Fields

        public string Value
        {
            get { return m_Value; }
        }

        #endregion

        #region Constructor

        public ArgumentEntry(object value = null)
        {
            m_Value = (value != null ? value.ToString() : string.Empty);
        }

        #endregion
    }
}
