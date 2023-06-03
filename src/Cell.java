import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;

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

        switch (type) {
            case "Cornucopia":
                this.setFill(Color.web("#5D3A0F"));
                break;
            case "Forest":
                this.setFill(Color.web("#1a3910"));
                break;
            case "Water":
                this.setFill(Color.web("#1B6481"));
                break;
            case "Desert":
                this.setFill(Color.web("#BFAE1E"));
                break;
            case "Mountains":
                this.setFill(Color.web("#6E6E6E"));
                break;
            default:
                this.setFill(Color.web("#33721F"));
                break;
        }
    }

    public String getType(){
        return type;
    }

    private void handleMouseClick(MouseEvent event){
        System.out.println("Type: " + type + ", X: " + gridX + ", Y: " + gridY);
    }
}
