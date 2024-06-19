package myboot.app3.backoffice.repository;

import myboot.app3.backoffice.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount,Long> {
}
