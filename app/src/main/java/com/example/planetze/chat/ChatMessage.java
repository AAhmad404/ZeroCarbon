package com.example.planetze.chat;

/**
 * Represents a single chat message in a conversation, either from the user or from the chatbot.
 */
public class ChatMessage {
    private final String message;
    private final boolean isUser;

    /**
     * Constructs a new ChatMessage object.
     *
     * @param message The text of the chat message.
     * @param isUser  A boolean indicating whether the message is from the user.
     *                - `true` if the message is from the user.
     *                - `false` if the message is from the chatbot.
     */
    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    /**
     * Gets the text of the chat message.
     *
     * @return A string containing the message text.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Checks whether the message is from the user.
     *
     * @return `true` if the message is from the user, `false` if it is from the chatbot.
     */
    public boolean isUser() {
        return isUser;
    }
}
