package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.Map;

public class JoinedChatrooms {
    private final Map<String, Integer> chatrooms;

    public JoinedChatrooms(Map<String, Integer> chatrooms) {
        this.chatrooms = chatrooms;
    }

    public Map<String, Integer> getChatrooms() {
        return chatrooms;
    }
}
