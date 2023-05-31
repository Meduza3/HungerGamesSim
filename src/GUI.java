import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUI implements Initializable {

    //GUI Parameters
    public static final int rows = 15;
    public static final int columns = 15;
    public static final double cellWidth = 1200/columns;
    public static final double cellHeight = 800/rows;
    @FXML
    public Pane pane;
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
        Tribute t1 = new Tribute(pane, 2, 6, "Alice the Falcon Whisperer");
        Tribute t2 = new Tribute(pane, 2, 8, "Bob the Bear Wrestler");
        Tribute t3 = new Tribute(pane, 2, 10, "Clara the Dolphin Trainer");

        Tribute t4 = new Tribute(pane, 4, 4, "David the Wolf Tracker");
        Tribute t5 = new Tribute(pane, 4, 12, "Emily the Eagle Soarer");

        Tribute t6 = new Tribute(pane, 6, 2, "Frank the Panther Prowler");
        Tribute t7 = new Tribute(pane, 6, 14, "Grace the Snake Charmer");

        Tribute t8 = new Tribute(pane, 8, 2, "Harry the Hawk Gazer");
        Tribute t9 = new Tribute(pane, 8, 14, "Isabella the Butterfly Catcher");

        Tribute t10 = new Tribute(pane, 10, 2, "Jack the Tiger Tamer");
        Tribute t11 = new Tribute(pane, 10, 14, "Katherine the Horse Rider");

        Tribute t12 = new Tribute(pane, 12, 4, "Luke the Bull Fighter");
        Tribute t13 = new Tribute(pane, 12, 12, "Mia the Deer Chaser");

        Tribute t14 = new Tribute(pane, 14, 6, "Noah the Shark Diver");
        Tribute t15 = new Tribute(pane, 14, 8, "Olivia the Otter Swimmer");
        Tribute t16 = new Tribute(pane, 14, 10, "Peter the Parrot Talker");
        pane.getChildren().addAll(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);


        startGame();
    }

    private void startGame(){
        for(Node node : pane.getChildren()){
            if(node instanceof Tribute){
                Thread thread = new Thread((Runnable) node);
                thread.start();
            }
        }
    }
}