package com.cydeo.fintracker.service.impl;

import com.cydeo.fintracker.repository.UserRepository;
import com.cydeo.fintracker.util.MapperUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xmlunit.util.Mapper;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MapperUtil mapperUtil;

    @InjectMocks
    private UserServiceImpl userService;






}