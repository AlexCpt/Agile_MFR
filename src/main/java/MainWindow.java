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

import java.util.ArrayList;
import java.util.List;


public class MainWindow extends Application
{
    final int mapWidth = 800;
    final int mapHeight = 600;
    Plan plan;


    public MainWindow(){
        System.out.println("Hello");
        ParserXML parser = new ParserXML();
        plan = parser.parsePlan("fichiersXML/planLyonPetit.xml");

        /*List<Point> points = new ArrayList<>();
        points.add(new Point("70",70,70));
        points.add(new Point("71",200,200));

        List<Troncon> troncons = new ArrayList<>();
        troncons.add(new Troncon(new Point("70",70,70), new Point("71",200,200), 56,"Lala"));

        plan = new Plan(points, troncons);*/
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();

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

    public void loadMap(Plan _plan){
        plan = _plan;
    }


}

//to merge

