package pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses;

import java.util.List;

public class PublicChatrooms extends Response{
    private final List<PublicChatroom> list;

    public List<PublicChatroom> getList() {
        return list;
    }

    public PublicChatrooms(List<PublicChatroom> list, String errorMessage, boolean error) {
        super(errorMessage, error);
        this.list = list;
    }
}
