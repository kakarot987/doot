package  com.doot.repository;

import  com.doot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(Long phoneNumber);
    Boolean existsByEmail(String email);

    @Query("SELECT u.emailVerified FROM User u WHERE u.email = ?1")
    Boolean findEmailVerifiedByEmail(String email);

    Boolean existsByPhoneNumber(Long phoneNumber);
}
