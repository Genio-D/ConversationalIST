package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests;

public class CreateChatroom {
    private final String username;

    public CreateChatroom(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
