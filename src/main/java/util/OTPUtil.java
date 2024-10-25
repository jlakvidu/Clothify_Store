package util;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Random;

public class OTPUtil {
    public static String generateOTP() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
}
