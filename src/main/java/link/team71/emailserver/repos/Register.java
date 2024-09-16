package link.team71.emailserver.repos;

import link.team71.emailserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Register extends JpaRepository<User, Object> {

    User findByEmail(String email);

}
