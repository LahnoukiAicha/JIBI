package myboot.app3.backoffice.repository;

import myboot.app3.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    Optional<User> findByTel(String tel);

}
