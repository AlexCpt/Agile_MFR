import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;


public class MainWindow extends Application
{

    final int bandeauWidth = 200;
    final int bandeauHeigth = 800;
    final int mapWidth = 800;
    final int mapHeight = 800;
    final int sceneWidth = mapWidth+bandeauWidth ;
    final int sceneHeight = mapHeight;

    ObservableList<String> planOptions =
            FXCollections.observableArrayList(
                    "planLyonPetit",
                    "planLyonMoyen",
                    "planLyonGrand"
            );
    final ComboBox comboBoxPlan = new ComboBox(planOptions);

    ObservableList<String> DLOptions =
            FXCollections.observableArrayList(
                    "Choisir une DL",
                    "DLgrand10",
                    "DLgrand10TW2",
                    "DLgrand20",
                    "DLgrand20TW2",
                    "DLmoyen5",
                    "DLmoyen5TW1",
                    "DLmoyen5TW4",
                    "DLmoyen10",
                    "DLmoyen10TW3",
                    "DLpetit3",
                    "DLpetit5"
            );
    final ComboBox comboBoxDemandeLivraison = new ComboBox(DLOptions);

    Plan plan;
    ParserXML parser;
    Tournee tournee;
    DemandeDeLivraison ddl;


    public MainWindow(){
        parser = new ParserXML();

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
        Label lblTitlePlan = new Label("Plan");
        Label lblTitleDL = new Label("Demande Livraison");

        // LeftHighVBox
        VBox Vbox = new VBox();

        //Partie Plan du bandeau
        Vbox.getChildren().add(lblTitlePlan);
        comboBoxPlan.setPromptText("planLyonPetit");
        Vbox.getChildren().add(comboBoxPlan);
        comboBoxPlan.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                mapPane.getChildren().clear();
                plan = parser.parsePlan("fichiersXML/"+ t1 +".xml");
                plan.print(mapPane);
            }
        });

        //Partie DL du bandeau
        comboBoxDemandeLivraison.setPromptText("Choisir une DL");
        comboBoxDemandeLivraison.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(t1.equals(DLOptions.get(0)))
                {
                    mapPane.getChildren().clear();
                    plan.print(mapPane);
                    return;
                }

                plan.resetTypePoints();
                ddl = parser.parseDemandeDeLivraison("fichiersXML/"+t1+".xml");
                if (ddl == null) {
                    return;
                }

                mapPane.getChildren().clear();
                plan.print(mapPane);
                tournee.print(mapPane);
            }
        });

        Button btnCalculerTournee = new Button();
        btnCalculerTournee.setText("Calculer tournée");
        Vbox.setSpacing(20);
        btnCalculerTournee.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                tournee = ddl.calculerTournee(plan);
                mapPane.getChildren().clear();
                plan.print(mapPane);
                tournee.print(mapPane);
            }
        });

        // --------------------------------

        //Create PopOver and add look and feel
        PopOver popOver = new PopOver(Vbox);


        Label label = new Label("Mouse mouse over me");
        label.setOnMouseEntered(mouseEvent -> {
            //Show PopOver when mouse enters label
            //popOver.show(lblTitlePlan);
        });

        label.setOnMouseExited(mouseEvent -> {
            //Hide PopOver when mouse exits label
            popOver.hide();
        });


        Vbox.getChildren().add(lblTitleDL);
        Vbox.getChildren().add(comboBoxDemandeLivraison);
        Vbox.getChildren().add(btnCalculerTournee);
        Vbox.setPrefSize(bandeauWidth, bandeauHeigth);
        Vbox.setAlignment(Pos.CENTER);

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