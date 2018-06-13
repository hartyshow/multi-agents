package agent_system;

import environment.Grid;
import environment.Position;

import java.util.*;

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

    public Map<Position,Position> aStar()
    {
        Position current = this.currentPosition;
        Position goal = this.finalPosition;



        List<Position> frontier = new ArrayList<Position>(); // liste des cases adjacentes à explorer
        frontier.add(current);

        Map<Position,Position> cameFrom = new HashMap<>();
        cameFrom.put(current,current);

        while(!frontier.isEmpty())
        {
            frontier.sort(new Comparator<Position>() {
                @Override
                public int compare(Position o1, Position o2) {
                    if(o1.getPriority() - o2.getPriority()<0)
                    {
                        return -1;
                    }
                    else if (o1.getPriority() - o2.getPriority()<0)
                    {
                        return 1;
                    }
                    else
                        return 0;
                }
            });
            current = frontier.remove(0);

            if(current.equals(goal))
            {
                break;
            }

            List<Position> neighbors = getNeighbors(current);

            for( Position next : neighbors)
            {
                Double new_cost = next.getCost() + 1;
                if(next.getCost() == 0 || new_cost < next.getCost())
                {
                    next.setCost(new_cost);
                    Double priority = new_cost + heuristic(next,goal);
                    next.setPriority(priority);
                    frontier.add(next);
                    cameFrom.put(next,current);
                }
            }
        }

        return cameFrom;
    }

    public List<Position> reconstructPath (Map<Position,Position> came_from, Position start, Position goal)
    {
        List<Position> path = new ArrayList<>();
        Position current = goal;
        while (current != start) {
            path.add(current);
            current = came_from.get(current);
        }
        //path.push_back(start); // optional
       // std::reverse(path.begin(), path.end());
        return path;
    }


    public double heuristic(Position firstPosition, Position secondPosition)
    {
        return Math.abs(firstPosition.getPosx() - secondPosition.getPosx()) + Math.abs(firstPosition.getPosy()-secondPosition.getPosy());
    }

    public List<Position> getNeighbors(Position position)
    {
        List<Position> neighbors = new ArrayList<Position>();
        if(position.getPosx() != 0 && grid.getAgents().get(position.getPosx()-1).get(position.getPosy()) == null)
        {
            neighbors.add(new Position(position.getPosx()-1,position.getPosy()));
        }
        if(position.getPosx() != grid.getWidth()-1 && grid.getAgents().get(position.getPosx()+1).get(position.getPosy()) == null)
        {
            neighbors.add(new Position(position.getPosx()+1,position.getPosy()));
        }
        if(position.getPosy() != 0 && grid.getAgents().get(position.getPosx()).get(position.getPosy()-1) == null)
        {
            neighbors.add(new Position(position.getPosx(),position.getPosy()-1));
        }
        if(position.getPosy() != grid.getWidth() && grid.getAgents().get(position.getPosx()).get(position.getPosy()+1) == null)
        {
            neighbors.add(new Position(position.getPosx(),position.getPosy()+1));
        }

        return neighbors;
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
