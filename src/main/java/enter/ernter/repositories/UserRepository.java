package enter.ernter.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import enter.ernter.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();

    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    Optional<User> findByUserIdAndSiteId(String userId, int siteId);
    
}
