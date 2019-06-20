package app.ws.service.impl;

import app.ws.io.entity.UserEntity;
import app.ws.io.repository.UserRepository;
import app.ws.shared.Utils;
import app.ws.shared.dto.AddressDto;
import app.ws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "sdad2321";
    String encryptedPassword = "32131w1ee";
    UserEntity userEntity;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Marin");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("sdawadsda");

    }

    @Test
    void getUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Marin", userDto.getFirstName());

    }

    @Test
    void getUser_UsernameNotFoundException() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,

                () -> userService.getUser("test@test.com")

                );

    }

    @Test
    void createUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        when(utils.generateAddressId(anyInt())).thenReturn(encryptedPassword);

        when(utils.generateUserId(anyInt())).thenReturn(userId);

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(userId);

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(addressDto);

        UserDto userDto = new UserDto();
        userDto.setAddresses(addresses);

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);

        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());

    }

}