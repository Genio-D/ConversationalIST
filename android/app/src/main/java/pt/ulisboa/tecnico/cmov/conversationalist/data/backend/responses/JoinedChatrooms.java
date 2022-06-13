package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.Map;

public class JoinedChatrooms extends Response {
    private final Map<String, Integer> chatrooms;

    public JoinedChatrooms(Map<String, Integer> chatrooms, String errorMessage, boolean error) {
        super(errorMessage, error);
        this.chatrooms = chatrooms;
    }

    public Map<String, Integer> getChatrooms() {
        return chatrooms;
    }
}
