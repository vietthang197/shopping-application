package vn.com.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.shop.entity.Account;
import vn.com.shop.entity.Role;
import vn.com.shop.repository.AccountRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        Account account = accountOptional.get();
        return User
                .withUsername(username)
                .password(account.getPassword())
                .authorities(account.getRoles().stream().map(item -> new SimpleGrantedAuthority(item.getName())).collect(Collectors.toList()))
                .accountExpired(false)
                .accountLocked(false)
                .build();
    }
}
