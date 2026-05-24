package com.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    void testMessageLength() {
        String longMsg = "a".repeat(251);
        assertTrue(longMsg.length() > 250, "Should be too long");
        String shortMsg = "Hi";
        assertTrue(shortMsg.length() <= 250, "Should be okay");
    }

    @Test
    void testRecipientValidation() {
        Message valid = new Message("+27718693002", "test");
        assertEquals("Cell phone number successfully captured.", valid.checkRecipientCell());

        Message noPlus = new Message("27718693002", "test");
        assertNotEquals("Cell phone number successfully captured.", noPlus.checkRecipientCell());
    }

    @Test
    void testMessageIDLength() {
        Message m = new Message("+27718693002", "Hello");
        assertTrue(m.checkMessageID());   // note: capital I and D
        assertEquals(10, m.getMessageID().length());
    }

    @Test
    void testMessageHashFormat() {
        Message m = new Message("+27718693002", "Hi Mike");
        String hash = m.getMessageHash();
        // Format: two digits : one digit (zero-based index) : FIRSTWORDLASTWORD (all caps)
        assertTrue(hash.matches("\\d{2}:\\d:[A-Z]+[A-Z]+"), "Hash format: 00:0:HIMA...");
    }

    @Test
    void testTotalMessagesCounter() {
        int before = Message.returnTotalMessages();   // note: correct method name
        Message m = new Message("+27718693002", "Hello");
        Message.addToSentMessages(m);
        assertEquals(before + 1, Message.returnTotalMessages());   // assertEquals with 's'
    }
}