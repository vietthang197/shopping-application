package vn.com.shop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vn.com.shop.entity.Account;
import vn.com.shop.entity.Customer;
import vn.com.shop.repository.CustomerRepository;
import vn.com.shop.services.CustomerService;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findLatestByAccount(Account account) {
        return customerRepository.findOneByAccount_IdOrderByCreatedDtDesc(account.getId(), PageRequest.ofSize(1));
    }
}
