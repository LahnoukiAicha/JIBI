package myboot.app3.backoffice.service;


import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

import myboot.app3.backoffice.entity.Account;
import myboot.app3.backoffice.entity.User;
import myboot.app3.backoffice.repository.AccountRepository;
import myboot.app3.backoffice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static myboot.app3.backoffice.Util.SMSGenerator.generateOTP;

@Service
public class SmsService {


    @Autowired
    private VonageClient vonageClient;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Account account;


    public void sendSmsSS(String number, String temporaryPassword) {
        TextMessage message = new TextMessage("JIBIAPP",
                number,
                "Your verification code is " + temporaryPassword
        );

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        response.getMessages().get(0).getErrorText();
    }

    public void sendSms(Long productId, Long clientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Retrieve user by email
        Optional<User> userOptional = userRepo.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String verificationCode = generateOTP();
            TextMessage message = new TextMessage(
                    "JIBI APP ",
                    "212663547631",
                    "YOUR VERIFICATION CODE: " + verificationCode + "\nProduct ID: " + productId + "\nClient ID: " + clientId
            );

            SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

            Account account = new Account();
            account.setUser(user);
            account.setVerificationCode(verificationCode);
            accountRepository.save(account);

            if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                System.out.println("Message sent successfully.");
            } else {
                System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
            }
        } else {
            System.out.println("User not found with email: " + email);
        }
    }

}

