package agent_system;

import environment.Position;

public class Message {

    private Agent sender;
    private Agent receiver;
    private Position advicedPosition;

    public Message(Agent sender, Agent receiver, Position advicedPosition) {
        this.sender = sender;
        this.receiver = receiver;
        this.advicedPosition = advicedPosition;
    }

    public Agent getSender() {
        return sender;
    }

    public void setSender(Agent sender) {
        this.sender = sender;
    }

    public Agent getReceiver() {
        return receiver;
    }

    public void setReceiver(Agent receiver) {
        this.receiver = receiver;
    }

    public Position getAdvicedPosition() {
        return advicedPosition;
    }

    public void setAdvicedPosition(Position advicedPosition) {
        this.advicedPosition = advicedPosition;
    }
}
