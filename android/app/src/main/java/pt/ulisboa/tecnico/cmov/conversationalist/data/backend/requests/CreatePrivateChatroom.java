package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests;

public class CreatePrivateChatroom {
    private final String username;
    private final String chatId;

    public CreatePrivateChatroom(String username, String chatId) {
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
