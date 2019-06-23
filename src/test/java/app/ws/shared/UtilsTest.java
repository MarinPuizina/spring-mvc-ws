package app.ws.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

// Anotations to make integration tests case
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() {



    }

    @Test
    void generateUserId() {

        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(userId2));

    }

    @Test
    void hasTokenNotExpired() {

        String token = utils.generateEmailVerificationToken("dsae2ewa");
        assertNotNull(token);

        Boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);

    }

    @Test
    void hasTokenExpired() {

        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0NEB0ZXN0LmNvbSIsImV4cCI6MTU2MDcxODY0N30.HmLWEfkdrHt3pwswlhQ-w-ndEgPGD72rjQi3p01CoWk4e3N3oIs99F83sfFAmLyaVhu9hBS_1e6uJYwUHEJNhw";

        Boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
        assertTrue(hasTokenExpired);

    }

}