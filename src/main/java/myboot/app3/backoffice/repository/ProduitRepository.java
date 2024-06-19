package myboot.app3.backoffice.repository;

import jakarta.persistence.EntityManager;
import myboot.app3.backoffice.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByUserIdAndUnpaid(Long userId, boolean unpaid);
    List<Produit> findByCreancierId(Long creancierId);
    List<Produit> findByUnpaid(boolean unpaid);
}
