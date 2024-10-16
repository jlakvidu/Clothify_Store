package service.custom;

import dto.User;
import service.SuperService;

public interface UserService extends SuperService {
    boolean registerUser(String firstName, String lastName,String emailAddress,String password,String confirmPassword);
    User loginUser(String email, String password);
    User findUserByEmail(String email);
    boolean sendOTPEmail(String email, String otp);
    void storeOTP(String email, String otp);
    boolean validateOTP(String email, String otp);
    void resetPassword(String email, String newPassword);
}
