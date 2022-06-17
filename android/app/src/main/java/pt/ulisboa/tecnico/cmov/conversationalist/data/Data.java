package pt.ulisboa.tecnico.cmov.conversationalist.data;

import android.database.Observable;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.JoinedChatrooms;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.Message;

public class Data {
    private static String username;
    private static JoinedChatrooms joinedChatrooms;
    private static MessageCache messageCache;
    private static Observable<Boolean> received;

    public synchronized static String getUsername() {
        if (username == null) {
            throw new RuntimeException("Not username defined yet");
        } else {
            return username;
        }
    }

    public synchronized static void setUsername(String username) {
        Data.username = username;
    }

    public static void registerUsername(String username) {
        BackendManager.addUser(username);
        setUsername(username);
    }

    public synchronized static JoinedChatrooms getJoinedChatrooms() {
        if (joinedChatrooms == null) {
            joinedChatrooms = BackendManager.getJoinedChatrooms(getUsername());
        }
        return joinedChatrooms;
    }

    public synchronized static void updateJoinedChatrooms() {
        joinedChatrooms = BackendManager.getJoinedChatrooms(getUsername());
    }

    public synchronized static MessageCache getMessageCache() {
        if (messageCache == null) {
            messageCache = new MessageCache();
        }
        return messageCache;
    }
}
