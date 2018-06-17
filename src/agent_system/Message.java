package agent_system;

import environment.Position;

public class Message {

    private int id;
    private static int id_counter = 0;

    private int senderId;
    private int receiverId;
    private Position desiredPosition;

    public Message(int senderId, int receiverId, Position desiredPosition) {
        this.id = id_counter;
        id_counter++;

        this.senderId = senderId;
        this.receiverId = receiverId;
        this.desiredPosition = desiredPosition;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", desiredPosition=" + desiredPosition +
                '}';
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
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
