package vn.com.shop.mapper;

import vn.com.shop.dto.RoleDto;
import vn.com.shop.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    public static RoleDto toDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Set<RoleDto> toSetDto(Set<Role> roles) {
        return roles.stream().map(RoleMapper::toDto).collect(Collectors.toSet());
    }
}
