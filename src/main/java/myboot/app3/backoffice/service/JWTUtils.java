package myboot.app3.backoffice.service;
// class designed to handle operations related to JSON Web Tokens (JWTs) within a
// Spring application. It provides methods to generate, parse, and validate JWTs,
// which are commonly used for authentication and authorization


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
@Component
public class JWTUtils {

    private SecretKey Key;//A SecretKey instance used for signing JWTs.
    private  static  final long EXPIRATION_TIME = 86400000;// A constant representing the token expiration time (24 hours).


    public JWTUtils(){
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    //Creates a JWT for the provided UserDetails, setting the subject,
    // issue date, expiration date, and signing the token with the Key
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }
    public  String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    //Extracts the username (subject) from the token
    public  String extractUsername(String token){
        return  extractClaims(token, Claims::getSubject);
    }

    //Parses the token and applies a function to the claims to extract specific information.
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    //validates the token by checking if the extracted username matches the UserDetails 's username
    public  boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //check if the token is not expired.
    public  boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

}
