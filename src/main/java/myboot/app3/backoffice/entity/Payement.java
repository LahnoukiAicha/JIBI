package myboot.app3.backoffice.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import myboot.app3.backoffice.serializer.HibernateProxySerializer;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Long timestamp;
    private String CardNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HibernateProxySerializer.class)
    @ToString.Exclude
    private User user;

}