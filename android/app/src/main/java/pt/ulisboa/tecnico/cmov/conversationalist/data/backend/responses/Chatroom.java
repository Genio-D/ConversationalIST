package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

public class Chatroom {
    private final String chatId;
    private final String type;
    private final int messages;


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
