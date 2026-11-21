package com.placement.ui;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateAdminHash {

    public static void main(String[] args) {

        String password = "Admin@123";

        String hash = BCrypt.hashpw(
                password,
                BCrypt.gensalt(12)
        );

        System.out.println("Password Hash: " + hash);
    }
}