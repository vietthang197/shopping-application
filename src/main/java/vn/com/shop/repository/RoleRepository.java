package vn.com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.shop.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}