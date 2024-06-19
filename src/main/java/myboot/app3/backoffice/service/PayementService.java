package myboot.app3.backoffice.service;

import myboot.app3.backoffice.entity.*;
import myboot.app3.backoffice.form.ConfirmPayement;
import myboot.app3.backoffice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PayementService {
    @Autowired
    private PayementRepository payementRepository;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private BankAccountRepo bankAccountRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProduitRepository produitRepository;

    public Map<String, String> confirmPayement(ConfirmPayement request) {
        System.out.println("wa toooooongoooooooooo");

        // Log input data
        System.out.println("Request: " + request);

        // Check client
        User user = userRepository.findById(Math.toIntExact(request.getUserId())).orElse(null);
        if (user == null) {
            System.out.println("Can't find client");
            return Map.of("message", "Can't find client");
        }

        // Check account
        BankAccount bankAccount = user.getBankAccount();
        if (bankAccount == null) {
            System.out.println("Can't find account");
            return Map.of("message", "Can't find account");
        }

        // Check verification code
        Account account = accountRepository.findByUserId(request.getUserId());
        if (account == null || account.getVerificationCode().isEmpty()) {
            System.out.println("Verification code not found");
            return Map.of("message", "Le code est erroné");
        }

        String code = account.getVerificationCode();
        if (!code.equals(request.getCode())) {
            System.out.println("Verification code mismatch");
            return Map.of("message", "Code est erroné");
        }

        // Check produit
        if (request.getUnpaid() == null) {
            System.out.println("Unpaid ID is null");
            return Map.of("message", "Unpaid ID is null");
        }
        Produit produit = produitRepository.findById(request.getUnpaid()).orElse(null);
        if (produit == null) {
            System.out.println("Produit non trouvé");
            return Map.of("message", "Produit non trouvé");
        }

        // Everything is set, we start paying each unpaid
        String numeroCompte = bankAccount.getAccountNumber();
        try {
            Payement payement = new Payement(null, new Date().getTime(), numeroCompte, user);

            // Ensure produit has a valid price
            Long price = produit.getPrice();
            if (price == null) {
                System.out.println("Produit price is null");
                return Map.of("message", "Produit price is null");
            }

            bankAccount.setAccountBalance(bankAccount.getAccountBalance() - price);
            bankAccountRepository.save(bankAccount);
            bankAccountRepository.flush();

            // Update produit unpaid field
            produit.setUnpaid(false);
            produitRepository.save(produit);
            produitRepository.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("message", "SQL error");
        }

        return Map.of("message", "Paiement effectué");
    }
}
