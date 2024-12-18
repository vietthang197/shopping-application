package vn.com.shop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String fullName;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;
    private String address;
}
