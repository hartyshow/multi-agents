package sample;

import agent_system.Agent;
import environment.Grid;
import environment.Position;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class Main extends Application implements Observer {

    private Grid grid;
    private StackPane root;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");

        createEnvironment(5, 4);
        printEnvironment();

        //launchSimulation();

        int numCols = 5 ;

        BooleanProperty[][] switches = new BooleanProperty[numCols][numCols];
        for (int x = 0 ; x < numCols ; x++) {
            for (int y = 0 ; y < numCols ; y++) {
                switches[x][y] = new SimpleBooleanProperty();
            }
        }

        GridPane grid = createGrid(switches);

        grid.setStyle("-fx-background-color: cell-border-color, cell-color ;\n" +
                "    -fx-background-insets: 0, 1 1 0 0 ;\n" +
                "    -fx-padding: 1 ;");

        root = new StackPane(grid);

        root.setStyle("-fx-padding: 20 ; cell-color: white ; cell-border-color: black ;");
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createCell(BooleanProperty cellSwitch) {

        StackPane cell = new StackPane();

        cell.setStyle("-fx-background-color: cell-border-color, cell-color ;\n" +
                "    -fx-background-insets: 0, 0 0 1 1 ;");

        //cell.setOnMouseClicked(e -> cellSwitch.set(! cellSwitch.get() ));

        Text circle = new Text();

        //circle.visibleProperty().bind(cellSwitch);

        cell.getChildren().add(circle);
        cell.getStyleClass().add("cell");
        return cell;
    }

    private GridPane createGrid(BooleanProperty[][] switches) {

        int numCols = switches.length;
        int numRows = switches[0].length;

        GridPane grid = new GridPane();

        for (int x = 0; x < numCols; x++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        for (int y = 0; y < numRows; y++) {
            RowConstraints rc = new RowConstraints();
            rc.setFillHeight(true);
            rc.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rc);
        }

        for (int x = 0; x < numCols; x++) {
            for (int y = 0; y < numRows; y++) {
                grid.add(createCell(switches[x][y]), x, y);
            }
        }

        grid.getStyleClass().add("grid");
        return grid;
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

    @Override
    public void update(Observable o, Object arg) {

    }
}
