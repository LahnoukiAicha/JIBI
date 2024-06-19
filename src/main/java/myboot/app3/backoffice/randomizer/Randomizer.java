package myboot.app3.backoffice.randomizer;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class Randomizer {
    private Random random;
    private static final String alphabetUppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String alphabetLowercase = "abcdefghijklmnopqrstuvwxyz";
    private static final String alphabet=alphabetLowercase+alphabetUppercase;
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
    private static final String numbers = "0123456789";

    public static String generateAgentUser(){
        String username = RandomStringUtils.random( 5, alphabetUppercase )+RandomStringUtils.random(4,numbers);
        System.out.println(username);
        return username;
    }

    public static String generateClientCompte(){
        String compte = RandomStringUtils.random( 8, alphabetUppercase )+RandomStringUtils.random(8,numbers);
        return compte;
    }
    public static String generatePassword(){
        String password = RandomStringUtils.random(5,alphabet)+RandomStringUtils.random(4,numbers);
        System.out.println(password);
        return password;
    }

}
