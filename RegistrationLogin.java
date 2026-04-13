/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.registrationlogin;

/**
 *
 * @author Student
 */

import java.util.Scanner;

public class RegistrationLogin {
    // Static variables to hold user credentials
    static String savedUsername = "";
    static String savedPassword = "";
    static String savedPhoneNumber = "";
    
    public static void main(String[] args) {
        Scanner userANDPass = new Scanner(System.in);
        
        int choice = 0;
        
        // Loop for the menu until user chooses to exit
        while (choice != 3) {
            System.out.println("\n===MENU===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            
            // Take user input for the menu choice
            choice = userANDPass.nextInt();
            userANDPass.nextLine(); // Consume the newline character
            
            // Handle user choice
            if (choice == 1) {
                register(userANDPass);  // Call the register method
            } 
            else if (choice == 2) {
                login(userANDPass);  // Call the login method
            } 
            else if (choice == 3) {
                System.out.println("Good Bye");
            } 
            else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        
        // Close scanner
        userANDPass.close();
    }

    // Method to register a new user
    public static void register(Scanner userANDPass) {
        System.out.println("Enter username:");
        savedUsername = userANDPass.nextLine();
        
        System.out.println("Enter password:");
        savedPassword = userANDPass.nextLine();
        
        System.out.println("Enter phone number:");
        savedPhoneNumber = userANDPass.nextLine();
        
        System.out.println("Registration successful");
    }

    // Method to log in with the registered information
    public static void login(Scanner userANDPass) {
        // Validation of username
        System.out.print("Enter username: ");
        String usernameLogin = userANDPass.nextLine();

        System.out.print("Enter password: ");
        String passwordLogin = userANDPass.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = userANDPass.nextLine();

        // Validation checks
        boolean validUsername = usernameLogin.contains("_") && usernameLogin.length() >= 5;
        boolean validPassword = passwordLogin.length() >= 8
                && passwordLogin.matches(".*[A-Za-z].*") 
                && passwordLogin.matches(".*[0-9].*");
        boolean validPhone = phoneNumber.length() == 10 && phoneNumber.matches("\\d{10}");

        // Validation results
        if (validUsername) {
            System.out.println("Username is valid");
        } else {
            System.out.println("Username must contain an underscore and must be at least 5 characters.");
        }

        if (validPassword) {
            System.out.println("Password is valid");
        } else {
            System.out.println("Password must be at least 8 characters and include both letters and numbers.");
        }

        if (validPhone) {
            System.out.println("Phone number is valid");
        } else {
            System.out.println("Phone number must have 10 digits.");
        }

        // Check if the credentials match saved ones
        if (validUsername && validPassword && validPhone) {
            if (usernameLogin.equals(savedUsername) && passwordLogin.equals(savedPassword) && phoneNumber.equals(savedPhoneNumber)) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid login credentials.");
            }
        } else {
            System.out.println("Some inputs are invalid.");
        }
    }
}
 