package agent_system;

import environment.Grid;
import environment.Position;

import java.util.LinkedList;

public class Agent extends Thread {

    private int id;
    public static int compteurId = 0;

    private Position currentPosition;
    private Position finalPosition;
    private Grid grid;

    public Agent (Grid grille, Position initialPosition, Position finalPosition) {
        compteurId++;
        this.id = compteurId;

        this.currentPosition = initialPosition;
        this.grid = grille;
        this.finalPosition = finalPosition;

        MessageBox.getInstance().addAgentMessageBox();
    }

    public void move(Position position) {
        this.currentPosition = position;
    }

    public void communicate(Agent receiver, Position advicedPosition) {
        MessageBox.getInstance().postAgentMessage(new Message(this, receiver, advicedPosition));
    }

    public void treatMessages() {
        LinkedList<Message> messages = MessageBox.getInstance().getAndRemoveAgentMessages(this);

        // TODO
    }

    public void reason() {

    }

    public void run() {
        try {
            int i = 0;
            while (true) {
                System.out.println("Agent " + this + " - Tour " + i);

                Thread.sleep(1000);
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean goalReached () {
        return this.currentPosition.equals(this.finalPosition);
    }

    @Override
    public String toString() {
        return ""+this.id;
    }

    public int getAgentId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Position getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(Position finalPosition) {
        this.finalPosition = finalPosition;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
