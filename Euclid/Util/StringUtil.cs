using System.Text;

namespace Euclid.Util
{
    public class StringUtil
    {
        #region Public methods

        public static Encoding GetEncoding()
        {
            return Encoding.GetEncoding("ISO-8859-1");
        }

        public static bool HasWhitelistedCharacters(string str, string allowedChars)
        {
            if (str == null)
            {
                return false;
            }

            for (int i = 0; i < str.Length; i++)
            {
                if (allowedChars.Contains(str[i].ToString()))
                {
                    continue;
                }

                return false;
            }

            return true;
        }

        #endregion
    }
}
