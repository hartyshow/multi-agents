package agent_system;

import java.util.ArrayList;
import java.util.LinkedList;

public class MessageBox {

    public static MessageBox instance = null;
    public ArrayList<LinkedList<Message>> messages;

    private MessageBox () {
        this.messages = new ArrayList<>();
    }

    public static MessageBox getInstance() {
        return instance == null ? new MessageBox() : instance;
    }

    public void addAgentMessageBox () {
        messages.add(new LinkedList<>());
    }

    public LinkedList<Message> getAndRemoveAgentMessages(Agent agent) {
        if (this.messages.size() >= agent.getAgentId()) {
            LinkedList<Message> messages = this.messages.get(agent.getAgentId());
            this.messages.get(agent.getAgentId()).remove();

            return messages;
        }
        else
            return null;
    }

    public void postAgentMessage (Message message) {
        messages.get(message.getReceiver().getAgentId()).add(message);
    }
}
