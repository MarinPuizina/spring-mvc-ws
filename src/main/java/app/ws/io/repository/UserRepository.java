package app.ws.io.repository;

import app.ws.io.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

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

}
