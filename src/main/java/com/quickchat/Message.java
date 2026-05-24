package com.quickchat;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Message {
    private static int globalMessageCounter = 0;
    private static List<Message> sentMessages = new ArrayList<>();

    private String messageID;
    private int messageNum;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    public Message(String recipient, String messageText) {
        this.messageNum = ++globalMessageCounter;
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.status = "pending";
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = 1_000_000_000L + (long)(rand.nextDouble() * 8_000_000_000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    public String checkRecipientCell() {
        if (recipient == null || recipient.length() < 10 || recipient.length() > 15)
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        if (!recipient.startsWith("+"))
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        String digits = recipient.substring(1);
        if (!digits.matches("\\d+"))
            return "Cell phone number is incorrectly formatted or does not contain an international code.";
        return "Cell phone number successfully captured.";
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        int zeroBased = messageNum - 1;
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();
        return firstTwo + ":" + zeroBased + ":" + firstWord + lastWord;
    }

    public String sentMessage() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.print("Choice: ");
        String choice = sc.nextLine();
        if (choice.equals("1")) {
            status = "sent";
            return "send";
        } else if (choice.equals("2")) {
            status = "disregarded";
            return "disregard";
        } else {
            status = "stored";
            return "store";
        }
    }

    public void storeMessageToJson() {
        String fileName = "stored_messages.json";
        JSONArray arr = new JSONArray();
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)));
            arr = new JSONArray(content);
        } catch (IOException e) { }

        JSONObject obj = new JSONObject();
        obj.put("messageID", messageID);
        obj.put("messageNum", messageNum);
        obj.put("recipient", recipient);
        obj.put("messageText", messageText);
        obj.put("messageHash", messageHash);
        obj.put("status", status);
        arr.put(obj);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(arr.toString(2));
            System.out.println("Message successfully stored.");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) return "No messages sent.";
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("ID: ").append(m.messageID)
              .append(", Hash: ").append(m.messageHash)
              .append(", To: ").append(m.recipient)
              .append(", Msg: ").append(m.messageText).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    public static void addToSentMessages(Message msg) {
        sentMessages.add(msg);
    }

    // Getters for testing
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getMessageText() { return messageText; }
    public int getMessageNum() { return messageNum; }
    public String getRecipient() { return recipient; }
}