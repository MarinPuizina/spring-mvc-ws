package app.ws.service.impl;

import app.ws.exceptions.UserServiceException;
import app.ws.io.entity.AddressEntity;
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
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        userEntity.setLastName("Puizina");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("sdawadsda");
        userEntity.setAddresses(getAddressesEntity());

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
    void createUser_UserServiceException() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Marin");
        userDto.setLastName("Puizina");
        userDto.setPassword("123");
        userDto.setEmail("test@test.com");

        assertThrows(UserServiceException.class,

                () -> userService.createUser(userDto)

        );

    }

    @Test
    void createUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        when(utils.generateAddressId(anyInt())).thenReturn(encryptedPassword);

        when(utils.generateUserId(anyInt())).thenReturn(userId);

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(userId);

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);


        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Marin");
        userDto.setLastName("Puizina");
        userDto.setPassword("123");
        userDto.setEmail("test@test.com");

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);

        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        verify(utils, times(2)).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("123");
        verify(userRepository, times(1)).save(any(UserEntity.class))    ;

    }

    private List<AddressDto> getAddressesDto() {

        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDto.setCity("Split");
        addressDto.setCountry("Croatia");
        addressDto.setPostalCode("21000");
        addressDto.setStreetName("123 street name");

        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setType("billing");
        billingAddressDto.setCity("Split");
        billingAddressDto.setCountry("Croatia");
        billingAddressDto.setPostalCode("21000");
        billingAddressDto.setStreetName("123 street name");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);

        return addresses;

    }

    private List<AddressEntity> getAddressesEntity() {

        List<AddressDto> addresses = getAddressesDto();

        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();

        return new ModelMapper().map(addresses, listType);

    }

}