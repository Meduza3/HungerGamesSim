import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
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
    double timeItTakesToMove;
    private final Pane pane;
    private final TextArea console;
    private final Text nameText;
    private Timeline timeline;
    ArrayList<Item> inventory = new ArrayList<>();
    Tribute(Pane pane, TextArea console, GridPane inventoryDisplay, double x, double y, String name) {
        this.setCenterX((x - 1) * GUI.cellWidth + GUI.cellWidth / 2);
        this.setCenterY((y - 1) * GUI.cellHeight + GUI.cellHeight / 2);
        this.setRadius(20);
        this.setOnMouseClicked(this::handleMouseClick);
        this.setOnMouseEntered(this::handleMouseHover);
        this.name = name;
        this.pane = pane;
        this.console = console;
        nameText = new Text(name);
        nameText.setX(this.getCenterX()-15);
        nameText.setY(this.getCenterY()+20);
        pane.getChildren().add(nameText);
    }

    private void handleMouseClick(MouseEvent mouseEvent) {
        System.out.println("Name: " + name + ", Health: " + health + ", Hunger: " + hunger + ", Thirst: " + thirst);
    }

    private void handleMouseHover(MouseEvent mouseEvent) {
        ArrayList<Node> paneChildren = new ArrayList<>(pane.getParent().getChildrenUnmodifiable());
        for (Node node : paneChildren) {
            if (node != null && node.getId() != null && node.getId().equals("info")) {
                if (node instanceof Pane) {
                    for (Node child : ((Pane) node).getChildren()) {
                        if (child instanceof Text) {
                            if (child.getId().equals("infoName")) {
                                ((Text) child).setText(name);
                            } else if (child.getId().equals("infoHealth")) {
                                ((Text) child).setText("Health: " + health);
                            } else if (child.getId().equals("infoHunger")) {
                                ((Text) child).setText("Hunger: " + hunger);
                            } else if (child.getId().equals("infoThirst")) {
                                ((Text) child).setText("Thirst: " + thirst);
                            } else if (child.getId().equals("infoEq")) {
                                ((Text) child).setText("Equipment: " + inventory.toString());
                            }
                        } else if (child instanceof Circle) {
                            ((Circle) child).setFill(this.getFill());
                            ((Circle) child).setStroke(this.getStroke());
                        }
                    }
                }
            }
        }
        updateInventoryDisplay();
    }

    void updateInventoryDisplay(){

    }

    @Override
    public void run() {
        this.setStrokeWidth(4);
        this.setStroke(Color.rgb((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random())));

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while(isAlive){
            reactivity = 0.6 * health + 0.2 * hunger + 0.2 * thirst;
            timeItTakesToMove = 2000 - 10 * reactivity;

            try {
                Thread.sleep((long) (Math.random() * timeItTakesToMove + timeItTakesToMove / 2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater(this::checkVitals);
            Platform.runLater(this::move);
            Platform.runLater(this::interact);
            Platform.runLater(this::useItem);
            hunger--;
            thirst--;
        }

        Platform.runLater(() -> pane.getChildren().remove(this));
    }
    private void useItem(){
        if(hunger < 20 && inventory.contains(Item.FRUIT)){
            inventory.remove(Item.FRUIT);
            hunger += 20;
        } else if(thirst < 20 && inventory.contains(Item.WATER)){
            inventory.remove(Item.WATER);
            thirst += 20;
        } if(inventory.contains(Item.SWORD)){
            for(Node tribute : pane.getChildren()){
                if(tribute instanceof Tribute){
                    if(Objects.equals(((Tribute) tribute).getCurrentCellId(), getCurrentCellId()) && !Objects.equals(((Tribute) tribute).name, name)){
                        System.out.println(name + " has killed " + ((Tribute) tribute).name + "!");
                        console.appendText(name + " has killed " + ((Tribute) tribute).name + "!\n");
                        ((Tribute) tribute).isAlive = false;
                    }
                }
            }
        } if(inventory.contains(Item.MEDICINE) && health < 50){
            inventory.remove(Item.MEDICINE);
            health += 30;
            System.out.println(name + " heals wounds.");
            console.appendText(name + " heals wounds.\n");
        }
    }

    private void checkVitals(){
        Platform.runLater(() -> {
            double normalizedHealth = Math.max(0, Math.min(health, 100)) / 100.0;
            this.setFill(Color.rgb((int) (255 * (1 - normalizedHealth)), (int) (255 * normalizedHealth), 0));
        });

        if(hunger <= 0 || thirst <= 0){
            health = health - 1;
            hunger = thirst = 0;
        }

        if(health <= 0){
            System.out.println(name + " has died!");
            console.appendText(name + " has died!\n");
            isAlive = false;
            Platform.runLater(() -> {
                pane.getChildren().remove(this);
                pane.getChildren().remove(nameText);
                timeline.stop();
            });
        }
    }

    private void move(){
        ArrayList<Cell> options = examineSurroundings();
        Cell nextStop = chooseNextStop(options);

        timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(this.centerXProperty(), this.getCenterX()),
                        new KeyValue(this.centerYProperty(), this.getCenterY()),
                        new KeyValue(nameText.xProperty(), nameText.getX()),
                        new KeyValue(nameText.yProperty(), nameText.getY())),
                new KeyFrame(Duration.millis(timeItTakesToMove/2),
                        new KeyValue(this.centerXProperty(), nextStop.centerX),
                        new KeyValue(this.centerYProperty(), nextStop.centerY),
                        new KeyValue(nameText.xProperty(), nextStop.centerX - 20),
                        new KeyValue(nameText.yProperty(), nextStop.centerY + this.getRadius() + 15))
        );

        timeline.play();
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
        return options.get(rand.nextInt(options.size()));
    }

    private void interact(){
        if(Objects.equals(getCurrentCell().getType(), "Water") && thirst < 80 && !inventory.contains(Item.WATER)){
            if(Math.random() > 0.3){
                System.out.println(name + " is collecting sweet lake water!");
                //console.appendText(name + " is collecting sweet lake water!\n");
                inventory.add(Item.WATER);
            } else {
                System.out.println(name + " fell in the water!");
                console.appendText(name + " fell in the water!\n");
                health = health - Math.floor(Math.random() * 30);
            }
        }
        if(Objects.equals(getCurrentCell().getType(), "Forest") && hunger < 80 && !inventory.contains(Item.FRUIT)){
            if(Math.random() > 0.3) {
                System.out.println(name + " is collecting food from the forest!");
                //console.appendText(name + " is collecting food from the forest!\n");
                inventory.add(Item.FRUIT);
            } else {
                System.out.println(name + " fell down a tree!");
                console.appendText(name + " fell down a tree!\n");
                health = health - Math.floor(Math.random() * 30);
            }
        }if(Objects.equals(getCurrentCell().getType(), "Cornucopia") && !inventory.contains(Item.SWORD) && Math.random() < 0.8){
            System.out.println(name + " is collecting a weapon from the cornucopia!");
            console.appendText(name + " is collecting a weapon from the cornucopia!\n");
            inventory.add(Item.SWORD);
        }if(getCurrentCell().getType().equals("Cornucopia") && !inventory.contains(Item.MEDICINE) && Math.random() < 0.9){
            System.out.println(name + " is collecting medicine from the cornucopia!");
            console.appendText(name + " is collecting medicine from the cornucopia!\n");
            inventory.add(Item.MEDICINE);
        } for(Node tribute : pane.getChildren()){
            if(tribute instanceof Tribute){
                if(Objects.equals(((Tribute) tribute).getCurrentCellId(), getCurrentCellId()) && !Objects.equals(((Tribute) tribute).name, name) && !((Tribute) tribute).inventory.contains(Item.SWORD) && !this.inventory.contains(Item.SWORD) && Math.random() < 0.3){
                    System.out.println(name + " and " + ((Tribute) tribute).name + " got into a fist fight!");
                    console.appendText(name + " and " + ((Tribute) tribute).name + " got into a fist fight!\n");
                    this.health = health - 15;
                    ((Tribute) tribute).health = health - 15;
                }
            }
        }
    }
}