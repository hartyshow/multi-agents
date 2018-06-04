package environment;

import agent_system.Agent;

public class Grille {

    private int width;
    private Agent[][] cases;

    public Grille (int width) {
        this.cases = new Agent[width][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                this.cases[i][j] = null;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Agent[][] getCases() {
        return cases;
    }

    public void setCases(Agent[][] cases) {
        this.cases = cases;
    }
}
