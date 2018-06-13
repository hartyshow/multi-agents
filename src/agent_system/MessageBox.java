package agent_system;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessageBox {

    public static MessageBox instance = null;
    public ArrayList<Message> messages;

    private MessageBox () {
        this.messages = new ArrayList<>();
    }

    public static MessageBox getInstance() {
        return instance == null ? new MessageBox() : instance;
    }

    public ArrayList<Message> getAgentMessages(Agent agent) {
        ArrayList<Message> messages = new ArrayList<>();

        for (Message message : this.messages) {
            if (message.getReceiver().getAgentId() == agent.getAgentId())
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
