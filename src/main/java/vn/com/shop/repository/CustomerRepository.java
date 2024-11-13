package vn.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}