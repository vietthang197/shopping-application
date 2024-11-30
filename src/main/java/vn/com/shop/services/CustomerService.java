package vn.com.shop.services;

import vn.com.shop.entity.Account;
import vn.com.shop.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findLatestByAccount(Account account);
}
