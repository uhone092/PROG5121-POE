/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.login;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ndivhuwo
 */
public class AccountValidatorTest {
    
    public AccountValidatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isValidIdentifier method, of class AccountValidator.
     */
    @Test
    public void testIsValidIdentifier() {
        System.out.println("isValidIdentifier");
        String identifier = "";
        AccountValidator instance = new AccountValidator();
        boolean expResult = false;
        boolean result = instance.isValidIdentifier(identifier);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidSecurityCode method, of class AccountValidator.
     */
    @Test
    public void testIsValidSecurityCode() {
        System.out.println("isValidSecurityCode");
        String securityCode = "";
        AccountValidator instance = new AccountValidator();
        boolean expResult = false;
        boolean result = instance.isValidSecurityCode(securityCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidContactNumber method, of class AccountValidator.
     */
    @Test
    public void testIsValidContactNumber() {
        System.out.println("isValidContactNumber");
        String contactNumber = "";
        AccountValidator instance = new AccountValidator();
        boolean expResult = false;
        boolean result = instance.isValidContactNumber(contactNumber);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
