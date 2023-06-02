import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import java.util.Objects;

public class Cell extends Rectangle implements Runnable {

    private String type;
    public int gridX;
    public int gridY;
    public double centerX, centerY;
    public boolean isAvailible;
    public Cell(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.setOnMouseClicked(this::handleMouseClick);
        this.centerX = x + width/2;
        this.centerY = y + height/2;
    }
    @Override
    public void run() {
        Platform.runLater(this::rollType);
        while(isAvailible){

        }
        setFill(Color.RED);
    }

    private void rollType() {
        double result = Math.random();
        if (result < 0.01) {
            type = "Cornucopia";
        } else if (result < 0.3) {
            type = "Forest";
        } else if (result < 0.4) {
            type = "Water";
        } else if (result < 0.55) {
            type = "Desert";
        } else if (result < 0.7) {
            type = "Mountains";
        } else {
            type = "Plains";
        }

        if(Objects.equals(type, "Cornucopia")){
            this.setFill(Color.web("#5D3A0F"));
        } else if(Objects.equals(type, "Forest")){
            this.setFill(Color.web("#1a3910"));
        } else if(Objects.equals(type, "Water")){
            this.setFill(Color.web("#1B6481"));
        } else if(Objects.equals(type, "Desert")){
            this.setFill(Color.web("#BFAE1E"));
        } else if(Objects.equals(type, "Mountains")){
            this.setFill(Color.web("#6E6E6E"));
        } else {
            this.setFill(Color.web("#33721F"));
        }
    }

    public String getType(){
        return type;
    }

    private void handleMouseClick(MouseEvent event){
        System.out.println("Type: " + type + ", X: " + gridX + ", Y: " + gridY);
    }
}
