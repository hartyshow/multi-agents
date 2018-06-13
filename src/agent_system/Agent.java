package agent_system;

import environment.Grid;
import environment.Position;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

public class Agent extends Observable implements Runnable {

    private int id;
    public static int compteurId = 0;

    private Position oldPosition;
    private Position currentPosition;
    private Position finalPosition;
    private Grid grid;
    private String color;

    public Agent (Grid grille, Position initialPosition, Position finalPosition) {
        compteurId++;
        this.grid = grille;
        this.id = compteurId;

        Random rand = new Random();
        this.color = String.format("#%02x%02x%02x", rand.nextInt(250)+5, rand.nextInt(250)+5, rand.nextInt(250)+5);

        addObserver(grid);
        move(initialPosition);
        this.finalPosition = finalPosition;

        MessageBox.getInstance().addAgentMessageBox();
    }

    public void move(Position position) {
        this.oldPosition = this.currentPosition;
        this.currentPosition = position;

        setChanged();
        notifyObservers(this);
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

    @Override
    public void run() {
        try {
            Thread.sleep(1000);

            while (true) {
                Position newPosition = getRandomMovement(currentPosition);

                if (this.grid.isPositionAvailable(newPosition))
                    move(newPosition);

                if (this.getAgentId() == 4)
                    System.out.println("Agent " + this.getAgentId() + " - oldPosition : " + oldPosition + " - newPosition : " + currentPosition + " / butAtteint ? " + this.getCurrentPosition().equals(this.getFinalPosition()));

                if (this.getCurrentPosition().equals(this.getFinalPosition()))
                    break;

                Thread.sleep(500);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Position getRandomMovement (Position currentPosition) {
        try {
            Random rand = new Random();
            int x = 0;
            int y = 0;
            int chance = rand.nextInt(2);

            int rand1 = rand.nextInt(3) - 1;
            int rand2 = rand.nextInt(3) - 1;
            int rand3 = rand.nextInt(3) - 1;
            int rand4 = rand.nextInt(3) - 1;

            // Je bouge le x puis le y
            if (chance == 0) {
                if (currentPosition.getPosx() + rand1 > grid.getWidth() || currentPosition.getPosx() + rand1 < 0)
                    x = currentPosition.getPosx();
                else
                    x = currentPosition.getPosx() + rand1;

                if (x == currentPosition.getPosx()) {
                    if (currentPosition.getPosy() + rand2 > grid.getWidth() || currentPosition.getPosy() + rand2 < 0)
                        y = currentPosition.getPosy();
                    else
                        y = currentPosition.getPosy() + rand2;
                }
            }
            // Je bouge le y puis le x
            else {
                if (currentPosition.getPosy() + rand3 > grid.getWidth() || currentPosition.getPosy() + rand3 < 0)
                    y = currentPosition.getPosy();
                else
                    y = currentPosition.getPosy() + rand3;

                if (y == currentPosition.getPosy()) {
                    if (currentPosition.getPosx() + rand4 > grid.getWidth() || currentPosition.getPosx() + rand4 < 0)
                        x = currentPosition.getPosx();
                    else
                        x = currentPosition.getPosx() + rand4;
                }
            }

            return new Position(x, y);
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public boolean goalReached () {
        return this.currentPosition.equals(this.finalPosition);
    }

    @Override
    public String toString() {
        return this.getAgentId() + " / oldPosition : " + this.getOldPosition() + " / currentPosition : " + this.getCurrentPosition();
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

    public Position getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
