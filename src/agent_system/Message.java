package agent_system;

import environment.Position;

public class Message {

    private int id;
    private static int id_counter = 0;

    private Agent sender;
    private Agent receiver;
    private Position desiredPosition;

    public Message(Agent sender, Agent receiver, Position desiredPosition) {
        this.id = id_counter;
        id_counter++;

        this.sender = sender;
        this.receiver = receiver;
        this.desiredPosition = desiredPosition;
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

    public Position getDesiredPosition() {
        return desiredPosition;
    }

    public void setDesiredPosition(Position desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
