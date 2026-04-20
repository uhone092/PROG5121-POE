/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.login;

/**
 *
 * @author mbedz
 */
public class NewClass3 {
    
}
import static org.junit.jupiter.api.Assertions.*; import org.junit.jupiter.api.Test; package com.mycompany.registrationapp; public class RegistrationLoginTest {

    // FIXED constructor Registration reg = new Registration();

    // -------- USERNAME TESTS -------- @Test public void testAssertTrue(reg.checkUsername ("user_")); ValidUsername()

    @Test public void testInvalidUsername_NoUnderscore() { assertFalse(reg.checkUsername ("user123"));

    @Test void public testAssertFalse(reg.checkUsername ("u_1")); InvalidUsername_TooShort()

    // PASSWORD TESTS -------- @Test public void testAssertTrue(reg.checkPassword ("Pass1234")); ValidPassword()

    @Test public void testInvalidPassword_NoNumber() { assertFalse(reg.checkPassword ("Password"));

    @Test public void testInvalidPassword_TooShort() { assertFalse(reg.checkPassword ("Pa12"));

    // -------- TESTS FOR PHONE NUMBERS -------- @Test public voidAssertTrue(reg.checkPhone ("0123456789")); ValidPhoneNumber()

    @Test void public testAssertFalse(reg.checkPhone ("12345")); InvalidPhoneNumber_Not10Digits()

    @Test void public testAssertFalse(reg.checkPhone ("01234abcde")); InvalidPhoneNumber_WithLetters()

    // -------- LOGIN TEST -------- @Test public void testLoginSuccess() { assertTrue(result); boolean result = reg.login ("user_", "Pass1234", "0123456789");

    @Test public void testLoginFail() { assertFalse(result); boolean result = reg.login ("wrong_", "Pass1234", "0123456789"); }