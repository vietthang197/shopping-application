package vn.com.shop.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.com.shop.entity.Account;
import vn.com.shop.entity.Role;
import vn.com.shop.repository.AccountRepository;
import vn.com.shop.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Configuration
public class InitData {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // create admin account
        Optional<Account> accountOptional = accountRepository.findByUsername("admin");
        if (accountOptional.isEmpty()) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ADMIN");
            if (adminRoleOptional.isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");

                Role userRole = new Role();
                userRole.setName("USER");

                roleRepository.saveAll(List.of(adminRole, userRole));
            }


            Account account = new Account();
            account.setUsername("admin");
            account.setEmail("admin@gmail.com");
            account.setPassword(passwordEncoder.encode("admin"));

            Role accAdminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            account.getRoles().add(accAdminRole);
            accountRepository.save(account);
        }
    }
}
