/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.loggin;
import java.util.Scanner;

/**
 *
 * @author Student
 */
public class Loggin { 
    public static void main(String [] args ){
     Scanner userANDPass = new Scanner(System.in);

     // Put the register method 
     // validation of username
     System.out.println("Enter username");
     String usernameLogin = userANDPass.next();

     // validation of password

     System.out.println("Please enter your password");
     String passwordLogin = userANDPass.next();

     // validation of phonenumber

     System.out.println("Enter your phone number");
     String phoneNumber = userANDPass.next();

      // Add a boolean method
    
     boolean validUsername = usernameLogin.contains("_")
             && usernameLogin.length() >= 5 ;

     boolean validPassword = passwordLogin.length() >= 8 
             && passwordLogin.matches(".*[A-Za-z].*") 
             && passwordLogin.matches(".*[0-9].*");

     boolean validPhone = phoneNumber.length() == 10
             && phoneNumber.matches("^\\+27{0,10}$");

        // use if statement
             
     if(validUsername){
        System.out.println("Username is valid"); 
     } else  { 
          System.out.println(" Username must contain underscore and must contain at least 5 characters");
     } 
     
     if(validPassword){
        System.out.println("Password is valid");
     } else {
         System.out.println("Password must be at least 8 characters , include and numbers");
     }
     if(validPhone){
        System.out.println("Phone number is valid");
     } else {
        System.out.println("Phone number must have 10 digits");
     }
     if(validUsername && validPassword && validPhone){
        System.out.println("Login successful");
     } else {
        System.out.println("Some inputs are invalid");
     }
     
    }

    

}

