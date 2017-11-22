import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainWindow extends Application
{

    final int bandeauWidth = 200;
    final int bandeauHeigth = 800;
    final int mapWidth = 800;
    final int mapHeight = 800;
    final int sceneWidth = mapWidth+bandeauWidth ;
    final int sceneHeight = mapHeight+bandeauHeigth;

    Plan plan;
    Tournee tournee;


    public MainWindow(){
        ParserXML parser = new ParserXML();

        plan = parser.parsePlan("fichiersXML/planLyonPetit.xml");

    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("HexaMaps 1.0");
        primaryStage.setMinHeight(sceneHeight);
        primaryStage.setMinWidth(sceneWidth);


        //MapPane
        Pane mapPane = new Pane();
        mapPane.setStyle("-fx-background-color: #cccbc1;");
        mapPane.setPrefSize(mapWidth,mapHeight);
        mapPane.setMinSize(mapWidth,mapHeight);
        mapPane.setMaxHeight(mapHeight);
        mapPane.setMaxWidth(mapWidth);

        mapPane.setLayoutX(sceneWidth - mapWidth);
        mapPane.setLayoutY(0);


        //Label Title
        Label lblTitle = new Label("Lyon - Livraison");
        lblTitle.setLayoutY(100);

        //Button
        Button btn = new Button();
        btn.setText("Charger Livraison");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Charger Livraison");
            }
        });

        //LeftHighVbox
        VBox highVbox = new VBox();
        highVbox.getChildren().add(lblTitle);
        highVbox.setPrefSize(bandeauWidth, bandeauHeigth);
        highVbox.setAlignment(Pos.CENTER);

        //LeftBotVbox
        VBox botVbox = new VBox();
        botVbox.getChildren().add(btn);
        botVbox.setPrefSize(bandeauWidth, bandeauHeigth);
        botVbox.setAlignment(Pos.CENTER);

        //Left Pane
        BorderPane leftPane = new BorderPane();
        leftPane.setTop(highVbox);
        leftPane.setBottom(botVbox);

        plan.print(mapPane);
        //tournee.print(mapPane);

        BorderPane root = new BorderPane();
        root.setRight(mapPane);
        root.setLeft(leftPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}