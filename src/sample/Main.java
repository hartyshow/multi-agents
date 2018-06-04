package sample;

import agent_system.Agent;
import environment.Grid;
import environment.Position;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    private Grid grid;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        createEnvironment(5, 4);
        printEnvironment();

        launchSimulation();
    }

    private void createEnvironment(int gridWidth, int nbAgent) {
        this.grid = new Grid(gridWidth);

        Random rand = new Random();
        Position currentPosition;
        Position finalPosition;
        for (int i = 0; i < nbAgent; i++){
            // Determine current position
            do{
                currentPosition = new Position(rand.nextInt(5), rand.nextInt(5));
            } while (!this.grid.isPositionAvailable(currentPosition));

            // Determine final position
            do{
                finalPosition = new Position(rand.nextInt(5), rand.nextInt(5));
            } while (!this.grid.isFinalPositionAvailable(finalPosition) && !finalPosition.equals(currentPosition));

            this.grid.addAgent(new Agent(this.grid, currentPosition, finalPosition));
        }
    }

    private void printEnvironment() {
        for (ArrayList<Agent> agents : grid.getAgents()) {
            for (Agent agent : agents) {
                System.out.print(agent == null ? " 0 " : " "+agent+" ");
            }
            System.out.println("");
        }
    }

    private void launchSimulation() {
        grid.launchSimulation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
