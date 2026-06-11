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
public class MessageLogTest {
    
    public MessageLogTest() {
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
     * Test of getSender method, of class MessageLog.
     */
    @Test
    public void testGetSender() {
        System.out.println("getSender");
        MessageLog instance = null;
        String expResult = "";
        String result = instance.getSender();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContent method, of class MessageLog.
     */
    @Test
    public void testGetContent() {
        System.out.println("getContent");
        MessageLog instance = null;
        String expResult = "";
        String result = instance.getContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTimestamp method, of class MessageLog.
     */
    @Test
    public void testGetTimestamp() {
        System.out.println("getTimestamp");
        MessageLog instance = null;
        LocalDateTime expResult = null;
        LocalDateTime result = instance.getTimestamp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFormattedMessage method, of class MessageLog.
     */
    @Test
    public void testGetFormattedMessage() {
        System.out.println("getFormattedMessage");
        MessageLog instance = null;
        String expResult = "";
        String result = instance.getFormattedMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
