package app.ws.io.repository;

import app.ws.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Using PagingAndSortingRepository allows us to use pagination
// Furthermore, it lets us use methods from CrudRepository
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);

    // Important - for query to be treated as native SQL query, we need to provide a value and "nativeQuery = true"
    // Also, we use custom name for calling this query
    // countQuery is used by JPA to figure total amount of records that needs to split in different pages
    // If we don't provide the countQuery then JPA will try to figure out the count of records by itself
    @Query(value = "select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
            countQuery = "select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
            nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

    // ?1 and ?2 .. are called positional parameters or index parameters. They map to String firstName and String lastName...
    // Position of the param is important to match with String names in method param list
    @Query(value = "select * from Users u where u.first_name = ?1", nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    // We can also use @Param annotation to map data to query, name of param inside @Param("lastName") must match to ":lastName"
    // When using this type of query, position of names doesn't matter
    @Query(value = "select * from Users u where u.last_name = :lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

    // %:keyword -> represents 0,1 or many chars, it doesn't matter what is the beginning of the string, it's important
    // that it has to end with keyword we will pass in
    // :keyword% -> that it begins with keyword and rest is not important
    // %:keyword% -> doesn't matter how it starts or ends it's important that it contains the keyword
    @Query(value="select * from Users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%", nativeQuery = true)
    List<UserEntity> findUsersByKeyword(@Param("keyword") String keyword);

    @Query(value="select u.first_name, u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
    List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

    // @Transactional annotation helps us when modifying database, if something goes wrong it will be rollback and data will be unchanged
    // In case of using update or delete SQL queries which are modifying database, we need to use @Modifying annotation
    @Transactional
    @Modifying
    @Query(value = "update users u set u.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus where u.user_id = :userId", nativeQuery = true)
    void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);

    // JPQL query -> we don't use database names, we use names from our code, like UserEntity class name and field names
    // JPQL is independent on database, therefore same query should work with any database
    @Query("select user from UserEntity user where user.userId = :userId")
    UserEntity findUserEntityByUserId(@Param("userId") String userId);

    @Query("select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
    List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u set u.emailVerificationStatus = :emailVerificationStatus where u.userId = :userId")
    void updateUserEntityEmailVerificationStatus(
            @Param("emailVerificationStatus") boolean emailVerificationStatus,
            @Param("userId") String userId);


}
