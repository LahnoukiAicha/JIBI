package myboot.app3.backoffice.service;

import myboot.app3.backoffice.dto.ReqRes;
import myboot.app3.backoffice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmiService {
    @Autowired
    private UserRepo userRepo;

    public boolean telephoneExists(String telephone) {
        return userRepo.findByTel(telephone).isPresent();
    }
    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }



    public boolean validateClientNum(String num) {
        return num.matches("^2126\\d{8}$");
    }

    public boolean validateClientEmail (String email){

        String regexPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        return email.matches(regexPattern);

    }

    public boolean checkBalanceAccount(Double accountBalance) {
        return accountBalance != null && accountBalance < 1;
    }







}
