import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


public class MainWindow extends Application
{
    final int mapWidth = 800;
    final int mapHeight = 600;
    Plan plan;

    public MainWindow(Plan _plan){
        this.plan = _plan;
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("LE NOM DE NOTRE APPLI QUOI");

        Pane mapPane = new Pane();
        mapPane.setStyle("-fx-background-color: #fffadd;");
        mapPane.setPrefSize(mapWidth,mapHeight);
        Circle circle = new Circle(50, Color.RED);
        circle.relocate(20,20);
        mapPane.getChildren().add(circle);

        plan.print(mapPane);

        Scene scene = new Scene(mapPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

