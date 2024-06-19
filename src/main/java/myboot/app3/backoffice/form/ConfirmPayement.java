package myboot.app3.backoffice.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPayement {
    private Long unpaid;
    private Long userId;
    private String code;
    @Override
    public String toString() {
        return "ConfirmPayement{" +
                "unpaid=" + unpaid +
                ", userId=" + userId +
                ", code='" + code + '\'' +
                '}';
    }
}