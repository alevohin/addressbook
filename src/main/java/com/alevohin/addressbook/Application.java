package com.alevohin.addressbook;

import com.alevohin.addressbook.spring.VaadinSessionSecurityContextHolderStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Application {

    static {
        SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
