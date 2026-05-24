package com.quickchat;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static int totalMessages = 0;
    private static int sentCount = 0;

    public static void main(String[] args) {
        if (!login()) {
            System.out.println("Login failed. Exiting.");
            return;
        }

        System.out.print("How many messages do you wish to compose? ");
        totalMessages = Integer.parseInt(scanner.nextLine());

        System.out.println("\nWelcome to QuickChat.\n");

        boolean running = true;
        while (running) {
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    sendMessages();
                    break;
                case 2:
                    System.out.println("Coming Soon.\n");
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.\n");
            }
        }
        scanner.close();
    }

    private static boolean login() {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();
        return user.equals("user") && pass.equals("pass");
    }

    private static void sendMessages() {
        sentCount = 0;
        for (int i = 0; i < totalMessages; i++) {
            System.out.println("\n--- Message " + (i+1) + " of " + totalMessages + " ---");

            String recipient = null;
            while (true) {
                System.out.print("Recipient (e.g., +27718693002): ");
                recipient = scanner.nextLine();
                Message temp = new Message(recipient, "dummy");
                String result = temp.checkRecipientCell();
                if (result.equals("Cell phone number successfully captured.")) {
                    break;
                } else {
                    System.out.println(result);
                }
            }

            String text = null;
            while (true) {
                System.out.print("Message (max 250 chars): ");
                text = scanner.nextLine();
                if (text.length() <= 250) {
                    break;
                } else {
                    int excess = text.length() - 250;
                    System.out.println("Message exceeds 250 chars by " + excess + "; please reduce.");
                }
            }

            Message msg = new Message(recipient, text);
            String action = msg.sentMessage();

            if (action.equals("send")) {
                sentCount++;
                Message.addToSentMessages(msg);
                System.out.println("Message successfully sent.");
                System.out.println("\n--- Sent Message Details ---");
                System.out.println("Message ID: " + msg.getMessageID());
                System.out.println("Message Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
                System.out.println("----------------------------\n");
            } else if (action.equals("disregard")) {
                System.out.println("Press 0 to delete the message.");
            } else {
                msg.storeMessageToJson();
                System.out.println("Message successfully stored.");
            }
        }
        System.out.println("\nTotal messages sent in this session: " + sentCount + "\n");
    }
}