package pt.ulisboa.tecnico.cmov.conversationalist.data;

import android.util.LruCache;

import pt.ulisboa.tecnico.cmov.conversationalist.ChatMessage;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.BackendManager;
import pt.ulisboa.tecnico.cmov.conversationalist.data.backend.responses.Message;

public class MessageCache {
    private final LruCache<String, ChatMessage> messageCache;

    public MessageCache() {
        this.messageCache = new LruCache<>(50) {
            @Override
            protected ChatMessage create(String key) {
                var message = BackendManager.getMessage(key);
                return new ChatMessage(message);
            }
        };
    }

    public synchronized ChatMessage getMessage(String path) {
        return messageCache.get(path);
    }

    public synchronized void insertMessage(String path, ChatMessage message) {
        messageCache.put(path, message);
    }
}
