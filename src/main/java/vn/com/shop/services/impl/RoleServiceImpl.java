package vn.com.shop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.shop.entity.Role;
import vn.com.shop.repository.RoleRepository;
import vn.com.shop.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<Role> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role name already exists");
        }
        return roleRepository.save(role);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role updateRole(String id, Role role) {
        Role existingRole = getRoleById(id);
        if (!existingRole.getName().equals(role.getName()) &&
                roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role name already exists");
        }
        existingRole.setName(role.getName());
        return roleRepository.save(existingRole);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }
}
