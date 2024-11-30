package vn.com.shop.mapper;

import vn.com.shop.dto.CustomerDto;
import vn.com.shop.entity.Customer;

public class CustomerMapper {
    public static CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .account(AccountMapper.toDto(customer.getAccount()))
                .build();
    }
}
