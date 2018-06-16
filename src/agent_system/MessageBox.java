package agent_system;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessageBox {

    public static MessageBox instance = null;
    public final static ArrayList<Message> messages = new ArrayList<>();
    private final Object lock = new Object();

    public static MessageBox getInstance() {
        return instance == null ? new MessageBox() : instance;
    }

    public ArrayList<Message> getAgentMessages(int agentId) {
        ArrayList<Message> messages = new ArrayList<>();
        ArrayList<Message> copyMessages = new ArrayList<>(this.messages);

        for (Message message : copyMessages) {
            //System.out.println("message : " + (message == null) + " / message.getReceiver() : " + (message.getReceiver() == null));
            if (message.getReceiver().getAgentId() == agentId)
                messages.add(message);
        }

        return messages;
    }

    public void deleteMessage (int messageId) {
        int i = 0;
        for (Message message : this.messages) {
            if (message.getId() == messageId) {
                this.messages.remove(i);
                return;
            }
            i++;
        }
    }

    public void postAgentMessage (Message message) {
        messages.add(message);
    }
}
