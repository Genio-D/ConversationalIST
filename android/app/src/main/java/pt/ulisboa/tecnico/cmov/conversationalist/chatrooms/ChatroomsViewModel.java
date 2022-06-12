package pt.ulisboa.tecnico.cmov.conversationalist.chatrooms;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;

public class ChatroomsViewModel extends ViewModel {
    private MutableLiveData<List<String>> joinedChatrooms;

    public MutableLiveData<List<String>> getJoinedChatrooms() {
        if (joinedChatrooms == null) {
            joinedChatrooms = new MutableLiveData<>(BackendManager.getPublicChatrooms().getList());
        }
        return joinedChatrooms;
    }
}
