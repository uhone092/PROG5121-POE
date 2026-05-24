/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.login;

/**
 *
 * @author Student
 */
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Login {

    private static final Scanner input = new Scanner(System.in);
    private static final Map<String, AccountDetails> accountDatabase = new TreeMap<>();
    private static final List<MessageLog> chatHistory = new ArrayList<>();
    private static final List<Message> messagesList = new ArrayList<>();
    private static AccountDetails currentLoggedInUser = null;

    // Hidden frame to act as owner of dialog boxes
    private static final JFrame dialogParentFrame = createDialogParentFrame();

    private static JFrame createDialogParentFrame() {
        JFrame frame = new JFrame("Dialog Parent");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(0, 0);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setVisible(false);
        return frame;
    }

    public static void main(String[] args) {

        displayWelcomeBanner();

        boolean applicationRunning = true;

        while (applicationRunning) {

            displayMainMenu();
            String selection = input.nextLine().trim();

            switch (selection) {

                case "1":
                    createNewAccount();
                    break;

                case "2":
                    if (authenticateUser()) {
                        startQuickChatSession();
                    }
                    break;

                case "3":
                    applicationRunning = false;
                    System.out.println("\nExiting application. Thank you for using Login!");
                    break;

                default:
                    System.out.println("\n[ERROR] Invalid selection. Please try again.");
            }
        }

        dialogParentFrame.dispose();
    }

    private static void displayWelcomeBanner() {

        System.out.println("*************************************************");
        System.out.println("*                   LOGIN                       *");
        System.out.println("*************************************************");

        System.out.println("\nAccount Creation Requirements:");
        System.out.println("- Identifier must contain an underscore (_) and be 5 or fewer characters");
        System.out.println("- Security code must be at least 8 characters with one uppercase letter,");
        System.out.println("  one digit, and one special character");
        System.out.println("- Contact number must include country code (+) and maximum 10 digits");
        System.out.println("*************************************************");
    }

    private static void displayMainMenu() {

        System.out.println("\nMAIN MENU");
        System.out.println("1. Create Account");
        System.out.println("2. Access Account");
        System.out.println("3. Exit Application");

        System.out.print("\nSelect an option (1-3): ");
    }

    private static void createNewAccount() {

        System.out.println("\n--- ACCOUNT CREATION ---");

        System.out.print("Enter identifier: ");
        String identifier = input.nextLine().trim();

        System.out.print("Enter security code: ");
        String securityCode = input.nextLine().trim();

        System.out.print("Enter contact number (with country code): ");
        String contactNumber = input.nextLine().trim();

        System.out.print("Enter given name: ");
        String givenName = input.nextLine().trim();

        System.out.print("Enter family name: ");
        String familyName = input.nextLine().trim();

        AccountValidator validator = new AccountValidator();

        if (!validator.isValidIdentifier(identifier)) {

            System.out.println("\n[ERROR] Invalid identifier format.");
            return;
        }

        if (!validator.isValidSecurityCode(securityCode)) {

            System.out.println("\n[ERROR] Invalid security code.");
            return;
        }

        if (!validator.isValidContactNumber(contactNumber)) {

            System.out.println("\n[ERROR] Invalid contact number.");
            return;
        }

        if (accountDatabase.containsKey(identifier)) {

            System.out.println("\n[ERROR] Identifier already exists.");
            return;
        }

        AccountDetails newAccount = new AccountDetails(
                identifier,
                securityCode,
                contactNumber,
                givenName,
                familyName
        );

        accountDatabase.put(identifier, newAccount);

        System.out.println("\n[SUCCESS] Account created successfully!");
    }

    private static boolean authenticateUser() {

        System.out.println("\n--- ACCOUNT ACCESS ---");

        System.out.print("Enter identifier: ");
        String identifier = input.nextLine().trim();

        System.out.print("Enter security code: ");
        String securityCode = input.nextLine().trim();

        AccountDetails account = accountDatabase.get(identifier);

        if (account != null &&
                account.verifyCredentials(identifier, securityCode)) {

            System.out.println("\n[SUCCESS] Authentication successful!");
            System.out.println("Welcome back, "
                    + account.getGivenName()
                    + " "
                    + account.getFamilyName()
                    + "!");

            currentLoggedInUser = account;

            return true;

        } else {

            System.out.println("\n[ERROR] Invalid login details.");
            return false;
        }
    }

    private static void startQuickChatSession() {

        System.out.println("\n=== Welcome to QuickChat ===");

        System.out.print("How many messages would you like to send? ");

        int maxMessages;

        try {

            maxMessages = Integer.parseInt(input.nextLine().trim());

            if (maxMessages <= 0) {

                System.out.println("[ERROR] Enter a positive number.");
                return;
            }

        } catch (NumberFormatException e) {

            System.out.println("[ERROR] Invalid number.");
            return;
        }

        boolean sessionActive = true;

        while (sessionActive) {

            displayQuickChatMenu();

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":

                    if (Message.getTotalMessagesSent() >= maxMessages) {

                        System.out.println("\n[INFO] Maximum messages reached.");

                    } else {

                        sendMessage(maxMessages);
                    }

                    break;

                case "2":

                    showRecentMessages();
                    break;

                case "3":

                    sessionActive = false;

                    System.out.println("\nExiting QuickChat...");
                    System.out.println("Total messages sent: "
                            + Message.getTotalMessagesSent());

                    currentLoggedInUser = null;

                    break;

                default:

                    System.out.println("\n[ERROR] Invalid option.");
            }
        }
    }

    private static void displayQuickChatMenu() {

        System.out.println("\n--- QUICKCHAT MENU ---");
        System.out.println("1) Send messages");
        System.out.println("2) Show recently sent messages");
        System.out.println("3) Quit");

        System.out.print("\nSelect an option (1-3): ");
    }

    private static void sendMessage(int maxMessages) {

        if (Message.getTotalMessagesSent() >= maxMessages) {

            System.out.println("\n[INFO] Maximum messages reached.");
            return;
        }

        System.out.println("\n--- SEND MESSAGE ---");

        System.out.print("Enter recipient cell number: ");
        String recipient = input.nextLine().trim();

        System.out.print("Enter your message (max 250 characters): ");
        String messageContent = input.nextLine().trim();

        Message newMessage = new Message(recipient, messageContent);

        if (!newMessage.checkMessageID()) {

            System.out.println("\n[ERROR] Invalid message ID.");
            return;
        }

        int recipientValidation = newMessage.checkRecipientCell();

        if (recipientValidation != 1) {

            if (recipientValidation == 0) {

                System.out.println("\n[ERROR] Recipient number too long.");

            } else {

                System.out.println("\n[ERROR] Number must start with +");
            }

            return;
        }

        if (messageContent.length() > 250) {

            System.out.println("\n[ERROR] Message exceeds 250 characters.");
            return;
        }

        System.out.println("\n[SUCCESS] Message captured.");

        System.out.println("\n1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");

        System.out.print("Choose an option: ");

        String action = input.nextLine().trim();

        switch (action) {

            case "1":

                messagesList.add(newMessage);

                Message.incrementTotalMessages();

                displayMessageDetails(newMessage);

                break;

            case "2":

                System.out.println("\n[INFO] Message discarded.");
                break;

            case "3":

                newMessage.storeMessage();

                messagesList.add(newMessage);

                Message.incrementTotalMessages();

                System.out.println("\n[INFO] Message stored.");

                break;

            default:

                System.out.println("\n[ERROR] Invalid option.");
        }
    }

    private static void displayMessageDetails(Message message) {

        String dialogMessage = String.format(
                "Message ID: %s\n\n" +
                "Message Hash: %s\n\n" +
                "Recipient: %s\n\n" +
                "Message: %s",

                message.getMessageID(),
                message.getMessageHash(),
                message.getRecipient(),
                message.getMessageContent()
        );

        try {

            JOptionPane.showMessageDialog(
                    dialogParentFrame,
                    dialogMessage,
                    "Message Details",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {

            System.out.println(dialogMessage);
        }
    }

    private static void showRecentMessages() {

        if (messagesList.isEmpty()) {

            System.out.println("\n[INFO] No messages available.");
            return;
        }

        System.out.println("\n--- RECENT MESSAGES ---");

        for (Message msg : messagesList) {

            System.out.println("--------------------------------");
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("Message: " + msg.getMessageContent());
            System.out.println("Hash: " + msg.getMessageHash());
        }
    }
}

// ==================== ACCOUNT DETAILS CLASS ====================

class AccountDetails {

    private final String identifier;
    private final String securityCode;
    private final String contactNumber;
    private final String givenName;
    private final String familyName;
    private final LocalDateTime creationDate;

    public AccountDetails(
            String identifier,
            String securityCode,
            String contactNumber,
            String givenName,
            String familyName
    ) {

        this.identifier = identifier;
        this.securityCode = securityCode;
        this.contactNumber = contactNumber;
        this.givenName = givenName;
        this.familyName = familyName;
        this.creationDate = LocalDateTime.now();
    }

    public boolean verifyCredentials(
            String identifier,
            String securityCode
    ) {

        return this.identifier.equals(identifier)
                && this.securityCode.equals(securityCode);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}

// ==================== ACCOUNT VALIDATOR CLASS ====================

class AccountValidator {

    public boolean isValidIdentifier(String identifier) {

        return identifier != null
                && identifier.contains("_")
                && identifier.length() <= 5;
    }

    public boolean isValidSecurityCode(String securityCode) {

        if (securityCode == null || securityCode.length() < 8) {

            return false;
        }

        boolean hasUppercase =
                !securityCode.equals(securityCode.toLowerCase());

        boolean hasDigit =
                securityCode.matches(".*\\d.*");

        boolean hasSpecial =
                securityCode.matches(".*[^A-Za-z0-9].*");

        return hasUppercase && hasDigit && hasSpecial;
    }

    public boolean isValidContactNumber(String contactNumber) {

        if (contactNumber == null
                || !contactNumber.startsWith("+")) {

            return false;
        }

        String withoutPlus = contactNumber.substring(1);

        if (!withoutPlus.matches("\\d+")) {

            return false;
        }

        return withoutPlus.length() <= 11;
    }
}

// ==================== MESSAGE LOG CLASS ====================

class MessageLog {

    private final String sender;
    private final String content;
    private final LocalDateTime timestamp;

    public MessageLog(String sender, String content) {

        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedMessage() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        return "[" + timestamp.format(formatter)
                + "] "
                + sender
                + ": "
                + content;
    }
}

// ==================== MESSAGE CLASS ====================

class Message {

    private final String messageID;

    private static int numMessagesSent = 0;

    private final String recipient;
    private final String messageContent;
    private final String messageHash;

    private static int totalMessagesSent = 0;

    public Message(String recipient, String messageContent) {

        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageContent = messageContent;
        this.messageHash = createMessageHash();

        numMessagesSent++;
    }

    private String generateMessageID() {

        Random random = new Random();

        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 10; i++) {

            id.append(random.nextInt(10));
        }

        return id.toString();
    }

    public boolean checkMessageID() {

        return messageID != null
                && messageID.length() <= 11;
    }

    public int checkRecipientCell() {

        if (recipient == null) {

            return -1;
        }

        if (!recipient.startsWith("+")) {

            return -1;
        }

        String digits = recipient.substring(1);

        if (digits.length() > 11) {

            return 0;
        }

        if (!digits.matches("\\d+")) {

            return -1;
        }

        return 1;
    }

    public String createMessageHash() {

        if (messageContent == null
                || messageContent.trim().isEmpty()) {

            return messageID.substring(0, 2)
                    + ":"
                    + numMessagesSent
                    + ":EMPTY";
        }

        String[] words = messageContent.trim().split("\\s+");

        String firstWord = words[0].toUpperCase();

        String lastWord =
                words[words.length - 1].toUpperCase();

        return messageID.substring(0, 2)
                + ":"
                + numMessagesSent
                + ":"
                + firstWord
                + lastWord;
    }

    public void storeMessage() {

        try {

            StringBuilder json = new StringBuilder();

            json.append("{\n");
            json.append("  \"messageID\": \"")
                    .append(this.messageID)
                    .append("\",\n");

            json.append("  \"messageHash\": \"")
                    .append(this.messageHash)
                    .append("\",\n");

            json.append("  \"recipient\": \"")
                    .append(this.recipient)
                    .append("\",\n");

            json.append("  \"messageContent\": \"")
                    .append(this.messageContent)
                    .append("\",\n");

            json.append("  \"numMessagesSent\": ")
                    .append(numMessagesSent)
                    .append(",\n");

            json.append("  \"timestamp\": \"")
                    .append(LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd HH:mm:ss")))
                    .append("\"\n");

            json.append("}");

            String fileName =
                    "stored_message_"
                            + this.messageID
                            + ".json";

            File file = new File(fileName);

            try (BufferedWriter writer =
                         new BufferedWriter(
                                 new FileWriter(file))) {

                writer.write(json.toString());
            }

            System.out.println(
                    "Message stored in JSON file: "
                            + fileName);

        } catch (IOException e) {

            System.out.println(
                    "[ERROR] Failed to store message: "
                            + e.getMessage());
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    public static void resetTotalMessagesSent() {
        totalMessagesSent = 0;
    }

    public static void incrementTotalMessages() {
        totalMessagesSent++;
    }
}