package vn.com.shop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        account.getRoles().add(userRole);

        return accountRepository.save(account);
    }

    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElse(null);
    }
}
