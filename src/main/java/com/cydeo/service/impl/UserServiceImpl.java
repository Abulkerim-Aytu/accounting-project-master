package com.cydeo.service.impl;

import com.cydeo.dto.UserDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.User;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, PasswordEncoder passwordEncoder, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public boolean findByUsernameCheck(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User LoggedInUser = userRepository.findByUsername(auth.getName());

        if (LoggedInUser.getId() != 1) {
            Company company = companyRepository.findById(LoggedInUser.getCompany().getId()).orElseThrow();

            List<User> userList = userRepository.findAllUserWithCompanyAndIsDeleted(company, false);
            return userList.stream().map(user -> mapperUtil.convert(user, new UserDto())).
                    collect(Collectors.toList());
        } else {
            List<User> userList = userRepository.findAllAdminRole("Admin");
            return userList.stream()
                    .map(user -> mapperUtil.convert(user, new UserDto()))
                    .peek(dto -> dto.setOnlyAdmin(isOnlyAdmin(dto)))
                    .collect(Collectors.toList());
        }
    }

    private boolean isOnlyAdmin(UserDto userDTO) {
        User user = mapperUtil.convert(userDTO, new User());
        Integer userOnlyAdmin = userRepository.isUserOnlyAdmin(user.getCompany(), user.getRole());
        return userOnlyAdmin == 1;

    }

    @Override
    public void save(UserDto userDTO) {
        userDTO.setEnabled(true);
        User user = mapperUtil.convert(userDTO, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(mapperUtil.convert(userDTO, new User()));
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findByIdAndIsDeleted(id, false);
        user.setIsDeleted(true);
        user.setUsername(user.getUsername() + " deleted"+user.getId());
        userRepository.save(user);
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return mapperUtil.convert(user, new UserDto());
    }

    @Override
    public UserDto updateUser(UserDto userDtoToBeUpdate) {
        User user1 = userRepository.findById(userDtoToBeUpdate.getId()).orElseThrow();
        user1.setUsername(user1.getUsername());
        userRepository.save(user1);
        User convertedUser = mapperUtil.convert(userDtoToBeUpdate, new User());
        convertedUser.setId(user1.getId());
        userRepository.save(convertedUser);
        return mapperUtil.convert(convertedUser, new UserDto());
    }


}