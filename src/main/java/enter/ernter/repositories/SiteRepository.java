package enter.ernter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import enter.ernter.entities.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    Optional<Site> findByDomain(String domain);
}
