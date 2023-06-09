import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class GUI implements Initializable {

    //GUI Parameters
    public static final int rows = 15;
    public static final int columns = 15;
    public static final double cellWidth = (double) 700 /columns;
    public static final double cellHeight = (double) 700 /rows;
    @FXML
    public Pane pane;

    @FXML
    private TextArea console;
    @FXML
    private GridPane inventoryDisplay;
    public static ArrayList<Cell> cells = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Draw the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell cell = new Cell(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                cell.setStroke(javafx.scene.paint.Color.BLACK);
                cell.setFill(javafx.scene.paint.Color.WHITE);
                cell.setId((i + 1) + "," + (j + 1));
                cell.gridX = i + 1;
                cell.gridY = j + 1;
                pane.getChildren().add(cell);
                cells.add(cell);
                cell.run();
            }
        }

        //Draw the tributes
        Tribute t1 = new Tribute(pane, console, inventoryDisplay,2, 6, "Alice");
        Tribute t2 = new Tribute(pane, console, inventoryDisplay,2, 8, "Bob");
        Tribute t3 = new Tribute(pane, console, inventoryDisplay,2, 10, "Michał");

        Tribute t4 = new Tribute(pane, console, inventoryDisplay,4, 4, "David");
        Tribute t5 = new Tribute(pane, console, inventoryDisplay,4, 12, "Emily");

        Tribute t6 = new Tribute(pane, console, inventoryDisplay,6, 2, "Frank");
        Tribute t7 = new Tribute(pane, console, inventoryDisplay,6, 14, "Grace");

        Tribute t8 = new Tribute(pane, console, inventoryDisplay, 8, 2, "Harry");
        Tribute t9 = new Tribute(pane, console, inventoryDisplay,8, 14, "Isabella");

        Tribute t10 = new Tribute(pane, console, inventoryDisplay,10, 2, "Jack");
        Tribute t11 = new Tribute(pane, console, inventoryDisplay,10, 14, "Katherine");

        Tribute t12 = new Tribute(pane, console, inventoryDisplay,12, 4, "Luke");
        Tribute t13 = new Tribute(pane, console, inventoryDisplay,12, 12, "Mia");

        Tribute t14 = new Tribute(pane, console, inventoryDisplay,14, 6, "Noah");
        Tribute t15 = new Tribute(pane, console, inventoryDisplay,14, 8, "Olivia");
        Tribute t16 = new Tribute(pane, console, inventoryDisplay,14, 10, "Paweł");
        pane.getChildren().addAll(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);


        try {
            startGame();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startGame() throws InterruptedException {
        for(Node node : pane.getChildren()){
            if(node instanceof Tribute){
                Thread thread = new Thread((Runnable) node);
                thread.start();
            }
        }
    }

    public void shrinkGrid() {
        // Create a new list for cells to remove
        ArrayList<Cell> toRemove = new ArrayList<>();

        // Calculate the grid size from the number of cells
        int gridSize = (int) Math.sqrt(cells.size());

        // Iterate over all cells
        for (Cell cell : cells) {
            // Use the cell's gridX and gridY properties to get its coordinates
            int cellX = cell.gridX;
            int cellY = cell.gridY;

            // If the cell is on the outer edge of the grid, add it to the toRemove list
            if (cellX == 1 || cellY == 1 || cellX == gridSize || cellY == gridSize) {
                toRemove.add(cell);
                pane.getChildren().remove(cell); // remove cell from the pane
            }
        }

        // Remove all cells in the toRemove list from the cells list
        cells.removeAll(toRemove);
    }

}