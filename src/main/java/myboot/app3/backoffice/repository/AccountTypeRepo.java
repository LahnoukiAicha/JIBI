package myboot.app3.backoffice.repository;


import myboot.app3.backoffice.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTypeRepo extends JpaRepository<AccountType,Long> {
}