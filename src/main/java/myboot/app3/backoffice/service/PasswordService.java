package myboot.app3.backoffice.service;

import myboot.app3.backoffice.dto.ReqRes;
import myboot.app3.backoffice.entity.User;
import myboot.app3.backoffice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PasswordService {

    private static final Logger LOGGER = Logger.getLogger(PasswordService.class.getName());

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes changePassword(String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        LOGGER.info("Attempting to change password for email: " + email);

        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user);
            user.setPassword(passwordEncoder.encode(newPassword));
            System.out.println(newPassword);
            user.setMustChangePassword(false);
            userRepo.save(user);
            ReqRes res = new ReqRes();
            res.setStatusCode(200);
            res.setMessage("Password changed successfully");
            return res;
        } else {
            LOGGER.warning("User not found for email: " + email);
            ReqRes res = new ReqRes();
            res.setStatusCode(404);
            res.setMessage("User not found");
            return res;
        }
    }
    public ReqRes changePasswordClient(String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tel = authentication.getName();

        LOGGER.info("Attempting to change password for tel: " + tel);

        Optional<User> userOptional = userRepo.findByTel(tel);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user);
            user.setPassword(passwordEncoder.encode(newPassword));
            System.out.println(newPassword);
            user.setMustChangePassword(false);
            userRepo.save(user);
            ReqRes res = new ReqRes();
            res.setStatusCode(200);
            res.setMessage("Password changed successfully");
            return res;
        } else {
            LOGGER.warning("User not found for email: " + tel);
            ReqRes res = new ReqRes();
            res.setStatusCode(404);
            res.setMessage("User not found");
            return res;
        }
    }
}
