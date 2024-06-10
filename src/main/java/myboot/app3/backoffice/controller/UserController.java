package myboot.app3.backoffice.controller;

import myboot.app3.backoffice.dto.ReqRes;
import myboot.app3.backoffice.entity.User;
import myboot.app3.backoffice.service.PasswordService;
import myboot.app3.backoffice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;


    @PostMapping("/register-admin")
    public ResponseEntity<ReqRes> registerAdmin(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(userService.registerAdmin(reg));
    }

    @PostMapping("/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(userService.register(reg));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
        return ResponseEntity.ok(userService.login(req));
    }

    @PostMapping("/loginClient")
    public ResponseEntity<ReqRes> loginClient(@RequestBody ReqRes req) {
        return ResponseEntity.ok(userService.loginClient(req));
    }


    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {
        return ResponseEntity.ok(userService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-agents")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/get-agents/{agentId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Integer agentId) {
        return ResponseEntity.ok(userService.getUsersById(agentId));
    }

    @PutMapping("/admin/update/{agentId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable Integer agentId, @RequestBody User reqres) {
        return ResponseEntity.ok(userService.updateUser(agentId, reqres));
    }

    @DeleteMapping("/admin/delete/{agentId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer agentId) {
        return ResponseEntity.ok(userService.deleteUser(agentId));
    }

    @GetMapping("/agent/getProfile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        // Log the authorities
        authentication.getAuthorities().forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
        ReqRes response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }   @GetMapping("/client/getProfile")
    public ResponseEntity<ReqRes> getClientProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        authentication.getAuthorities().forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
        ReqRes response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("/change-password")
    public ResponseEntity<ReqRes> changePassword(@RequestBody ReqRes changePasswordRequest) {
        String newPassword = changePasswordRequest.getPassword();
        ReqRes response = passwordService.changePassword(newPassword);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PutMapping("/changePasswordClient")
    public ResponseEntity<ReqRes> changePasswordClient(@RequestBody ReqRes changePasswordRequest) {
        String newPassword = changePasswordRequest.getPassword();
        ReqRes response = passwordService.changePassword(newPassword);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/createClient")
    public ResponseEntity<ReqRes> createClient(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(userService.createClient(reg));
    }
    @GetMapping("/agent/get-all-clients")
    public ResponseEntity<ReqRes> getAllClient() {
        return ResponseEntity.ok(userService.getAllClients());
    }

    @GetMapping("/agent/get-client/{idClient}")
    public ResponseEntity<ReqRes> getClientById(@PathVariable Integer idClient) {
        return ResponseEntity.ok(userService.getUsersById(idClient));
    }
}
