/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.login;

import java.time.LocalDateTime;
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
public class AccountDetailsTest {
    
    public AccountDetailsTest() {
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
     * Test of verifyCredentials method, of class AccountDetails.
     */
    @Test
    public void testVerifyCredentials() {
        System.out.println("verifyCredentials");
        String identifier = "";
        String securityCode = "";
        AccountDetails instance = null;
        boolean expResult = false;
        boolean result = instance.verifyCredentials(identifier, securityCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIdentifier method, of class AccountDetails.
     */
    @Test
    public void testGetIdentifier() {
        System.out.println("getIdentifier");
        AccountDetails instance = null;
        String expResult = "";
        String result = instance.getIdentifier();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContactNumber method, of class AccountDetails.
     */
    @Test
    public void testGetContactNumber() {
        System.out.println("getContactNumber");
        AccountDetails instance = null;
        String expResult = "";
        String result = instance.getContactNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGivenName method, of class AccountDetails.
     */
    @Test
    public void testGetGivenName() {
        System.out.println("getGivenName");
        AccountDetails instance = null;
        String expResult = "";
        String result = instance.getGivenName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFamilyName method, of class AccountDetails.
     */
    @Test
    public void testGetFamilyName() {
        System.out.println("getFamilyName");
        AccountDetails instance = null;
        String expResult = "";
        String result = instance.getFamilyName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCreationDate method, of class AccountDetails.
     */
    @Test
    public void testGetCreationDate() {
        System.out.println("getCreationDate");
        AccountDetails instance = null;
        LocalDateTime expResult = null;
        LocalDateTime result = instance.getCreationDate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
