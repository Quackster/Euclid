/**
 * Habbo Hotel V1 RC4 class by Quackster (Alex)
 * Written in December 2021
 */

namespace Euclid.Util.Encryption
{
    public class SecretKey
    {
        /// <summary>
        /// Decode the secret sent from client
        /// </summary>
        public static int SecretDecode(string key)
        {
            string table = key.Substring(0, key.Length / 2); ;
            string tempKey = key.Substring(key.Length / 2);

            int checkSum = 0;
            int i = 0;

            while (i < tempKey.Length)
            {
                var a = table.IndexOf(tempKey[i]);

                if (a % 2 == 0)
                {
                    a *= 2;
                }

                if (i % 3 == 0)
                {
                    a *= 3;
                }

                if (a < 0)
                {
                    a = (tempKey.Length % 2);
                }

                checkSum += a;
                i++;
            }

            return checkSum;
        }
    }
}