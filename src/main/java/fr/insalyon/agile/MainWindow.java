package fr.insalyon.agile;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class MainWindow extends Application
{
    final double bandeauWidth = 200;
    final double bandeauHeigth = 800;
    final double rightPaneWidth = 200;
    final double rightPaneHeigth = 800;
    final double mapWidth = 800;
    final double mapHeight = 800;
    final double sceneWidth = mapWidth+bandeauWidth ;
    final double sceneHeight = mapHeight;

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

        //Label
        Label lblTitlePlan = new Label("Plan");
        Label lblTitleDL = new Label("Demande Livraison");
        Label lblTimeline = new Label("Timeline");
        lblTimeline.setPadding(new Insets(10));

        // LeftVBox
        VBox leftVbox = new VBox();

        //Right vBox
        VBox rightVbox = new VBox();
        rightVbox.getChildren().add(lblTimeline);
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPrefSize(bandeauWidth, bandeauHeigth);

        //timeLineBuild();

        //Partie Plan du bandeau
        leftVbox.getChildren().add(lblTitlePlan);
        comboBoxPlan.setPromptText("planLyonPetit");
        leftVbox.getChildren().add(comboBoxPlan);
        comboBoxPlan.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                mapPane.getChildren().clear();
                plan = parser.parsePlan("fichiersXML/"+ t1 +".xml");
                plan.print(mapPane, primaryStage);
            }
        });

        //Partie Demande Livraison du bandeau
        comboBoxDemandeLivraison.setPromptText("Choisir une DL");
        comboBoxDemandeLivraison.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(t1.equals(DLOptions.get(0)))
                {
                    mapPane.getChildren().clear();
                    plan.print(mapPane, primaryStage);
                    return;
                }

                plan.resetTypePoints();
                ddl = parser.parseDemandeDeLivraison("fichiersXML/"+t1+".xml");
                if (ddl == null) {
                    return;
                }

                mapPane.getChildren().clear();
                plan.print(mapPane, primaryStage);
            }
        });

        //Right Pane
        Pane rightPane = new Pane();
        rightPane.getChildren().add(rightVbox);

        Button btnCalculerTournee = new Button();
        btnCalculerTournee.setText("Calculer tournée");
        leftVbox.setSpacing(20);
        btnCalculerTournee.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                tournee = ddl.calculerTournee();
                mapPane.getChildren().clear();
                timeLineBuild(rightPane, tournee);
                plan.print(mapPane, primaryStage);
                tournee.print(mapPane, primaryStage);
            }
        });


        // --------------------------------
        //VBOX

        leftVbox.getChildren().add(lblTitleDL);
        leftVbox.getChildren().add(comboBoxDemandeLivraison);
        leftVbox.getChildren().add(btnCalculerTournee);
        leftVbox.setPrefSize(bandeauWidth, bandeauHeigth);
        leftVbox.setAlignment(Pos.CENTER);

        //Left Pane
        Pane leftPane = new Pane();
        leftPane.getChildren().add(leftVbox);



        plan.print(mapPane, primaryStage);

        BorderPane root = new BorderPane();
        root.setRight(rightPane);
        root.setCenter(mapPane);
        root.setLeft(leftPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void timeLineBuild(Pane rightPane, Tournee tournee){

        //Todo : externaliser ça
        double xPoint =  rightPaneWidth/2; //TODO : bug
        double yFirstPoint = 50;
        double yLastPoint = 700;
        final int radiusAffichageTimeline = 11;
        double widthLabelTime = 56;
        double heightLabelTime = 7; //Todo : L'avoir dynamiquement ? ça a l'air chiant
        LocalTime heureDebutTournee = LocalTime.of(8,0);
        LocalTime heureFinTournee = LocalTime.of(18,0);


        //Point de départ et label
        Circle pointEntrepotDepart = new Circle(radiusAffichageTimeline);
        pointEntrepotDepart.setFill(Color.rgb(244,39,70));
        pointEntrepotDepart.relocate(xPoint - radiusAffichageTimeline,yFirstPoint - radiusAffichageTimeline);

        Label lblEntrepotDepart = new Label(heureDebutTournee.toString()); //Todo : rendre dynamique
        lblEntrepotDepart.setLayoutX(xPoint - widthLabelTime);
        lblEntrepotDepart.setLayoutY(yFirstPoint- heightLabelTime);
        lblEntrepotDepart.setTextFill(Color.grayRgb(96));

        //Point d'arrivée
        Circle pointEntrepotArrivee = new Circle(radiusAffichageTimeline);
        pointEntrepotArrivee.setFill(Color.rgb(244,39,70));
        pointEntrepotArrivee.relocate(xPoint - radiusAffichageTimeline,yLastPoint - radiusAffichageTimeline);

        Label lblEntrepotArrivee = new Label(heureFinTournee.toString()); //Todo : rendre dynamique
        lblEntrepotArrivee.setLayoutX(xPoint - widthLabelTime);
        lblEntrepotArrivee.setLayoutY(yLastPoint- heightLabelTime);
        lblEntrepotArrivee.setTextFill(Color.grayRgb(96));

        //LigneTest -> à virer
        Line line = new Line();
        line.setStroke(Color.grayRgb(133));
        line.setStrokeWidth(1);
        line.getStrokeDashArray().addAll(4d);
        line.setStartX(xPoint);
        line.setStartY(yFirstPoint);
        line.setEndX(xPoint);
        line.setEndY(yLastPoint);

        System.out.println(tournee.livraisons.isEmpty());
        //Vrai ligne
        for (Point pointLivraison: tournee.livraisons) {
            System.out.println("coucou2");

            if(pointLivraison.getType() != Point.Type.LIVRAISON){
                continue;
            }

            //Points
            Circle pointIti = new Circle(radiusAffichageTimeline);
            pointEntrepotArrivee.setFill(Color.rgb(56, 120, 244));
            System.out.println(heureDebutTournee.getSecond());//pointLivraison.getLivraison().getDateArrivee().getSecond();
            pointEntrepotArrivee.relocate(xPoint - radiusAffichageTimeline,  - radiusAffichageTimeline);

            //Label


            //lignes
        }

        //Voiture


        //Affichage
        rightPane.getChildren().add(line);
        rightPane.getChildren().add(lblEntrepotDepart);
        rightPane.getChildren().add(lblEntrepotArrivee);
        rightPane.getChildren().add(pointEntrepotDepart);
        rightPane.getChildren().add(pointEntrepotArrivee);

    }
}