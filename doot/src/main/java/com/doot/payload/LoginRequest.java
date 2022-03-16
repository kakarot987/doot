package  com.doot.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

    private Long phoneNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
