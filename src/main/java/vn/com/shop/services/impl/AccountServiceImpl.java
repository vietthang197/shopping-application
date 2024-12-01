package vn.com.shop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.shop.dto.PasswordChangeRequest;
import vn.com.shop.dto.ProfileUpdateRequest;
import vn.com.shop.dto.RegisterRequest;
import vn.com.shop.entity.Account;
import vn.com.shop.entity.Role;
import vn.com.shop.repository.AccountRepository;
import vn.com.shop.repository.RoleRepository;
import vn.com.shop.services.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Account registerNewAccount(RegisterRequest request) {
        if (accountRepository.findByUsername(request.getUsername().toLowerCase()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Account account = new Account();
        account.setUsername(request.getUsername().toLowerCase());
        account.setEmail(request.getEmail().toLowerCase());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        account.getRoles().add(userRole);

        return accountRepository.save(account);
    }

    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username.toLowerCase()).orElse(null);
    }

    @Override
    public void updateProfile(String username, ProfileUpdateRequest request) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        account.setEmail(request.getEmail().toLowerCase());
        accountRepository.save(account);
    }

    @Override
    public void changePassword(String username, PasswordChangeRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), account.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }
}
