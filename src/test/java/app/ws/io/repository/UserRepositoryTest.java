package app.ws.io.repository;

import app.ws.io.entity.AddressEntity;
import app.ws.io.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Needed to make it an integration test
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    // Set this to false if working with H2 test database so we can create some data
    static boolean recordsCreated = true;

    @BeforeEach
    void setUp() {

        if(!recordsCreated)
            createRecords();

    }

    private void createRecords() {
        // Prepare User Entity
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Marin");
        userEntity.setLastName("Puizina");
        userEntity.setUserId("1a2b3c");
        userEntity.setEncryptedPassword("123");
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationStatus(true);

        // Prepare User Addresses
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setType("shipping");
        addressEntity.setAddressId("ahgyt74hfy");
        addressEntity.setCity("Split");
        addressEntity.setCountry("Croatia");
        addressEntity.setPostalCode("ABCCDA");
        addressEntity.setStreetName("123 Street Address");

        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(addressEntity);

        userEntity.setAddresses(addresses);

        userRepository.save(userEntity);


        // Prepare User Entity
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setFirstName("Marin");
        userEntity2.setLastName("Puizina");
        userEntity2.setUserId("1a2b3cddddd");
        userEntity2.setEncryptedPassword("123");
        userEntity2.setEmail("test2@test.com");
        userEntity2.setEmailVerificationStatus(true);

        // Prepare User Addresses
        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setType("shipping");
        addressEntity2.setAddressId("ahgyt74hfywwww");
        addressEntity2.setCity("Split");
        addressEntity2.setCountry("Croatia");
        addressEntity2.setPostalCode("ABCCDA");
        addressEntity2.setStreetName("123 Street Address");

        List<AddressEntity> addresses2 = new ArrayList<>();
        addresses2.add(addressEntity2);

        userEntity2.setAddresses(addresses2);

        userRepository.save(userEntity2);
    }

    @Test
    void findAllUsersWithConfirmedEmailAddress() {

        Pageable pageableRequest = PageRequest.of(0, 2);
        Page<UserEntity> pages = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);

        assertNotNull(pages);

        List<UserEntity> userEntities = pages.getContent();
        assertNotNull(userEntities);

    }

    @Test
    void findUserByFirstName() {

        String firstName = "Marin";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);

        assertNotNull(users);
        assertTrue(users.size() > 1);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));

    }

    @Test
    void findUserByLastName() {

        String lastName = "Puizina";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);

        assertNotNull(users);
        assertTrue(users.size() > 1);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));

    }

    @Test
    void findUserByKeyword() {

        String keyword = "Marin";
        List<UserEntity> users = userRepository.findUsersByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() > 1);

        UserEntity user = users.get(0);
        assertTrue(
                user.getLastName().contains(keyword) ||
                        user.getFirstName().contains(keyword)
                  );

    }

    @Test
    void findUserFirstNameAndLastNameByKeyword() {

        String keyword = "Marin";
        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() > 1);

        Object[] user = users.get(0);
        assertTrue(user.length == 2);
        // We know that first name is at index 0 and last name at index 1 because of our query
        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFirstName);
        assertNotNull(userLastName);

        System.out.println("First name = " + userFirstName);
        System.out.println("Last name = " + userLastName);

    }

    @Test
    void updateUserEmailVerificationStatus() {

        boolean emailVerificationStatus = true;
        String userId = "VbyaK1PELFQIQz3ZVDKSI1XF5SCcoe";

        userRepository.updateUserEmailVerificationStatus(emailVerificationStatus, userId);

        UserEntity storedDetails = userRepository.findByUserId(userId);

        assertTrue(storedDetails.getEmailVerificationStatus() == emailVerificationStatus);

    }

    @Test
    void findUserEntityByUserId() {

        String userId = "VbyaK1PELFQIQz3ZVDKSI1XF5SCcoe";
        UserEntity userEntity = userRepository.findUserEntityByUserId(userId);

        assertNotNull(userEntity);
        assertTrue(userEntity.getUserId().equals(userId));

    }

    @Test
    void getUserEntityFullNameById() {

        String userId = "VbyaK1PELFQIQz3ZVDKSI1XF5SCcoe";
        List<Object[]> records = userRepository.getUserEntityFullNameById(userId);

        assertNotNull(records);
        assertTrue(records.size() == 1);

        Object[] userDetails = records.get(0);

        String firstName = String.valueOf(userDetails[0]);
        String lastName = String.valueOf(userDetails[1]);

        assertNotNull(firstName);
        assertNotNull(lastName);

    }

    @Test
    void updateUserEntityEmailVerificationStatus() {

        boolean emailVerificationStatus = true;
        String userId = "VbyaK1PELFQIQz3ZVDKSI1XF5SCcoe";

        userRepository.updateUserEntityEmailVerificationStatus(emailVerificationStatus, userId);

        UserEntity storedDetails = userRepository.findByUserId(userId);

        assertTrue(storedDetails.getEmailVerificationStatus() == emailVerificationStatus);

    }

}