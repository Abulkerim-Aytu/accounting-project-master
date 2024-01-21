package com.cydeo.service.impl;

import com.cydeo.dto.RoleDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Role;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;


    public RoleServiceImpl(RoleRepository roleRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public RoleDto findById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow();
        return mapperUtil.convert(role, new RoleDto());
    }

    public List<RoleDto> getAllRoles() {
        List<Role> roleList = roleRepository.findAll();
        return roleList.stream().map(role -> mapperUtil.convert(role, new RoleDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDto> getAllRolesForCurrentUser() {
        UserDto user=securityService.getLoggedInUser();
        if(user.getRole().getId()!=1){
            List<Role>roleList=roleRepository.getAllRoleForAdmin(user.getRole().getDescription());
            return roleList.stream().map(role1 -> mapperUtil.convert(role1, new RoleDto()))
                    .collect(Collectors.toList());
        }
        else{
            Role role=roleRepository.getAllRoleForRoot(user.getRole().getDescription());
            return Collections.singletonList(mapperUtil.convert(role, new RoleDto()));
        }
    }


}