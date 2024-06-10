package myboot.app3.backoffice.service;


import com.vonage.client.VonageClient;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {


    @Autowired
    private VonageClient vonageClient;


    public void sendSmsSS(String number, String temporaryPassword) {
        TextMessage message = new TextMessage("JIBIAPP",
                number,
                "Your verification code is " + temporaryPassword
        );

        SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(message);

        response.getMessages().get(0).getErrorText();
    }
}
