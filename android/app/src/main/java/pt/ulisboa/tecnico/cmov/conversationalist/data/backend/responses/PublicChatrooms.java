package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.List;

public class PublicChatrooms {
    private final List<String> list;

    public List<String> getList() {
        return list;
    }

    public PublicChatrooms(List<String> list) {
        this.list = list;
    }
}
