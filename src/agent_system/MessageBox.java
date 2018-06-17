package agent_system;

import java.util.ArrayList;

public class MessageBox {

    public static MessageBox instance = null;
    public final static ArrayList<Message> messages = new ArrayList<>();

    public static MessageBox getInstance() {
        return instance == null ? new MessageBox() : instance;
    }

    public synchronized ArrayList<Message> getAgentMessages(int agentId) {
        try {
            ArrayList<Message> messages = new ArrayList<>();
            ArrayList<Message> copyMessages = new ArrayList<>(this.messages);

            for (Message message : copyMessages) {
                //System.out.println("message : " + message);
                if (message.getReceiverId() == agentId)
                    messages.add(message);
            }

            return messages;
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public synchronized void deleteMessage (int messageId) {
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
