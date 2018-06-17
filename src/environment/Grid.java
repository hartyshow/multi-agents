package environment;

import agent_system.Agent;
import sample.Main;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Grid extends Observable implements Observer{

    private int width;
    private static ArrayList<Agent> agents;

    public Grid (int width, Main main) {
        this.width = width;
        this.agents = new ArrayList<>();

        addObserver(main);
    }

    public synchronized void addAgent (Agent agent) {
        this.agents.add(agent);

        if (this.agents.size() == Main.NB_AGENTS) {
            setChanged();
            notifyObservers(agent);
        }
    }

    public boolean goalReached () {
        for (Agent agent : this.agents) {
            if (agent != null && !agent.goalReached())
                return false;
        }

        return true;
    }

    public synchronized boolean isPositionAvailable(Position position) {
        //System.out.println("agents : " + this.agents);
        boolean available = true;

        for (Agent agent : this.agents) {
            if (agent != null && agent.getCurrentPosition().equals(position))
                available = false;
        }

        //System.out.println("position : " + position + " available ? " + available);
        return available;
    }

    public synchronized boolean isFinalPositionAvailable(Position position) {
        for (Agent agent : this.agents) {
            if (agent != null && agent.getFinalPosition().equals(position))
                return false;
        }

        return true;
    }

    public void launchSimulation() {
        for (Agent agent : this.agents) {
            if (agent != null)
                new Thread(agent).start();
        }
    }

    public Agent getAgentById (int id) {
        for (Agent agent : this.agents) {
            if (agent != null && agent.getAgentId() == id)
                return agent;
        }

        System.out.println("pas trouvé : " + id);
        System.out.println("liste : " + this.agents);

        return null;
    }

    public synchronized Agent getAgentOnPosition (Position position) {
        for (Agent agent : agents) {
            if (agent != null && agent.getCurrentPosition().equals(position))
                return agent;
        }

        return null;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public ArrayList<Agent> getAgents() {
        return this.agents;
    }

    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
