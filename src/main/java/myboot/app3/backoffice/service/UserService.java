package myboot.app3.backoffice.service;

import myboot.app3.backoffice.Util.SMSGenerator;
import myboot.app3.backoffice.dto.ReqRes;
import myboot.app3.backoffice.entity.User;
import myboot.app3.backoffice.repository.UserRepo;
import myboot.app3.backoffice.Util.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private SmsService smsService;

    public ReqRes register(ReqRes reqRes) {
        User user = new User();
        user.setFname(reqRes.getFname());
        user.setLname(reqRes.getLname());
        user.setEmail(reqRes.getEmail());
        user.setRole(reqRes.getRole());
        String temporaryPassword = null;

        if ("AGENT".equalsIgnoreCase(reqRes.getRole())) {
            user.setAddress(reqRes.getAddress());
            user.setDate(reqRes.getDate());
            user.setTel(reqRes.getTel());
            user.setCin(reqRes.getCin());
            user.setNumImmatriculation(reqRes.getNumImmatriculation());
            user.setNumPatente(reqRes.getNumPatente());
            user.setMustChangePassword(true);
             temporaryPassword = PasswordGenerator.generateRandomPassword();
            user.setPassword(passwordEncoder.encode(temporaryPassword));
            emailService.sendTemporaryPassword(user.getEmail(), temporaryPassword);
        }

        userRepo.save(user);
        reqRes.setStatusCode(200);
        reqRes.setMessage("User registered successfully");
        reqRes.setMustChangePassword(user.isMustChangePassword());
        if (temporaryPassword != null) {
            reqRes.setTemporaryPassword(temporaryPassword);
        }
        return reqRes;
    }
    public ReqRes createClient(ReqRes reqRes) {
        User user = new User();
        user.setFname(reqRes.getFname());
        user.setLname(reqRes.getLname());
        user.setEmail(reqRes.getEmail());
        user.setRole(reqRes.getRole());
        System.out.println(reqRes.getRole());

        user.setAddress(reqRes.getAddress());
        user.setTel(reqRes.getTel());
        user.setCin(reqRes.getCin());
        user.setInitialBalance(reqRes.getInitialBalance());
        System.out.println(reqRes.getInitialBalance());
        user.setMustChangePassword(true);
        String temporaryPassword = SMSGenerator.generateOTP();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        smsService.sendSmsSS(user.getTel(), temporaryPassword);

        userRepo.save(user);

        reqRes.setStatusCode(200);
        reqRes.setMessage("Client registered successfully");
        reqRes.setMustChangePassword(user.isMustChangePassword());
        reqRes.setTemporaryPassword(temporaryPassword);

        return reqRes;
    }



    public ReqRes registerAdmin(ReqRes reqRes) {
        List<User> existingAdmin = userRepo.findByRole("ADMIN");
        if (!existingAdmin.isEmpty()) {
            reqRes.setStatusCode(400);
            reqRes.setMessage("An admin already exists. Only one admin can be created.");
            return reqRes;
        }

        User admin = new User();
        admin.setFname(reqRes.getFname());
        admin.setLname(reqRes.getLname());
        admin.setEmail(reqRes.getEmail());
        admin.setPassword(passwordEncoder.encode(reqRes.getPassword()));
        admin.setRole("ADMIN");

        userRepo.save(admin);
        reqRes.setStatusCode(200);
        reqRes.setMessage("Admin registered successfully");
        return reqRes;
    }

    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow();

            if (user.isMustChangePassword()) {
                response.setStatusCode(403);
                response.setMessage("Password change required");
                response.setMustChangePassword(true);
                var jwt = jwtUtils.generateToken(user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRole(user.getRole());
                return response;
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
    public ReqRes loginClient(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            String phoneNumber = loginRequest.getTel();
            String password = loginRequest.getPassword();
            System.out.println("Attempting login for phone number: " + phoneNumber);
            System.out.println("Attempting login for psswd: " + password);

            // Find the user by phone number
            var user = userRepo.findByTel(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("User not found with phone number: " + phoneNumber));

            // Log user details for debugging
            System.out.println("User found: " + user);

            // Check if the user must change their password
            if (user.isMustChangePassword()) {
                response.setStatusCode(403);
                response.setMessage("Password change required");
                response.setMustChangePassword(true);
                var jwt = jwtUtils.generateToken(user);
                response.setToken(jwt);
                response.setRole(user.getRole());
                return response;
            }

            // Generate JWT tokens for the authenticated user
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            // Set the response details
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
            System.err.println("Error during login: " + e.getMessage());
        }
        return response;
    }



    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            User users = userRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();
        try {
            List<User> result = userRepo.findByRole("AGENT");

            if (!result.isEmpty()) {
                reqRes.setListUsers(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }
    public ReqRes getAllClients() {
        ReqRes reqRes = new ReqRes();
        try {
            List<User> result = userRepo.findByRole("CLIENT");
            if (!result.isEmpty()) {
                reqRes.setListUsers(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            User usersById = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isPresent()) {
                userRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, User updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setFname(updatedUser.getFname());
                existingUser.setLname(updatedUser.getLname());
                existingUser.setAddress(updatedUser.getAddress());
                existingUser.setDate(updatedUser.getDate());
                existingUser.setTel(updatedUser.getTel());
                existingUser.setCin(updatedUser.getCin());
                existingUser.setNumImmatriculation(updatedUser.getNumImmatriculation());
                existingUser.setNumPatente(updatedUser.getNumPatente());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                User savedUser = userRepo.save(existingUser);
                reqRes.setUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }


}
