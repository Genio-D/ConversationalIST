package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests;

public class JoinRoom {
    private final String chatId;
    private final String username;

    public JoinRoom(String chatId, String username) {
        this.chatId = chatId;
        this.username = username;
    }

    public String getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }
}
