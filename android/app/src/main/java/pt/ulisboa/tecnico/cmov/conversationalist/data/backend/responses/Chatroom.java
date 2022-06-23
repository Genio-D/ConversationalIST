package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.Objects;

public class Chatroom {
    private final String chatId;
    private final String type;
    private final int messages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chatroom)) return false;
        Chatroom chatroom = (Chatroom) o;
        return getChatId().equals(chatroom.getChatId()) && getType().equals(chatroom.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId(), getType());
    }

    public Chatroom(String chatId, String type, int messages) {
        this.chatId = chatId;
        this.type = type;
        this.messages = messages;
    }

    public String getChatId() {
        return chatId;
    }

    public String getType() {
        return type;
    }

    public int getMessages() {
        return messages;
    }
}
