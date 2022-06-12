package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests;

public class PostMessage {
    private final String username;
    private final String chatroomId;
    private final String messageType;
    private final String content;

    public PostMessage(String username, String chatroomId, String messageType, String content) {
        this.username = username;
        this.chatroomId = chatroomId;
        this.messageType = messageType;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public String getContent() {
        return content;
    }

    public String getMessageType() {
        return messageType;
    }
}
