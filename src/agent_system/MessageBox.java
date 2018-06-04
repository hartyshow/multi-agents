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

    public LinkedList<Message> getAgentMessages (int agentId) {
        return this.messages.size() >= agentId ? this.messages.get(agentId) : null;
    }

    public void postAgentMessage (Message message) {
        messages.get(message.getSender().getAgentId()).add(message);
    }
}
