package environment;

import java.util.Objects;

public class Position {

    private int posx;
    private int posy;
    private Double cost = 0.0;

    private Double priority = 0.0;

    public Position(int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
    }

    @Override
    public boolean equals(Object object) {
        Position position = (Position) object;

        return (posx == position.getPosx() && posy == position.getPosy());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(posx, posy);
    }

    @Override
    public String toString() {
        return "Position{" +
                "posx=" + posx +
                ", posy=" + posy +
                '}';
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }


    public Double getPriority() {
        return priority;
    }

    public void setPriority(Double priority) {
        this.priority = priority;
    }

}
