package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests;

public class CreateGeoChatroom {
    private final String username;
    private final String chatId;
    private final double latitude;
    private final double longitude;
    private final double radius;

    public CreateGeoChatroom(String username, String chatId, double latitude, double longitude, double radius) {
        this.username = username;
        this.chatId = chatId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public String getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }
}
