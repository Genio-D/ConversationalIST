package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.ArrayList;
import java.util.List;

public class JoinedChatrooms extends Response {
    private final List<Chatroom> chatrooms;

    public JoinedChatrooms(List<Chatroom> chatrooms, String errorMessage, boolean error) {
        super(errorMessage, error);
        this.chatrooms = chatrooms;
    }

    public List<Chatroom> getChatrooms() {
        return chatrooms;
    }

    public List<String> getChatIds() {
        var chatIds = new ArrayList<String>();
        for(var chatroom : getChatrooms()) {
            chatIds.add(chatroom.getChatId());
        }
        return chatIds;
    }
}
