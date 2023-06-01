import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Tribute extends Circle implements Runnable {
    String name;
    double health = 100;
    double hunger = 100;
    double thirst = 100;
    double reactivity = 100;
    boolean isAlive = true;
    int gridX, gridY;
    double timeItTakesToMove;
    private Pane pane;
    Tribute(Pane pane, double x, double y, String name) {
        this.setCenterX((x - 1) * GUI.cellWidth + GUI.cellWidth / 2);
        this.setCenterY((y - 1) * GUI.cellHeight + GUI.cellHeight / 2);
        this.setRadius(20);
        this.setOnMouseClicked(this::handleMouseClick);
        this.setOnMouseEntered(this::handleMouseHover);
        this.name = name;
        this.pane = pane;
    }

    private void handleMouseClick(MouseEvent mouseEvent) {
        System.out.println("Name: " + name + ", Health: " + health + ", Hunger: " + hunger + ", Thirst: " + thirst);
    }

    private void handleMouseHover(MouseEvent mouseEvent) {
        System.out.println("Mouse hovered on " + name + "!");
        ArrayList<Node> paneChildren = new ArrayList<>(pane.getParent().getChildrenUnmodifiable());
        for(int i = 0; i < paneChildren.size(); i++){
            System.out.println(paneChildren.get(i));
            Node node = paneChildren.get(i);
            if(node != null && node.getId() != null && node.getId().equals("info")){
                if(node instanceof Pane){
                    for(Node child : ((Pane) node).getChildren()){
                        if(child instanceof Text){
                            if(child.getId().equals("infoName")){((Text) child).setText(name);}
                            else if(child.getId().equals("infoHealth")){((Text) child).setText("Health: " + health);}
                            else if(child.getId().equals("infoHunger")){((Text) child).setText("Hunger: " + hunger);}
                            else if(child.getId().equals("infoThirst")){((Text) child).setText("Thirst: " + thirst);}
                        } else if(child instanceof Circle){
                            ((Circle) child).setFill(this.getFill());
                            ((Circle) child).setStroke(this.getStroke());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(4);
        this.setStroke(Color.rgb((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random())));

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        reactivity = 0.6 * health + 0.2 * hunger + 0.2 * thirst;
        timeItTakesToMove = 2000 - 15 * reactivity;
        while(isAlive){
            try {
                Thread.sleep((long) (timeItTakesToMove));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                double normalizedHealth = Math.max(0, Math.min(health, 100)) / 100.0;
                this.setFill(Color.rgb((int) (255 * (1 - normalizedHealth)), (int) (255 * normalizedHealth), 0));
            });

            Platform.runLater(this::checkVitals);
            Platform.runLater(this::move);
            Platform.runLater(this::interact);
            hunger--;
            thirst--;
        }
        Platform.runLater(() -> pane.getChildren().remove(this));
    }

    private void checkVitals(){
        if(hunger <= 0 || thirst <= 0 || health <= 0){
            System.out.println(name + " has fucking died!");
            isAlive = false;
            Platform.runLater(() -> pane.getChildren().remove(this));
        }
    }

    private void move(){
        ArrayList<Cell> options = examineSurroundings();
        Cell nextStop = chooseNextStop(options);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(this.centerXProperty(), this.getCenterX()),
                        new KeyValue(this.centerYProperty(), this.getCenterY())),
                new KeyFrame(Duration.millis(timeItTakesToMove/2),
                        new KeyValue(this.centerXProperty(), nextStop.centerX),
                        new KeyValue(this.centerYProperty(), nextStop.centerY))
        );
        timeline.play();
    }

    private ArrayList<Cell> examineSurroundings(){
        ArrayList<Cell> surroundingCells = new ArrayList<>();
        String currentCellId = getCurrentCellId();
        String[] parts = currentCellId.split(",");
        int currentCellX = Integer.parseInt(parts[0]);
        int currentCellY = Integer.parseInt(parts[1]);

        for(Cell cell : GUI.cells) {
            parts = cell.getId().split(",");
            int cellX = Integer.parseInt(parts[0]);
            int cellY = Integer.parseInt(parts[1]);
            if(Math.abs(cellX - currentCellX) <= 1 && Math.abs(cellY - currentCellY) <= 1){
                surroundingCells.add(cell);
            }
        }
        return surroundingCells;
    }

    public String getCurrentCellId(){
        for (Cell cell : GUI.cells) {
            if (cell.contains(this.getCenterX(), this.getCenterY())) {
                return cell.getId();
            }
        }
        return null;
    }

    public Cell getCurrentCell(){
        for (Cell cell : GUI.cells) {
            if (cell.contains(this.getCenterX(), this.getCenterY())) {
                return cell;
            }
        }
        return null;
    }

    private Cell chooseNextStop(ArrayList<Cell> options){
        Random rand = new Random();
        Cell randomElement = options.get(rand.nextInt(options.size()));
        return randomElement;
    }

    private void interact(){
        if(getCurrentCell().getType() == "Water" && thirst < 80){
            if(Math.random() > 0.3){
                System.out.println(name + " is drinking water!");
                thirst = thirst + 20;
            } else {
                System.out.println(name + " fell in the water!");
                health = health - Math.floor(Math.random() * 30);
            }
        }
        if(getCurrentCell().getType() == "Forest" && hunger < 80){
            if(Math.random() > 0.3) {
                System.out.println(name + " is eating food from the forest!");
                hunger = hunger + 20;
            } else {
                System.out.println(name + " fell down a tree!");
                health = health - Math.floor(Math.random() * 30);
            }
        }
    }
}
