package pt.ulisboa.tecnico.cmov.conversationalist;

import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.Message;

public class ChatMessage {

    public static final int TEXT=0;
    public static final int IMAGE=1;
    public static final int LOCATION=2;

    private final String author;
    private final String type;
    private final String content;
    private final String timestamp;

    public ChatMessage(String author, String type, String content, String timestamp) {
        this.author = author;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;
    }

    public ChatMessage(Message message) {
        this.author = message.getAuthor();
        this.type = message.getType();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getType() {
        switch(this.type) {
            case "text":
                return TEXT;
            case "image":
                return IMAGE;
            case "location":
                return LOCATION;
            default:
                return -1;
        }
    }
}
