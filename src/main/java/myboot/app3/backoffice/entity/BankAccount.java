package myboot.app3.backoffice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount implements Serializable {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountNumber;
    private Double accountBalance;



    // @OneToMany(fetch = FetchType.EAGER)
  //  private List<Operation> operations;
}
