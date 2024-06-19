package myboot.app3.backoffice.controller;

import myboot.app3.backoffice.dto.ReqRes;
import myboot.app3.backoffice.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/client/send-sms")
    public ResponseEntity<Map<String, String>> sendSms(@RequestParam Long productId, @RequestParam Long clientId) {
        smsService.sendSms(productId, clientId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "SMS sent!");
        return ResponseEntity.ok(response);
    }



}
