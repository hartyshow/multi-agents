package environment;

import agent_system.Agent;

public class Case {

    private Position position;

    public Case(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
