import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;



public class MainWindow extends Application
{
    final int mapWidth = 800;
    final int mapHeight = 800;
    Plan plan;
    ParserXML parser;
    Tournee tournee;


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
        primaryStage.setTitle("LE NOM DE NOTRE APPLI QUOI");

        Pane mapPane = new Pane();
        mapPane.setStyle("-fx-background-color: #cccbc1;");
        mapPane.setPrefSize(mapWidth,mapHeight);

        Button btn = new Button();
        btn.setText("Calculer tournée");
        btn.setOnAction(event -> {
            DemandeDeLivraison ddl = parser.parseDemandeDeLivraison("fichiersXML/DLgrand10.xml");
            plan.print(mapPane);
            tournee = ddl.calculerTournee(plan);
            tournee.print(mapPane);
        });

        plan.print(mapPane);

        StackPane root = new StackPane();
        root.getChildren().add(mapPane);
        root.getChildren().add(btn);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
