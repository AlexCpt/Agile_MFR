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
    final int sceneHeight = mapHeight;

    Plan plan;
    ParserXML parser;
    Tournee tournee;
    DemandeDeLivraison ddl;


    public MainWindow(){
        parser = new ParserXML();

        plan = parser.parsePlan("fichiersXML/planLyonGrand.xml");

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

        //Button
        Button btn = new Button();
        btn.setText("Charger demande de livraison");
        btn.setOnAction(event -> {
            ddl = parser.parseDemandeDeLivraison("fichiersXML/DLgrand10.xml");
            plan.print(mapPane);
        });

        Button btn2 = new Button();
        btn2.setText("Calculer tournée");
        btn2.setOnAction(event -> {
            if (ddl == null) {
                return;
            }
            tournee = ddl.calculerTournee(plan);
            tournee.print(mapPane);
        });

        // LeftHighVBox
        VBox Vbox = new VBox();
        Vbox.getChildren().add(lblTitle);
        Vbox.getChildren().add(btn);
        Vbox.getChildren().add(btn2);
        Vbox.setPrefSize(bandeauWidth, bandeauHeigth);
        Vbox.setLayoutY(150);
        Vbox.setAlignment(Pos.TOP_CENTER);

        //Left Pane
        Pane leftPane = new Pane();
        leftPane.getChildren().add(Vbox);

        plan.print(mapPane);

        BorderPane root = new BorderPane();
        root.setRight(mapPane);
        root.setLeft(leftPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}