package myboot.app3.backoffice.repository;

import myboot.app3.backoffice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserId(Long userId);
    @Query(value="SELECT * FROM Account WHERE client_id=:clientId",nativeQuery = true)
    public List<Account> findListByUserId(Long clientId);
}