package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests;

public class CreatePublicChatroom {
    private final String username;
    private final String chatId;

    public CreatePublicChatroom(String username, String chatId) {
        this.username = username;
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }
}
