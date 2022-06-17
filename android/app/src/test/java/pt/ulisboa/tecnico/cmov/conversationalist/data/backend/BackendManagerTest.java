package pt.ulisboa.tecnico.cmov.conversationalist.data.backend;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;
import java.util.Set;

public class BackendManagerTest {
    // CHANGE ADDRESS BEFORE TESTING

    @Test
    public void backendTest() {
        var userError = "user doesn't exist";
        var chatIdError = "chat doesn't exist";

        var username = "velho";
        BackendManager.addUser(username);
        assertThrows(RuntimeException.class, () -> BackendManager.addUser(username));

        var publicChatId = "A1";
        BackendManager.createPublicChatroom(username, publicChatId);
        assertThrows("username already exists",
                RuntimeException.class,
                () -> BackendManager.createPublicChatroom(username, publicChatId));
        assertThrows(userError,
                RuntimeException.class,
                () -> BackendManager.createPublicChatroom("asd", publicChatId));

        var geoChatId = "0-27";
        assertThrows(userError,
                RuntimeException.class,
                () -> BackendManager.createGeoChatroom(username, publicChatId, 12.3, 12.3, 12.3));
        BackendManager.createGeoChatroom(username, geoChatId, 12.3, 12.3, 12.3);
        assertThrows(chatIdError,
                RuntimeException.class,
                () -> BackendManager.createGeoChatroom(username, geoChatId, 12.3, 12.3, 12.3));

        var publicChatrooms = List.of(publicChatId);
        assertEquals(publicChatrooms, BackendManager.getPublicChatrooms().getList());

        var joinedChatrooms = Set.of(publicChatId, geoChatId);
        assertEquals(joinedChatrooms, BackendManager.getJoinedChatrooms(username).getChatrooms().keySet());

        var messageType = "text";
        var content = "hello";
        BackendManager.postMessage(username, publicChatId, messageType, content);
        BackendManager.postMessage(username, geoChatId, messageType, content);
        assertThrows(userError,
                RuntimeException.class,
                () -> BackendManager.postMessage("asd", publicChatId, messageType, content));
        assertThrows(chatIdError,
                RuntimeException.class,
                () -> BackendManager.postMessage(username, "asd", messageType, content));

        assertEquals(content, BackendManager.getMessage("/" + publicChatId + "/" + 0).getContent());
        assertEquals(content, BackendManager.getMessage("/" + geoChatId + "/" + 0).getContent());

        var username2 = "genio";
        BackendManager.addUser(username2);
        BackendManager.joinRoom(username2, publicChatId);
        assertThrows(userError,
                RuntimeException.class,
                () -> BackendManager.joinRoom("asd", publicChatId));
        assertThrows(chatIdError,
                RuntimeException.class,
                () -> BackendManager.joinRoom(username2, "asd"));
    }
}