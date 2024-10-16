package repository.custom;

public interface OTPDao {
    void saveOTP(String email, String otp);
    String getOTP(String email);
    void deleteOTP(String email);
}
