package vn.com.shop.mapper;

import vn.com.shop.dto.AccountDto;
import vn.com.shop.entity.Account;

public class AccountMapper {
    public static AccountDto toDto(Account account) {
        if (account == null) {
            return null;
        }
        return AccountDto.builder()
                .id(account.getId())
                .email(account.getEmail())
                .username(account.getUsername())
                .roles(RoleMapper.toSetDto(account.getRoles()))
                .build();
    }
}
