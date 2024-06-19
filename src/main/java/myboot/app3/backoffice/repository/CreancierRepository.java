package myboot.app3.backoffice.repository;

import myboot.app3.backoffice.entity.Creancier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CreancierRepository extends JpaRepository<Creancier, Long> {
    @Query("SELECT c FROM Creancier c LEFT JOIN FETCH c.produits WHERE c.id = :id")
    Optional<Creancier> findByIdWithProduits(@Param("id") Long id);
}