package myboot.app3.backoffice.repository;

import myboot.app3.backoffice.entity.Payement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PayementRepository extends JpaRepository<Payement,Long> {
    List<Payement> findPayementsByUserId(Long UserId);
}