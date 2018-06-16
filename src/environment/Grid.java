package environment;

import agent_system.Agent;
import sample.Main;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Grid extends Observable implements Observer{

    private Main main;
    private int width;
    private ArrayList<ArrayList<Agent>> agents;

    public Grid(int width, Main main) {
        this.main = main;

        this.width = width;
        this.agents = new ArrayList<>();

        ArrayList<Agent> bidon = new ArrayList<>();
        for (int j = 0; j < width; j++) {
            bidon.add(null);
        }

        for(int i=0; i < width; i++)
        {
            this.agents.add(new ArrayList<>(bidon));
        }

        addObserver(main);
    }

    public void addAgent (Agent agent) {
        agents.get(agent.getCurrentPosition().getPosx()).set(agent.getCurrentPosition().getPosy(), agent);

        setChanged();
        notifyObservers(agent);
    }

    public boolean goalReached () {
        for (ArrayList<Agent> arrayAgents : agents) {
            for (Agent agent : arrayAgents) {
                if (agent != null && !agent.goalReached())
                    return false;
            }
        }

        return true;
    }

    public synchronized boolean isPositionAvailable(Position position) {
        for (ArrayList<Agent> arrayAgents : agents) {
            for (Agent agent : arrayAgents) {
                if (agent != null && agent.getCurrentPosition().equals(position))
                    return false;
            }
        }

        return true;
    }

    public synchronized boolean isFinalPositionAvailable(Position position) {
        for (ArrayList<Agent> arrayAgents : agents) {
            for (Agent agent : arrayAgents) {
                if (agent != null && agent.getFinalPosition().equals(position))
                    return false;
            }
        }

        return true;
    }

    public void launchSimulation() {
        for (ArrayList<Agent> arrayAgents : agents) {
            for (Agent agent : arrayAgents) {
                if (agent != null)
                    new Thread(agent).start();
            }
        }
    }

    public Agent getAgentById (int id) {
        for (ArrayList<Agent> arrayAgents : agents) {
            for (Agent agent : arrayAgents) {
                if (agent != null && agent.getAgentId() == id)
                    return agent;
            }
        }

        return null;
    }

    public synchronized Agent getAgentOnPosition (Position position) {
        for (ArrayList<Agent> arrayAgents : agents) {
            for (Agent agent : arrayAgents) {
                if (agent != null && agent.getCurrentPosition().equals(position))
                    return agent;
            }
        }

        return null;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public synchronized ArrayList<ArrayList<Agent>> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<ArrayList<Agent>> agents) {
        this.agents = agents;
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
