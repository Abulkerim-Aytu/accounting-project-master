package com.cydeo.service;

import com.cydeo.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto findByUsername(String username);

    boolean findByUsernameCheck(String username);

    List<UserDto>getAllUsers();
    void save(UserDto user);
    void delete(Long id);
    UserDto findById(Long id);
    UserDto updateUser(UserDto dto);
}