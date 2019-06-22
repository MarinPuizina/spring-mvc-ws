package app.ws.ui.controller;

import app.ws.service.impl.UserServiceImpl;
import app.ws.shared.dto.AddressDto;
import app.ws.shared.dto.UserDto;
import app.ws.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    // Instantiates userController and injects mocks into it
    // When our unit test is run and we call a method on userController like getUser()
    // The userService is injected into userController
    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDto userDto;

    final String USER_ID = "jkbn23nju213njk12nj3";

    @BeforeEach
    void setUp() {

        // Initializing mocks so we can use mocks and inject them
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Marin");
        userDto.setLastName("Puizina");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("exad23dfdxad5");


    }

    @Test
    void getUser() {

        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());

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

}