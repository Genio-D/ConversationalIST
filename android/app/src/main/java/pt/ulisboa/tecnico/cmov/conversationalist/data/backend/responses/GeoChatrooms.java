package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.List;

public class GeoChatrooms extends Response {
    private final List<GeoChatroom> list;

    public GeoChatrooms(List<GeoChatroom> list, String errorMessage, boolean error) {
        super(errorMessage, error);
        this.list = list;
    }

    public List<GeoChatroom> getList() {
        return list;
    }
}
