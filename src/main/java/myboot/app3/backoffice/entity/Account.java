package myboot.app3.backoffice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component

public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String verificationCode;
    @OneToOne
    @JoinColumn(name = "User_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;


}