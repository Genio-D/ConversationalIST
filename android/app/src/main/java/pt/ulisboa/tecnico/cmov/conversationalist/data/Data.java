package pt.ulisboa.tecnico.cmov.conversationalist.data;

import java.util.regex.Pattern;

import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.Chatroom;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.JoinedChatrooms;

public class Data {
    private static String username;
    private static JoinedChatrooms joinedChatrooms;
    private static MessageCache messageCache;

    public synchronized static void clean() {
        username = null;
        joinedChatrooms = null;
        messageCache = null;
    }

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

    public static void registerUsername(String username, String password) {
        BackendManager.addUser(username, password);
        setUsername(username);
    }

    public static void login(String username, String password) {
        BackendManager.login(username, password);
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

    public static Chatroom getChatroom(String chatId) {
        for(var chatroom : getJoinedChatrooms().getChatrooms()) {
            if (chatroom.getChatId().equals(chatId)) {
                return chatroom;
            }
        }
        throw new RuntimeException("chatId not in joined chatrooms");
    }

    public static String getChatIdFromPath(String path) {
        return path.split("/")[0];
    }

    public static boolean onlyAlphanum(String s) {
        var p = Pattern.compile("[a-zA-Z0-9]+");
        var m = p.matcher(s);
        return m.matches();
    }
}
