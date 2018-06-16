package agent_system;

import environment.Grid;
import environment.Position;
import javafx.scene.control.Alert;

import java.util.*;

public class Agent extends Observable implements Runnable {

    private int id;
    public static int compteurId = 0;

    private Position oldPosition;
    private Position currentPosition;
    private Position finalPosition;
    private Grid grid;
    private String color;

    private boolean running = true;

    public Agent (Grid grille, Position initialPosition, Position finalPosition) {
        compteurId++;
        this.grid = grille;
        this.id = compteurId;
        this.finalPosition = finalPosition;
        Random rand = new Random();
        this.color = String.format("#%02x%02x%02x", rand.nextInt(250)+5, rand.nextInt(250)+5, rand.nextInt(250)+5);

        addObserver(grid);
        move(initialPosition);
        
    }

    public Position glouton()
    {

        if(this.currentPosition.equals(this.finalPosition))
        {
            return null;
        }

        List<Position> neighbors = getNeighbors(this.currentPosition);

        neighbors.sort(new Comparator<Position>() {
            @Override
            public int compare(Position o1, Position o2) {

                return heuristic(o1,finalPosition)-heuristic(o2,finalPosition);
            }
        });

        return neighbors.get(0);
    }

    public Map<Position,Position> aStar()
    {
        Position current = this.currentPosition;
        Position goal = this.finalPosition;



        List<Position> frontier = new ArrayList<Position>(); // liste des cases adjacentes à explorer
        frontier.add(current);

        Map<Position,Position> cameFrom = new HashMap<>();
        cameFrom.put(current,current);

        Map<Position, Double> cost_so_far = new HashMap<>();
        cost_so_far.put(current, 0.0);

        Map<Position, Double> priorities = new HashMap<>();
        priorities.put(current,0.0);

        while(!frontier.isEmpty())
        {
            frontier.sort(new Comparator<Position>() {
                @Override
                public int compare(Position o1, Position o2) {

                    if(priorities.get(o1) - priorities.get(o2)<0)
                    {
                        return -1;
                    }
                    else if (priorities.get(o1) - priorities.get(o2)>0)
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
                Double new_cost = cost_so_far.get(current) + 1;
                if(!cost_so_far.containsKey(next) || new_cost < cost_so_far.get(next))
                {
                    cost_so_far.put(next,new_cost);
                    Double priority = new_cost + heuristic(next,goal);
                    priorities.put(next, priority);
                    frontier.add(next);
                    cameFrom.put(next,current);
                }
            }
        }

        return cameFrom;
    }

    public List<Position> reconstructPath (Map<Position,Position> came_from)
    {
        try {
            List<Position> path = new ArrayList<>();
            Position current = this.finalPosition;
            if (this.finalPosition.equals(this.currentPosition)) {
                return null;
            } else {
                try {
                    while (current != null && !current.equals(this.currentPosition)) {
                        path.add(current);
                        current = came_from.get(current);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //path.push_back(start); // optional
            Collections.reverse(path);
            return path;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public int heuristic(Position firstPosition, Position secondPosition)
    {
        // distance de Manathan
        return Math.abs(firstPosition.getPosx() - secondPosition.getPosx()) + Math.abs(firstPosition.getPosy()-secondPosition.getPosy());
        // distance euclidienne
        //return Math.sqrt(Math.pow((double) (firstPosition.getPosx() - secondPosition.getPosx()),2)+Math.pow((double) (firstPosition.getPosy() - secondPosition.getPosy()),2));

    }

    public List<Position> getNeighbors(Position position)
    {
        List<Position> neighbors = new ArrayList<Position>();
        // && grid.getAgents().get(position.getPosx()-1).get(position.getPosy()) == null
        if(position.getPosx() != 0)
        {
            neighbors.add(new Position(position.getPosx()-1,position.getPosy()));
        }
        // && grid.getAgents().get(position.getPosx()+1).get(position.getPosy()) == null
        if(position.getPosx() != grid.getWidth()-1)
        {
            neighbors.add(new Position(position.getPosx()+1,position.getPosy()));
        }
        //&& grid.getAgents().get(position.getPosx()).get(position.getPosy()-1) == null
        if(position.getPosy() != 0 )
        {
            neighbors.add(new Position(position.getPosx(),position.getPosy()-1));
        }
        //&& grid.getAgents().get(position.getPosx()).get(position.getPosy()+1) == null
        if(position.getPosy() != grid.getWidth()-1 )
        {
            neighbors.add(new Position(position.getPosx(),position.getPosy()+1));
        }

        return neighbors;
    }

    public void move(Position position) {
        this.oldPosition = this.currentPosition;
        this.currentPosition = position;
        if (oldPosition != null && grid.getAgents().get(oldPosition.getPosx()).get(oldPosition.getPosy()).equals(this)) {
            grid.getAgents().get(oldPosition.getPosx()).set(oldPosition.getPosy(), null);
        }
        grid.getAgents().get(currentPosition.getPosx()).set(currentPosition.getPosy(), this);
        setChanged();
        notifyObservers(this);
    }

    public void communicate(Agent receiver, Position desiredPosition) {
        MessageBox.getInstance().postAgentMessage(new Message(this, receiver, desiredPosition));
    }

    public ArrayList<Message> getInboxMessages () {
        return MessageBox.getInstance().getAgentMessages(this.getAgentId());
    }

    public void treatMessage(Message message) {
        if (message.getDesiredPosition().equals(this.getCurrentPosition()))
            move(getRandomMovement(this.getCurrentPosition()));

        MessageBox.getInstance().deleteMessage(message.getId());
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);

            while (running) {
                Thread.sleep(500);

                // On récupère les messages
                ArrayList<Message> messages = new ArrayList<>();
                messages = getInboxMessages();

                // On traite les messages un par un puis on les supprime
                if (messages != null)
                    for (Message message : messages)
                        treatMessage(message);

                // On se déplace pour atteindre notre but final
                //Map<Position,Position> aStarPositions = aStar();
                //List<Position> path = reconstructPath(aStarPositions);
                Position newPosition = glouton();

                if(newPosition != null)
                {

                    // Si la case est libre, on s'y déplace
                    if (this.grid.isPositionAvailable(newPosition))
                        move(newPosition);

                        // Sinon, on demande à l'agent sur la case de se déplacer
                    else {
                        Agent agent = grid.getAgentOnPosition(newPosition);

                        if (agent != null)
                            communicate(agent, newPosition);
                    }
                }


                if (this.grid.goalReached()) {
                    System.out.println("Fini !");

                    setChanged();
                    notifyObservers("fini");
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        this.running = false;
    }

    public synchronized Position getRandomMovement (Position currentPosition) {
        Random rand = new Random();
        int x,y;
        int chance = rand.nextInt(5);
        Position nextPosition;

        do {
            if (chance == 0) {
                x = currentPosition.getPosx() + 1 > grid.getWidth()-1 ? currentPosition.getPosx() : currentPosition.getPosx() + 1;
                y = currentPosition.getPosy();
            }
            else if (chance == 1) {
                x = currentPosition.getPosx() - 1 < 0 ? currentPosition.getPosx() : currentPosition.getPosx() - 1;
                y = currentPosition.getPosy();
            }
            else if (chance == 2) {
                x = currentPosition.getPosx();
                y = currentPosition.getPosy() + 1 > grid.getWidth()-1 ? currentPosition.getPosy() : currentPosition.getPosy() + 1;
            }
            else {
                x = currentPosition.getPosx();
                y = currentPosition.getPosy() - 1 < 0 ? currentPosition.getPosy() : currentPosition.getPosy() - 1;
            }

            nextPosition = new Position(x, y);
        }
        while (nextPosition.equals(currentPosition) && !this.getGrid().isPositionAvailable(nextPosition));

        System.out.println("current : " + currentPosition + " / nextPosition : " + nextPosition);

        return nextPosition;
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
