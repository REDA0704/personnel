package util;

import org.mindrot.jbcrypt.BCrypt;

public class Password {
    // Hacher un mot de passe
    public static String hash(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Vérifier un mot de passe
    public static boolean verify(String password, String hashedPassword)
    {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
