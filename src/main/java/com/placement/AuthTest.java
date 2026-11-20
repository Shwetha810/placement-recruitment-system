
package com.placement;

import com.placement.model.User;
import com.placement.service.AuthService;

import java.sql.SQLException;
import java.util.Scanner;

public class AuthTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();

        while (true) {

            System.out.println("\n===== PLACEMENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Student Registration");
            System.out.println("2. Student Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
                continue;
            }

            if (choice == 1) {

                System.out.print("Enter full name: ");
                String name = sc.nextLine();

                System.out.print("Enter email: ");
                String email = sc.nextLine();

                System.out.print("Enter password: ");
                String password = sc.nextLine();

                String result = authService.registerStudent(
                        name, email, password
                );

                System.out.println(result);

            } else if (choice == 2) {

                System.out.print("Enter email: ");
                String email = sc.nextLine();

                System.out.print("Enter password: ");
                String password = sc.nextLine();

                try {

                    User user = authService.login(email, password);

                    if (user != null) {
                        System.out.println(
                                "Welcome, " + user.getFullName()
                        );
                        System.out.println(
                                "Role: " + user.getRole()
                        );
                    } else {
                        System.out.println(
                                "Invalid email or password."
                        );
                    }

                } catch (SQLException e) {
                    System.out.println("Login failed due to database error.");
                    e.printStackTrace();
                }

            } else if (choice == 3) {

                System.out.println("Thank you!");
                break;

            } else {

                System.out.println("Invalid choice.");
            }
        }

        sc.close();
    }
}
