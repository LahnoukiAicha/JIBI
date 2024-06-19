package myboot.app3.backoffice.controller;


import myboot.app3.backoffice.form.ConfirmPayement;
import myboot.app3.backoffice.service.PayementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/client/payement")
@CrossOrigin(origins = "http://localhost:4200")
public class PayementController {

    @Autowired
    private PayementService payementService;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> pay(@RequestBody ConfirmPayement request) {
        System.out.println("you are in /pay");
        Map<String, String> result = payementService.confirmPayement(request);
        if (!result.get("message").equals("Paiement effectué")) {
            return ResponseEntity.status(403).body(result);
        }
        return ResponseEntity.ok(result);
    }
}
