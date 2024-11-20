package vn.com.shop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.shop.entity.Role;

public interface RoleService {
    Page<Role> getAllRoles(Pageable pageable);

    Role getRoleById(String id);

    Role createRole(Role role);

    Role updateRole(String id, Role role);

    void deleteRole(String id);
}
