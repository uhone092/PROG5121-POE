/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.login;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Uhone
 */
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LoginTest {

    private List<Message> sentMessages;
    private List<Message> storedMessages;

    private Message testMessage1;
    private Message testMessage2;
    private Message testMessage3;
    private Message testMessage4;
    private Message testMessage5;

    @Before
    public void setUp() {
        Message.resetTotalMessagesSent();

        sentMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();

        testMessage1 = new Message("+27123456789", "Hello World");

        testMessage2 = new Message(
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time."
        );

        testMessage3 = new Message(
                "+27838884567",
                "Ok, I am leaving without you."
        );

        testMessage4 = new Message(
                "+27838884567",
                "It is dinner time!"
        );

        testMessage5 = new Message(
                "+27987654321",
                "Meeting at 3 PM today"
        );

        sentMessages.add(testMessage1);
        sentMessages.add(testMessage2);
        sentMessages.add(testMessage3);
        sentMessages.add(testMessage4);
        sentMessages.add(testMessage5);

        storedMessages.add(testMessage2);
        storedMessages.add(testMessage4);
    }

    @After
    public void tearDown() {
        Message.resetTotalMessagesSent();
        sentMessages.clear();
        storedMessages.clear();
    }

    private String findLongestMessage(List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return null;
        }

        Message longest = messages.get(0);

        for (Message message : messages) {
            if (message.getMessageContent().length()
                    > longest.getMessageContent().length()) {
                longest = message;
            }
        }

        return longest.getMessageContent();
    }

    private String searchMessageByID(List<Message> messages,
                                     String messageID) {

        for (Message message : messages) {
            if (message.getMessageID().equals(messageID)) {
                return message.getMessageContent();
            }
        }

        return null;
    }

    private List<String> searchMessagesByRecipient(
            List<Message> messages,
            String recipient) {

        List<String> foundMessages = new ArrayList<>();

        for (Message message : messages) {
            if (message.getRecipient().equals(recipient)) {
                foundMessages.add(message.getMessageContent());
            }
        }

        return foundMessages;
    }

    private String deleteMessageByHash(
            List<Message> messages,
            String messageHash) {

        for (int i = 0; i < messages.size(); i++) {

            Message message = messages.get(i);

            if (message.getMessageHash().equals(messageHash)) {

                String deletedContent =
                        message.getMessageContent();

                messages.remove(i);

                return "Message \"" + deletedContent
                        + "\" successfully deleted.";
            }
        }

        return "Message not found for deletion.";
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {

        assertEquals(5, sentMessages.size());

        assertEquals(
                "Hello World",
                sentMessages.get(0).getMessageContent()
        );

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                sentMessages.get(1).getMessageContent()
        );

        assertEquals(
                "Ok, I am leaving without you.",
                sentMessages.get(2).getMessageContent()
        );

        assertEquals(
                "It is dinner time!",
                sentMessages.get(3).getMessageContent()
        );

        assertEquals(
                "Meeting at 3 PM today",
                sentMessages.get(4).getMessageContent()
        );
    }

    @Test
    public void testDisplayLongestMessage() {

        List<Message> messages1to4 =
                sentMessages.subList(0, 4);

        String longestMessage =
                findLongestMessage(messages1to4);

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                longestMessage
        );
    }

    @Test
    public void testSearchForMessageID() {

        String message4ID =
                testMessage4.getMessageID();

        String foundMessage =
                searchMessageByID(sentMessages, message4ID);

        assertEquals(
                "It is dinner time!",
                foundMessage
        );
    }

    @Test
    public void testSearchAllMessagesByRecipient() {

        List<String> foundMessages =
                searchMessagesByRecipient(
                        sentMessages,
                        "+27838884567"
                );

        assertEquals(3, foundMessages.size());

        assertTrue(
                foundMessages.contains(
                        "Where are you? You are late! I have asked you to be on time."
                )
        );

        assertTrue(
                foundMessages.contains(
                        "Ok, I am leaving without you."
                )
        );

        assertTrue(
                foundMessages.contains(
                        "It is dinner time!"
                )
        );
    }

    @Test
    public void testDeleteMessageByHash() {

        String hash =
                testMessage2.getMessageHash();

        List<Message> copy =
                new ArrayList<>(sentMessages);

        String result =
                deleteMessageByHash(copy, hash);

        assertEquals(
                "Message \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                result
        );

        assertEquals(4, copy.size());
    }

    @Test
    public void testCompleteIntegration() {

        assertEquals(5, sentMessages.size());

        String longest =
                findLongestMessage(sentMessages);

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                longest
        );

        List<String> recipientMessages =
                searchMessagesByRecipient(
                        sentMessages,
                        "+27838884567"
                );

        assertEquals(3, recipientMessages.size());

        String message4ID =
                testMessage4.getMessageID();

        assertEquals(
                "It is dinner time!",
                searchMessageByID(
                        sentMessages,
                        message4ID
                )
        );

        String deleteResult =
                deleteMessageByHash(
                        new ArrayList<>(sentMessages),
                        testMessage2.getMessageHash()
                );

        assertTrue(
                deleteResult.contains(
                        "successfully deleted"
                )
        );
    }
}
