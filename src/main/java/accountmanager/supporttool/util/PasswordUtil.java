package accountmanager.supporttool.util;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;

public class PasswordUtil {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*()_+-=[]|,./?><";
    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword() {
        //TODO : CONFIGURE PASSWORD LENGTH
        int length = 12;
        if (length < 12) {
            throw new IllegalArgumentException("The password length must be at least 12 characters.");
        }

        StringBuilder password = new StringBuilder(length);

        // Randomly choose characters from allowed characters
        for (int i = 0; i < length - 3; i++) {
            int index = random.nextInt(PASSWORD_ALLOW_BASE.length());
            password.append(PASSWORD_ALLOW_BASE.charAt(index));
        }

        // Add at least one lowercase character
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));

        // Add at least one uppercase character
        password.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));

        // Add at least one digit
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));

        // Add at least one special character
        password.append(OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        // Shuffle the characters to make the password more random
        char[] passwordChars = password.toString().toCharArray();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(length);
            char temp = passwordChars[i];
            passwordChars[i] = passwordChars[randomIndex];
            passwordChars[randomIndex] = temp;
        }

        return new String(passwordChars);
    }
}
