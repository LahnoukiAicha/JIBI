package myboot.app3.backoffice.dto;
// class contains various fields to encapsulate the data needed for
// requests and responses between the client and server
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import myboot.app3.backoffice.entity.BankAccount;
import myboot.app3.backoffice.entity.User;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) //only properties that are not null will be included in the serialized JSON.
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String fname;
    private String lname;
    private String email;
    private String address;
    private String date;
    private String tel;
    private String cin;
    private String numImmatriculation;
    private String numPatente;
    private String password;
    private String role;
    private User users;
    private List<User> listUsers;
    private String temporaryPassword;
    private boolean mustChangePassword;
    private Long idType;
    private Double accountBalance;
    private String sms;

}
