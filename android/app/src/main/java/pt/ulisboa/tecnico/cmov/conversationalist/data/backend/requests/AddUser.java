package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.requests;

public class AddUser {
    private final String username;

    public AddUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
