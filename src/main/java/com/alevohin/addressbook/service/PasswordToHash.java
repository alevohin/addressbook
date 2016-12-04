package com.alevohin.addressbook.service;

import com.vaadin.spring.annotation.SpringComponent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;

@SpringComponent
@ConfigurationProperties(prefix = "addressbook.auth")
public class PasswordToHash {

    private String salt;

    public String convert(String rawPassword) {
        try {
            String password = rawPassword + salt;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            return new String(Hex.encode(md.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
