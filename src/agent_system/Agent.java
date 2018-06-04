package agent_system;

import environment.Grille;
import environment.Position;

public class Agent extends Thread {

    private int id;
    public static int compteurId = 0;

    private Position currentPosition;
    private Position finalPosition;
    private Grille grille;

    public Agent (Grille grille, Position initialPosition) {
        compteurId++;
        this.id = compteurId;

        this.currentPosition = initialPosition;
        this.grille = grille;
        this.finalPosition = null;

        MessageBox.getInstance().addAgentMessageBox();
    }

    public void move() {

    }

    public void communicate() {

    }

    public void lireMessage() {

    }

    public void raisonner() {

    }

    public void run() {

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

    public Grille getGrille() {
        return grille;
    }

    public void setGrille(Grille grille) {
        this.grille = grille;
    }
}
