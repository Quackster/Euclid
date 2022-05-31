using System;

namespace Euclid.Util
{
    public class RegisterUtil
    {
        public static bool IsValidName(String name)
        {
            // FAILproof!
            if (name != null)
            {

                // Atleast 3 characters and not more than 20?
                if (name.Length >= 3 && name.Length <= 20)
                {
                    // Does username start with MOD- ?
                    if (name.IndexOf("MOD-") != 0)
                    {

                        // We don't want m0d neither...
                        if (name.IndexOf("M0D-") != 0)
                        {
                            // Check for characters
                            string allowed = "1234567890qwertyuiopasdfghjklzxcvbnm-=?!@:.,";

                            if (allowed.Equals("*"))
                            {
                                // Any name can pass!
                                return true;
                            }
                            else
                            {

                                // Check each character in the name
                                char[] nameChars = name.ToCharArray();

                                for (int i = 0; i < nameChars.Length; i++)
                                {

                                    // Is this character allowed?
                                    if (!allowed.Contains(char.ToLower(nameChars[i])))
                                    {
                                        // Not allowed
                                        return false;
                                    }
                                }

                                // Passed all checks!
                                return true;
                            }
                        }
                    }
                }
            }

            // Bad for whatever reason!
            return false;
        }
    }
}
