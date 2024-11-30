package vn.com.shop.services;

import vn.com.shop.dto.RegisterRequest;
import vn.com.shop.entity.Account;

public interface AccountService {
    Account registerNewAccount(RegisterRequest request);
    Account findAccountByUsername(String username);
}
