/**
 * Habbo Hotel V1 RC4 class by Quackster (Alex)
 * Written in December 2021
 */

using System;
using System.Globalization;

namespace Euclid.Util.Encryption
{
    public class RC4
    {
        private int i;
        private int k;
        private int j;

        private int[] sbox;
        private int[] skey;

        /// <summary>
        /// Create key, not used, but ported from Habbo V1 RC4 class written in Lingo
        /// </summary>
        public int CreateKey()
        {
            var k = "";
            var random = new Random();
            var i = 0;

            while (i < 4)
            {
                k += int2hex(random.Next(256) - 1);
                i++;
            }

            return Math.Abs(hex2int(k));
        }

        /// <summary>
        /// Initialise the encryption class
        /// </summary>
        /// <param name="myKey"></param>
        public void SetKey(int myKey)
        {
            // Console.WriteLine("New key assigned to RC4: " + myKey);
            
            skey = new int[256];
            sbox = new int[256];

            i = 0;
            k = 0;

            var tempKey = Convert.ToString(myKey);

            while (i <= 255)
            {
                skey[i] = (int)tempKey[i % tempKey.Length];
                sbox[i] = i;
                i = 1 + i;
            }

            i = 0;
            j = 0;

            while (i <= 255)
            {
                j = (j + sbox[i] + skey[i]) % 256;
                k = sbox[i];
                sbox[i] = sbox[j];
                sbox[j] = k;
                i = 1 + i;
            }


            i = 0;
            j = 0;

            //Print(sbox);
            //Print(skey);
        }

        /// <summary>
        /// Recreate the enciphering performed on the client
        /// </summary>
        public string Encipher(string data)
        {
            string cipher = string.Empty;
            int a = 0;

            while (a < data.Length)
            {
                cipher += int2hex(data[a] ^ shift());
                a++;
            }

            return cipher;
        }

        /// <summary>
        /// Decipher incoming packets from client
        /// </summary>
        public string Decipher(string data)
        {
            string cipher = string.Empty;
            int a = 0;

            while (a < data.Length)
            {
                var t = hex2int(data.Substring(a, 2));
                cipher += (char)(t ^ shift());
                a += 2;
            }

            return cipher;
        }

        /// <summary>
        /// Shift RC4 tables
        /// </summary>
        private int shift()
        {
            i = (i + 1) % 256;
            j = (j + sbox[i]) % 256;
            var temp = sbox[i];
            sbox[i] = sbox[j];
            sbox[j] = temp;
            return sbox[(sbox[i] + sbox[j]) % 256];
        }

        /// <summary>
        /// int2hex ported from Lingo
        /// </summary>
        public static string int2hex(int aint)
        {
            var digits = "0123456789ABCDEF";
            var hexstr = "";

            if (aint <= 0)
                hexstr = "00";
            else
            {
                while (aint > 0)
                {
                    var sd = (aint % 16);
                    aint /= 16;
                    hexstr = digits[sd] + hexstr;
                }
            }

            if ((hexstr.Length % 2) == 1)
                hexstr = "0" + hexstr;

            return hexstr;
        }

        /// <summary>
        /// hex2int method used for deciphering
        /// </summary>
        public static int hex2int(string ahex)
        {
            return int.Parse(ahex, NumberStyles.HexNumber);
        }
        
        /// <summary>
        /// Used for debugging tables
        /// </summary>
        private void Print(int[] array)
        {
            foreach (int i in array)
                Console.Write(i + ", ");

            Console.WriteLine();
        }
    }
}