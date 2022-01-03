package alphonse.util.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class ChatManager {
    public final static ChatManager chatManager = new ChatManager();

    private ArrayBlockingQueue<ChatMessage> chatMessages = new ArrayBlockingQueue<>(10);
    private List<String> messageList = new ArrayList<>();

    public static class ChatMessage {
        private final Long timeStamp;
        private final String message;

        ChatMessage(String message) {
            this.timeStamp = System.currentTimeMillis();
            this.message = message;
        }

        public Long getTimeStamp() {
            return timeStamp;
        }

        public String getMessage() {
            return message;
        }
    }


    private ChatManager() {
    }

    public boolean chatMessagesContains(String s, long msAgo) {
        for (ChatMessage chatMessage : chatMessages) {
            if (chatMessage.message.contains(s) && (System.currentTimeMillis() - msAgo) < chatMessage.getTimeStamp()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkContainsAndRemove(String s, long msAgo) {
        ChatMessage toRemove = null;
        boolean match = false;
        for (ChatMessage chatMessage : chatMessages) {
            if (chatMessage.message.contains(s) && (System.currentTimeMillis() - msAgo) < chatMessage.getTimeStamp()) {
                toRemove = chatMessage;
                match = true;
                break;
            }
        }
        if (toRemove != null) {
            chatMessages.remove(toRemove);
        }
        return match;
    }

    public void addMessage(String msg) {
        if (chatMessages.size() >= 10) {
            chatMessages.remove();
            messageList.remove(0);
        }
        chatMessages.add(new ChatMessage(msg));
        messageList.add(msg);
    }
}