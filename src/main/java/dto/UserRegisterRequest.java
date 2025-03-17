package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegisterRequest {
    // @NotBlank 表示不可為空白字串或是空字串
    @NotBlank
    @Email // @Email 只有當前端回傳的事email的格式，才會通過spring boot的程式驗證
    private String email;
    @NotBlank
    private String password;

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
}
