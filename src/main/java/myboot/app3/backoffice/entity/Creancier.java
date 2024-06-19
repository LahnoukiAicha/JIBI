package myboot.app3.backoffice.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
public class Creancier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String categorie;
    private String logo;

    @OneToMany(mappedBy = "creancier", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Produit> produits;

    @Override
    public String toString() {
        return "Creancier{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}