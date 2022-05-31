using System;
using System.Linq;

namespace Euclid.Util.Extensions
{
    public static class StringExtensions
    {
        /// <summary>
        /// Filter harmful injectable characters
        /// </summary>
        public static string FilterInput(this string str)
        {
            str = str.Replace((char)1, ' ');
            str = str.Replace((char)2, ' ');
            str = str.Replace((char)3, ' ');
            str = str.Replace((char)9, ' ');
            str = str.Replace((char)10, ' ');
            str = str.Replace((char)13, ' ');
            str = str.Replace('#', '*');
            return str;
        }

        /// <summary>
        /// Get if string is numeric
        /// </summary>
        public static bool IsNumeric(this string str)
        {
            return int.TryParse(str, out _);
        }

        /// <summary>
        /// Convert delimetered string to int array
        /// </summary>
        public static int[] ToIntArray(this string value, char separator)
        {
            return value.Split(separator).Select(i => int.Parse(i)).ToArray();
        }

        /// <summary>
        /// Convert string to console output
        /// </summary>
        public static string ToConsoleOutput(this string value)
        {
            var consoleText = value;

            for (int i = 0; i < 13; i++)
            {
                consoleText = consoleText.Replace(Convert.ToString((char)i), "[" + i + "]");
            }

            return consoleText;
        }
    }
}
