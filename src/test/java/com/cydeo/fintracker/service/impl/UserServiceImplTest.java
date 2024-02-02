package com.cydeo.fintracker.service.impl;

import com.cydeo.fintracker.dto.UserDto;
import com.cydeo.fintracker.entity.Company;
import com.cydeo.fintracker.entity.User;
import com.cydeo.fintracker.exception.CategoryNotFoundException;
import com.cydeo.fintracker.exception.UserNotFoundException;
import com.cydeo.fintracker.repository.CompanyRepository;
import com.cydeo.fintracker.repository.UserRepository;
import com.cydeo.fintracker.util.MapperUtil;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.xmlunit.util.Mapper;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MapperUtil mapperUtil;

    @Mock
    SecurityServiceImpl securityServiceImpl;

    @Mock
    CompanyRepository companyRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

//    ----------findUserById()----------
    @Test
    void should_throw_an_exception_when_User_does_not_exist() {
//        GIVEN
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

//        WHEN
        Throwable throwable = catchThrowable(() -> userService.findUserById(anyLong()));

//        THEN
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        assertThat(throwable).hasMessage("User not found.");

    }

    @Test
    void should_return_the_User_when_User_exist() {

//        GIVEN
        User user = new User();
        user.setId(1L);
        UserDto userDto = new UserDto();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(user, new UserDto())).thenReturn(userDto);

//        WHEN
        UserDto result = userService.findUserById(1L);

//        THEN
        assertThat(result).isEqualTo(userDto);
    }


    //    ----------findByUsername()----------
    @Test
    void should_not_find_user_by_username() {
//        GIVEN
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

//        WHEN
        Throwable throwable = catchThrowable(() -> userService.findByUsername(anyString()));

//        THEN
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void should_find_the_User_by_username() {

//        GIVEN
        User user = new User();
        user.setUsername("test");

        UserDto userDto = new UserDto();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(user, new UserDto())).thenReturn(userDto);

//        WHEN
        UserDto result = userService.findByUsername("test");

//        THEN
        assertThat(result.getUsername()).isEqualTo("test");
    }

    //    ----------save()----------
    @Test
    void should_save_user() {
        //GIVEN
        UserDto userDTO = new UserDto();
        userDTO.setUsername("username");
        userDTO.setPassword("testPassword");
        User user = new User();

        when(mapperUtil.convert(eq(userDTO), any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User storedUser = new User();
        when(userRepository.save(any(User.class))).thenReturn(storedUser);

        //WHEN
        UserDto result = userService.save(userDTO);

        //THEN
        assertNotNull(result);
        verify(mapperUtil, times(1)).convert(eq(storedUser), any(UserDto.class));
    }


    //    ----------delete()----------
    @Test
    void should_soft_delete_for_user() {
        //GIVEN
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        //WHEN
        userService.delete(user.getId());

        //THEN
        Assertions.assertTrue(user.getIsDeleted());
        verify(userRepository).findById(anyLong());
    }

    //    ----------update()----------
    @Test
    void user_should_be_update() {

        //GIVEN
        UserDto userDTO = new UserDto();
        userDTO.setId(1L);
        userDTO.setUsername("updatedUsername");
        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));
        when(mapperUtil.convert(eq(userDTO), any(User.class))).thenReturn(user);

        //WHEN
        userService.update(userDTO);

        //THEN
        assertEquals(user.getId(), userDTO.getId());
        verify(userRepository).save(user);
    }

    //    ----------listallUser()----------
    @Test
    void should_return_all_list() {

        Authentication auth = mock(Authentication.class);
//        SecurityContextHolder.getContext().setAuthentication(auth);


        //GIVEN
        User loggedInUser = new User();
        loggedInUser.setUsername("username");
        loggedInUser.setId(2L); // Assuming a non-admin user

        Company company = new Company();
        company.setId(1L);
        company.setTitle("Abc");

        UserDto userDTO = new UserDto();

        loggedInUser.setCompany(company);

        // Mock the behavior of the security service to return the mock user
        when(securityServiceImpl.getLoggedInUser()).thenReturn(userDTO);

        when(userRepository.findByUsername("username")).thenReturn(Optional.of(loggedInUser));
        when(companyRepository.findById(loggedInUser.getCompany().getId())).thenReturn(Optional.of(company));

        List<User> userList = new ArrayList<>();
        userList.add(loggedInUser);
        when(userRepository.findAllUserWithCompanyAndIsDeleted(company, false)).thenReturn(userList);

        when(mapperUtil.convert(eq(User.class), any(UserDto.class))).thenReturn(userDTO);

        //WHEN
        List<UserDto> result = userService.listAllUsers();

        //THEN
        assertEquals(1, result.size());
        verify(userRepository).findByUsername("username");
        verify(companyRepository).findById(loggedInUser.getCompany().getId());
        verify(userRepository).findAllUserWithCompanyAndIsDeleted(company, false);
        verify(mapperUtil, times(2)).convert(any(User.class), any(UserDto.class));

    }


}
