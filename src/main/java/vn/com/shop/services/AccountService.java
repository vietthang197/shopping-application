package vn.com.shop.services;

import jakarta.validation.Valid;
import vn.com.shop.dto.PasswordChangeRequest;
import vn.com.shop.dto.ProfileUpdateRequest;
import vn.com.shop.dto.RegisterRequest;
import vn.com.shop.entity.Account;

public interface AccountService {
    Account registerNewAccount(RegisterRequest request);
    Account findAccountByUsername(String username);

    void updateProfile(String username, @Valid ProfileUpdateRequest request);

    void changePassword(String username, @Valid PasswordChangeRequest request);
}
