package pt.ulisboa.tecnico.cmov.conversationalist;

public class ChatMessage {

    public static final int TEXT=0;
    public static final int IMAGE=1;
    public static final int LOCATION=2;

    private String author;
    private String type;
    private String content;
    private String timestamp;

    public ChatMessage(String author, String type, String content, String timestamp) {
        this.author = author;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;
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
