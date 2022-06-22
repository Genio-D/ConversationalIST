package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

public class GeoChatroom extends Chatroom {
    private final double latitude;
    private final double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }

    private final double radius;

    public GeoChatroom(String chatId, String type, int messages, double latitude, double longitude, double radius) {
        super(chatId, type, messages);
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }
}
