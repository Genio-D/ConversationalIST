package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

public class Message extends Response {
    private final String author;
    private final String content;
    private final String timestamp;
    private final String type;

    @Override
    public String toString() {
        return "Message{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Message(String author, String content, String timestamp, String type, String errorMessage, boolean error) {
        super(errorMessage, error);
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
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

    public String getType() {
        return type;
    }
}
