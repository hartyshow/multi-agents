package sample;

import agent_system.Agent;
import environment.Grid;
import environment.Position;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application implements Observer {

    private Grid grid;
    private GridPane gridPane;
    private StackPane root;
    private boolean finished = false;

    public static final int NB_AGENTS = 30;
    public static final int GRID_WIDTH = 10;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");

        this.grid = new Grid(GRID_WIDTH, this);

        // Création de la grille
        this.gridPane = createGrid();

        gridPane.setStyle("-fx-background-color: cell-border-color, cell-color ;\n" +
                "    -fx-background-insets: 0, 1 1 0 0 ;\n" +
                "    -fx-padding: 1 ;");

        root = new StackPane(gridPane);

        root.setStyle("-fx-padding: 20 ; cell-color: white ; cell-border-color: black ;");
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        createAgents(NB_AGENTS);
        launchSimulation();
    }

    private StackPane createCell() {

        StackPane cell = new StackPane();

        cell.setStyle("-fx-background-color: cell-border-color, cell-color ;\n" +
                "    -fx-background-insets: 0, 0 0 1 1 ;\n");

        Text circle = new Text();
        cell.getChildren().add(circle);
        cell.getStyleClass().add("cell");
        return cell;
    }

    private GridPane createGrid() {
        GridPane gridPane = new GridPane();

        for (int x = 0; x < this.grid.getWidth(); x++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setFillWidth(true);
            cc.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(cc);
        }

        for (int y = 0; y < this.grid.getWidth(); y++) {
            RowConstraints rc = new RowConstraints();
            rc.setFillHeight(true);
            rc.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rc);
        }

        for (int x = 0; x < this.grid.getWidth(); x++) {
            for (int y = 0; y < this.grid.getWidth(); y++) {
                gridPane.add(createCell(), x, y);
            }
        }

        gridPane.getStyleClass().add("grid");
        return gridPane;
    }

    private void createAgents(int nbAgent) throws InterruptedException {
        Random rand = new Random();
        Position currentPosition;
        Position finalPosition;
        for (int i = 0; i < nbAgent; i++){
            // Determine current position
            do{
                currentPosition = new Position(rand.nextInt(grid.getWidth()), rand.nextInt(grid.getWidth()));
            } while (!this.grid.isPositionAvailable(currentPosition));

            // Determine final position
            do{
                finalPosition = new Position(rand.nextInt(grid.getWidth()), rand.nextInt(grid.getWidth()));
            } while (!this.grid.isFinalPositionAvailable(finalPosition) && !finalPosition.equals(currentPosition));

            Agent newAgent = new Agent(this.grid, currentPosition, finalPosition);
            
            this.grid.addAgent(newAgent);
        }
    }

    private synchronized String getEnvironmentString (Grid grid) {
        try {
            List<String> environment = new ArrayList<>();
            for (int i = 0; i < grid.getWidth(); i++) {
                for (int j = 0; j < grid.getWidth(); j++) {
                    environment.add("0");
                }
            }

            for (Agent agent : this.grid.getAgents()) {
                if (agent != null)
                    environment.set(agent.getCurrentPosition().getPosy() * (grid.getWidth()) + agent.getCurrentPosition().getPosx(), String.valueOf(agent.getAgentId()));
            }

            String environmentString = "";
            for (String envi : environment) {
                environmentString += envi + "/";
            }

            return environmentString.substring(0, environmentString.length() - 1);
        }
        catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    private void launchSimulation() {
        grid.launchSimulation();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            if (arg instanceof Agent) {
                Agent agent = (Agent) arg;

                /*if (agent.getOldPosition() != null) {
                    Node voidPosition = getNodeByRowColumnIndex(agent.getOldPosition().getPosx(), agent.getOldPosition().getPosy(), this.gridPane);
                    voidPosition.setStyle("-fx-background-color: black, white ;\n" +
                            "    -fx-background-insets: 0, 0 0 1 1 ;\n");

                    ObservableList<Node> child = ((StackPane) voidPosition).getChildren();
                    Text text = (Text) child.get(0);
                    text.setText(" ");
                }*/

                final String stringEnvironment = getEnvironmentString(grid);
                final String[] tabEnvironment = stringEnvironment.split("/");
                int x = 0;
                int y = 0;

                for (String caseEnvironment : tabEnvironment) {
                    int caseId = Integer.parseInt(caseEnvironment);

                    if (caseId == 0) {
                        Node voidPosition = getNodeByRowColumnIndex(x, y, this.gridPane);
                        voidPosition.setStyle("-fx-background-color: black, white ;\n" +
                                "    -fx-background-insets: 0, 0 0 1 1 ;");

                        ObservableList<Node> child = ((StackPane) voidPosition).getChildren();
                        Text text = (Text) child.get(0);
                        text.setText(" ");
                    } else {
                        Node agentPosition = getNodeByRowColumnIndex(x, y, this.gridPane);
                        agentPosition.setStyle("-fx-background-color: black, " +
                                grid.getAgentById(caseId).getColor() + ";\n" +
                                "    -fx-background-insets: 0, 0 0 1 1 ;");

                        ObservableList<Node> child = ((StackPane) agentPosition).getChildren();
                        Text text = (Text) child.get(0);
                        text.setText("" + caseId);
                    }

                    x++;
                    if (x == grid.getWidth()) {
                        y++;
                        x = 0;
                    }
                }
            }
            else {
                if (!finished) {
                    finished = true;
                    //this.grid.stopSimulation();
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Simulation completed !");
                        alert.setHeaderText(null);
                        alert.setContentText("Simulation successfully completed !");

                        Optional<ButtonType> option = alert.showAndWait();

                        if (option.get() == ButtonType.OK) {
                            Platform.exit();
                            System.exit(0);
                        }
                    });
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
