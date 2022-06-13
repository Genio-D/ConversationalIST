package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.List;

public class PublicChatrooms extends Response{
    private final List<String> list;

    public List<String> getList() {
        return list;
    }

    public PublicChatrooms(List<String> list, String errorMessage, boolean error) {
        super(errorMessage, error);
        this.list = list;
    }
}
