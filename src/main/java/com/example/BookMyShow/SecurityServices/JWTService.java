package com.example.BookMyShow.SecurityServices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class JWTService {

    @Autowired
    private SecretKey secretKey;

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // Set key size to 256 bits
            //secretKey = keyGen.generateKey(); // Generate the key
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public String generateToken(String username, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", roles);
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600*1000)) //1hour
                .and()
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        return secretKey;
    }

    public String extractUserName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject(); // Returns the subject (username) from the claims
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration(); // Returns the expiration date from the claims
    }

}
/*
    extractAllClaims method

    Jwts.parser() is a component responsible for interpreting and validating a JWT. It takes a JWT string as input and
    breaks it down into its constituent parts, verifies its integrity, and extracts the claims (data) stored within it.

    1.Jwts.parser():
    This initializes the JWT parser. This is a standard method to create a parser instance from the JJWT library.

    2..verifyWith(getKey()):
    This method sets the signing key used to verify the JWT’s signature. The getKey() method should return the
    correct key that was used to sign the JWT. Make sure that the key is the same as the one used for signing the token.

    3..build():
    This part finalizes the configuration of the parser. In some contexts, this might be optional, depending on
    how the library is designed.

    4..parseSignedClaims(token):

    This method parses the provided JWT string (token) and verifies its signature. If the signature is valid, it
    returns an object that contains the claims.

    5..getPayload():
    This retrieves the claims (the payload) from the parsed JWT. The Claims object contains various pieces of
    information encoded in the JWT.

 */

/*

    The secret key is generated once when the JWTService instance is created. This ensures that the same key
    is used for signing and verifying tokens throughout the lifetime of that instance.

    Your JWTService constructor for generating a SecretKey. This approach will generate a secure HMAC SHA-256 key,
    which is appropriate for signing JWTs.

    However, you should ensure that the SecretKey is consistent and reused for both token generation and validation

 */

/*
    1.Method Declaration:
    public String generateToken(String username): This method is public and returns a String, which will be the generated
    JWT token. It takes a single parameter, username, which represents the subject of the token.


    2.Claims Initialization:
    Map<String, Object> claims = new HashMap<>();: A new HashMap is created to hold claims, which are key-value
    pairs that provide additional information about the token (like user roles, permissions, etc.)

    3.JWT Builder:
    return Jwts.builder(): This initializes a JWT builder instance from the jjwt library, allowing you to configure
    various properties of the token.


    4.Adding Claims:
    .claims(): This prepares the claims to be added to the JWT. It indicates that you will be working with claims
    in the upcoming method calls.

    .add(claims): This method is intended to add the previously created claims map to the JWT. The specific behavior
    of this method will depend on the version of the jjwt library you are using. In your original code,
    it effectively adds the claims map you initialized.


    5.Setting the Subject:
    .subject(username): Sets the subject of the JWT to the provided username. This is a common practice to indicate
    who the token belongs to.


    6.Signing the Token:
    .signWith(getKey()): This method is used to sign the JWT with a cryptographic key. The getKey() method should
    return the key used for signing. This step is crucial to ensure that the token cannot be tampered with after
    it is created.

    This signWith(getKey()) signs the generated token. The signature is generated based on header and payload(claims)
    combined with a secret or private key.
    Signing the token ensures that the toke cannot be altered after it has been issued. If anyone tries to change
    the payload(the claims) or header of the token, the signature will no longer match, and the token will be
    considered invalid.


    7.Compacting the Token:
    .compact(): This finalizes the token creation process. It generates the JWT as a compact, URL-safe string that
    can be sent to clients. This string can be stored or transmitted for authentication purposes.

*/
