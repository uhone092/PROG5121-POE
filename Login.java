/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.login;

/**
 *
 * @author Uhone
 */
import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Type;

public class Login {
    private static final Scanner input = new Scanner(System.in);
    private static final Map<String, AccountDetails> accountDatabase = new TreeMap<>();
    private static final List<MessageLog> chatHistory = new ArrayList<>();
    private static final List<Message> messagesList = new ArrayList<>();
    private static AccountDetails currentLoggedInUser = null;
    
    // Array Collections for Message Management
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages = new ArrayList<>();
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIDs = new ArrayList<>();
    
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
        populateTestData(); // Add test data

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
                    showArrayManagementMenu();
                    break;
                case "4":
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
        System.out.println("*");
        System.out.println("*          WELCOME TO LOGIN          *");
        System.out.println("*");
        System.out.println("\nAccount Creation Requirements:");
        System.out.println("- Identifier must contain an underscore (_) and be 5 or fewer characters");
        System.out.println("- Security code must be at least 8 characters with one uppercase letter,");
        System.out.println("  one digit, and one special character");
        System.out.println("- Contact number must include country code (+) and maximum 10 digits");
        System.out.println("*");
    }

    private static void displayMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("1. Create Account");
        System.out.println("2. Access Account");
        System.out.println("3. Message Management");
        System.out.println("4. Exit Application");
        System.out.print("\nSelect an option (1-4): ");
    }

    // ================= ARRAY MANAGEMENT METHODS =================
    
    private static void showArrayManagementMenu() {
        boolean managementActive = true;
        while (managementActive) {
            System.out.println("\n--- MESSAGE MANAGEMENT MENU ---");
            System.out.println("1. Display Sender and Recipient of All Sent Messages");
            System.out.println("2. Display Longest Sent Message");
            System.out.println("3. Search for Message by ID");
            System.out.println("4. Search Messages by Recipient");
            System.out.println("5. Delete Message by Hash");
            System.out.println("6. Display Full Message Report");
            System.out.println("7. Show Array Contents");
            System.out.println("8. Load Stored Messages from JSON");
            System.out.println("9. Back to Main Menu");
            System.out.print("\nSelect an option (1-9): ");
            
            String choice = input.nextLine().trim();
            
            switch (choice) {
                case "1":
                    displaySenderRecipientOfSentMessages();
                    break;
                case "2":
                    displayLongestSentMessage();
                    break;
                case "3":
                    searchMessageByID();
                    break;
                case "4":
                    searchMessagesByRecipient();
                    break;
                case "5":
                    deleteMessageByHash();
                    break;
                case "6":
                    displayFullMessageReport();
                    break;
                case "7":
                    showArrayContents();
                    break;
                case "8":
                    loadStoredMessagesFromJSON();
                    break;
                case "9":
                    managementActive = false;
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid option. Please choose 1-9.");
            }
        }
    }

    public static void displaySenderRecipientOfSentMessages() {
        System.out.println("\n--- SENT MESSAGES (SENDER AND RECIPIENT) ---");
        if (sentMessages.isEmpty()) {
            System.out.println("No sent messages found.");
            return;
        }
        
        for (Message msg : sentMessages) {
            System.out.println("Sender: " + (currentLoggedInUser != null ? currentLoggedInUser.getGivenName() : "System") + 
                             " | Recipient: " + msg.getRecipient());
        }
    }

    public static String displayLongestSentMessage() {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }
        
        Message longestMessage = sentMessages.get(0);
        for (Message msg : sentMessages) {
            if (msg.getMessageContent().length() > longestMessage.getMessageContent().length()) {
                longestMessage = msg;
            }
        }
        
        String result = longestMessage.getMessageContent();
        System.out.println("\n--- LONGEST SENT MESSAGE ---");
        System.out.println("Message: " + result);
        System.out.println("Length: " + result.length() + " characters");
        return result;
    }

    private static void searchMessageByID() {
        System.out.print("\nEnter Message ID to search: ");
        String searchID = input.nextLine().trim();
        
        String result = searchMessageByID(searchID);
        System.out.println(result);
    }

    public static String searchMessageByID(String messageID) {
        // Search in all message arrays
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(disregardedMessages);
        allMessages.addAll(storedMessages);
        
        for (Message msg : allMessages) {
            if (msg.getMessageID().equals(messageID)) {
                return msg.getMessageContent();
            }
        }
        return "Message ID not found.";
    }

    private static void searchMessagesByRecipient() {
        System.out.print("\nEnter recipient number to search: ");
        String recipient = input.nextLine().trim();
        
        List<String> results = searchMessagesByRecipient(recipient);
        
        System.out.println("\n--- MESSAGES FOR RECIPIENT: " + recipient + " ---");
        if (results.isEmpty()) {
            System.out.println("No messages found for this recipient.");
        } else {
            for (String message : results) {
                System.out.println("\"" + message + "\"");
            }
        }
    }

    public static List<String> searchMessagesByRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        
        // Search in sent and stored messages
        List<Message> searchableMessages = new ArrayList<>();
        searchableMessages.addAll(sentMessages);
        searchableMessages.addAll(storedMessages);
        
        for (Message msg : searchableMessages) {
            if (msg.getRecipient().equals(recipient)) {
                results.add(msg.getMessageContent());
            }
        }
        
        return results;
    }

    private static void deleteMessageByHash() {
        System.out.print("\nEnter Message Hash to delete: ");
        String hash = input.nextLine().trim();
        
        String result = deleteMessageByHash(hash);
        System.out.println(result);
    }

    public static String deleteMessageByHash(String messageHash) {
        // Search and delete from sent messages
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(messageHash)) {
                Message deletedMessage = sentMessages.remove(i);
                messageHashes.remove(messageHash);
                messageIDs.remove(deletedMessage.getMessageID());
                return "Message \"" + deletedMessage.getMessageContent() + "\" successfully deleted.";
            }
        }
        
        // Search and delete from stored messages
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(messageHash)) {
                Message deletedMessage = storedMessages.remove(i);
                messageHashes.remove(messageHash);
                messageIDs.remove(deletedMessage.getMessageID());
                return "Message \"" + deletedMessage.getMessageContent() + "\" successfully deleted.";
            }
        }
        
        return "Message hash not found.";
    }

    public static void displayFullMessageReport() {
        System.out.println("\n=== FULL MESSAGE REPORT ===");
        System.out.println("SENT MESSAGES:");
        System.out.println("=" + "=".repeat(80));
        
        if (sentMessages.isEmpty()) {
            System.out.println("No sent messages found.");
        } else {
            for (Message msg : sentMessages) {
                System.out.println("Message Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageContent());
                System.out.println("-".repeat(50));
            }
        }
        
        System.out.println("\nTotal Sent Messages: " + sentMessages.size());
        System.out.println("Total Stored Messages: " + storedMessages.size());
        System.out.println("Total Disregarded Messages: " + disregardedMessages.size());
    }

    private static void showArrayContents() {
        System.out.println("\n=== ARRAY CONTENTS ===");
        System.out.println("Sent Messages: " + sentMessages.size());
        System.out.println("Disregarded Messages: " + disregardedMessages.size());
        System.out.println("Stored Messages: " + storedMessages.size());
        System.out.println("Message Hashes: " + messageHashes.size());
        System.out.println("Message IDs: " + messageIDs.size());
    }

    private static void loadStoredMessagesFromJSON() {
        System.out.println("\n--- LOADING STORED MESSAGES FROM JSON ---");
        File currentDir = new File(".");
        File[] jsonFiles = currentDir.listFiles((dir, name) -> name.startsWith("stored_message_") && name.endsWith(".json"));
        
        if (jsonFiles == null || jsonFiles.length == 0) {
            System.out.println("No JSON files found.");
            return;
        }
        
        int loadedCount = 0;
        for (File jsonFile : jsonFiles) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
                StringBuilder jsonContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
                reader.close();
                
                // Parse JSON manually (simple approach)
                String json = jsonContent.toString();
                String messageID = extractJsonValue(json, "messageID");
                String recipient = extractJsonValue(json, "recipient");
                String messageContent = extractJsonValue(json, "messageContent");
                
                Message storedMsg = new Message(recipient, messageContent);
                if (!storedMessages.contains(storedMsg)) {
                    storedMessages.add(storedMsg);
                    messageHashes.add(storedMsg.getMessageHash());
                    messageIDs.add(storedMsg.getMessageID());
                    loadedCount++;
                }
                
            } catch (IOException e) {
                System.out.println("Error reading file: " + jsonFile.getName());
            }
        }
        
        System.out.println("Loaded " + loadedCount + " messages from JSON files.");
    }

    private static String extractJsonValue(String json, String key) {
        String searchPattern = "\"" + key + "\": \"";
        int startIndex = json.indexOf(searchPattern);
        if (startIndex == -1) return "";
        
        startIndex += searchPattern.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) return "";
        
        return json.substring(startIndex, endIndex);
    }

    // ================= TEST DATA POPULATION =================
    
    public static void populateTestData() {
        // Clear existing data
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();

        // Test Data Message 1 - Sent
        Message msg1 = new Message("+27834557896", "Did you get the cake?");
        addToSentMessages(msg1);

        // Test Data Message 2 - Stored
        Message msg2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.");
        addToStoredMessages(msg2);

        // Test Data Message 3 - Disregarded
        Message msg3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        addToDisregardedMessages(msg3);

        // Test Data Message 4 - Sent
        Message msg4 = new Message("0838884567", "It is dinner time !");
        addToSentMessages(msg4);

        // Test Data Message 5 - Stored
        Message msg5 = new Message("+27838884567", "Ok, I am leaving without you.");
        addToStoredMessages(msg5);
    }

    public static void addToSentMessages(Message message) {
        sentMessages.add(message);
        messageHashes.add(message.getMessageHash());
        messageIDs.add(message.getMessageID());
    }

    public static void addToDisregardedMessages(Message message) {
        disregardedMessages.add(message);
        messageHashes.add(message.getMessageHash());
        messageIDs.add(message.getMessageID());
    }

    public static void addToStoredMessages(Message message) {
        storedMessages.add(message);
        messageHashes.add(message.getMessageHash());
        messageIDs.add(message.getMessageID());
    }

    // ================= GETTERS FOR TESTING =================
    
    public static ArrayList<Message> getSentMessages() {
        return sentMessages;
    }

    public static ArrayList<Message> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static ArrayList<Message> getStoredMessages() {
        return storedMessages;
    }

    public static ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    public static ArrayList<String> getMessageIDs() {
        return messageIDs;
    }

    // ================= EXISTING METHODS (UNCHANGED) =================
    
    private static void createNewAccount() {
        System.out.println("\n--- ACCOUNT CREATION ---");

        System.out.print("Enter identifier: ");
        String identifier = input.nextLine().trim();

        System.out.print("Enter security code: ");
        String securityCode = input.nextLine().trim();

        System.out.print("Enter contact number (with country code, e.g., +12025550179): ");
        String contactNumber = input.nextLine().trim();

        System.out.print("Enter given name: ");
        String givenName = input.nextLine().trim();

        System.out.print("Enter family name: ");
        String familyName = input.nextLine().trim();

        AccountValidator validator = new AccountValidator();

        if (!validator.isValidIdentifier(identifier)) {
            System.out.println("\n[ERROR] Invalid identifier format. Must contain an underscore and be 5 or fewer characters.");
            return;
        }

        if (!validator.isValidSecurityCode(securityCode)) {
            System.out.println("\n[ERROR] Invalid security code. Must be at least 8 characters with one uppercase letter, one digit, and one special character.");
            return;
        }

        if (!validator.isValidContactNumber(contactNumber)) {
            System.out.println("\n[ERROR] Invalid contact number. Must include country code (+) and be 10 or fewer digits.");
            return;
        }

        if (accountDatabase.containsKey(identifier)) {
            System.out.println("\n[ERROR] This identifier is already in use. Please choose another one.");
            return;
        }

        AccountDetails newAccount = new AccountDetails(identifier, securityCode, contactNumber, givenName, familyName);
        accountDatabase.put(identifier, newAccount);

        System.out.println("\n[SUCCESS] Account created successfully!");
        System.out.println("- Identifier registered: " + identifier);
        System.out.println("- Security code set successfully");
        System.out.println("- Contact number registered: " + contactNumber);
    }

    private static boolean authenticateUser() {
        System.out.println("\n--- ACCOUNT ACCESS ---");

        System.out.print("Enter identifier: ");
        String identifier = input.nextLine().trim();

        System.out.print("Enter security code: ");
        String securityCode = input.nextLine().trim();

        AccountDetails account = accountDatabase.get(identifier);

        if (account != null && account.verifyCredentials(identifier, securityCode)) {
            System.out.println("\n[SUCCESS] Authentication successful!");
            System.out.println("Welcome back, " + account.getGivenName() + " " + account.getFamilyName() + "!");
            currentLoggedInUser = account;
            return true;
        } else {
            System.out.println("\n[ERROR] Authentication failed. Invalid identifier or security code.");
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
                System.out.println("[ERROR] Please enter a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Please enter a valid number.");
            return;
        }

        boolean sessionActive = true;
        while (sessionActive) {
            displayQuickChatMenu();
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    if (Message.getTotalMessagesSent() >= maxMessages) {
                        System.out.println("\n[INFO] You have reached your maximum number of messages (" + maxMessages + ").");
                        System.out.println("Total messages sent: " + Message.getTotalMessagesSent());
                    } else {
                        sendMessage(maxMessages);
                    }
                    break;
                case "2":
                    showRecentlySentMessages();
                    break;
                case "3":
                    sessionActive = false;
                    System.out.println("\nExiting QuickChat...");
                    System.out.println("Total messages sent in this session: " + Message.getTotalMessagesSent());
                    currentLoggedInUser = null;
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }

    private static void showRecentlySentMessages() {
        System.out.println("\n--- RECENTLY SENT MESSAGES ---");
        
        if (sentMessages.isEmpty()) {
            System.out.println("No messages have been sent yet.");
            return;
        }
        
        System.out.println("Total messages sent: " + sentMessages.size());
        System.out.println("=" + "=".repeat(50));
        
        // Show all sent messages
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            System.out.println("Message " + (i + 1) + ":");
            System.out.println("  To: " + msg.getRecipient());
            System.out.println("  Content: " + msg.getMessageContent());
            System.out.println("  Message ID: " + msg.getMessageID());
            System.out.println("  Hash: " + msg.getMessageHash());
            System.out.println("-".repeat(40));
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
            System.out.println("\n[INFO] Maximum number of messages reached.");
            return;
        }

        System.out.println("\n--- SEND MESSAGE ---");

        System.out.print("Enter recipient cell number (with country code, max 10 digits): ");
        String recipient = input.nextLine().trim();

        System.out.print("Enter your message (max 250 characters): ");
        String messageContent = input.nextLine().trim();

        Message newMessage = new Message(recipient, messageContent);

        if (!newMessage.checkMessageID()) {
            System.out.println("\n[ERROR] Message ID validation failed.");
            return;
        }

        int recipientValidation = newMessage.checkRecipientCell();
        if (recipientValidation != 1) {
            if (recipientValidation == 0) {
                System.out.println("\n[ERROR] Recipient cell number is too long (max 10 characters).");
            } else {
                System.out.println("\n[ERROR] Recipient cell number must start with international code (+).");
            }
            return;
        }

        if (messageContent.length() > 250) {
            System.out.println("\n[ERROR] Please enter a message of less than 250 characters");
            return;
        }

        if (messageContent.length() > 50) {
            System.out.println("\n[ERROR] Please enter a message of less than 50 characters");
            return;
        }

        System.out.println("\n[SUCCESS] Message sent");

        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        System.out.print("Choose an option (1-3): ");
        String action = input.nextLine().trim();

        switch (action) {
            case "1":
                messagesList.add(newMessage);
                addToSentMessages(newMessage);
                Message.incrementTotalMessages();
                displayMessageDetails(newMessage);
                break;
            case "3":
                newMessage.storeMessage();
                messagesList.add(newMessage);
                addToStoredMessages(newMessage);
                Message.incrementTotalMessages();
                System.out.println("\n[INFO] Message stored for later sending.");
                break;
            case "2":
                addToDisregardedMessages(newMessage);
                System.out.println("\n[INFO] Message discarded.");
                break;
            default:
                System.out.println("[ERROR] Invalid choice. Defaulting to 'send'.");
                messagesList.add(newMessage);
                addToSentMessages(newMessage);
                Message.incrementTotalMessages();
                displayMessageDetails(newMessage);
                break;
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
            JOptionPane.showMessageDialog(dialogParentFrame, dialogMessage,
                    "Message Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println("[ERROR] Could not display dialog: " + e.getMessage());
            System.out.println("[INFO] Message details:");
            System.out.println(dialogMessage);
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

    public AccountDetails(String identifier, String securityCode, String contactNumber,
                          String givenName, String familyName) {
        this.identifier = identifier;
        this.securityCode = securityCode;
        this.contactNumber = contactNumber;
        this.givenName = givenName;
        this.familyName = familyName;
        this.creationDate = LocalDateTime.now();
    }

    public boolean verifyCredentials(String identifier, String securityCode) {
        return this.identifier.equals(identifier) && this.securityCode.equals(securityCode);
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
        return identifier != null && identifier.contains("_") && identifier.length() <= 5;
    }

    public boolean isValidSecurityCode(String securityCode) {
        if (securityCode == null || securityCode.length() < 8) {
            return false;
        }

        boolean hasUppercase = !securityCode.equals(securityCode.toLowerCase());
        boolean hasDigit = securityCode.matches(".*\\d.*");
        boolean hasSpecial = securityCode.matches(".*[^A-Za-z0-9].*");

        return hasUppercase && hasDigit && hasSpecial;
    }

    public boolean isValidContactNumber(String contactNumber) {
        if (contactNumber == null || !contactNumber.startsWith("+")) {
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "[" + timestamp.format(formatter) + "] " + sender + ": " + content;
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

    // FIXED METHOD - This was causing the storage issue
    static void incrementTotalMessages() {
        totalMessagesSent++;
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
        return messageID != null && messageID.length() <= 10;
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
        if (messageContent == null || messageContent.trim().isEmpty()) {
            return messageID.substring(0, 2) + ":" + numMessagesSent + ":EMPTY";
        }
        String[] words = messageContent.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();
        return messageID.substring(0, 2) + ":" + numMessagesSent + ":" + firstWord + lastWord;
    }

    public void storeMessage() {
        try {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"messageID\": \"").append(this.messageID).append("\",\n");
            json.append("  \"messageHash\": \"").append(this.messageHash).append("\",\n");
            json.append("  \"recipient\": \"").append(this.recipient).append("\",\n");
            json.append("  \"messageContent\": \"").append(this.messageContent).append("\",\n");
            json.append("  \"numMessagesSent\": ").append(numMessagesSent).append(",\n");
            json.append("  \"timestamp\": \"").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\"\n");
            json.append("}");
            
            String fileName = "stored_message_" + this.messageID + ".json";
            File file = new File(fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(json.toString());
            }
            System.out.println("Message stored in JSON file: " + fileName);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to store message: " + e.getMessage());
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
}