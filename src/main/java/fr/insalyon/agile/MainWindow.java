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
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class MainWindow extends Application
{
    final double sceneHeight = 750;
    final double bandeauWidth = 200;
    final double bandeauHeigth = sceneHeight;
    final double rightPaneWidth = 200;
    final double rightPaneHeigth = sceneHeight;
    final double mapWidth = 800;
    final double mapHeight = sceneHeight;
    final double sceneWidth = mapWidth+bandeauWidth ;


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

        // LeftVBox
        VBox leftVbox = new VBox();


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

        //Titre
        Label lblTimeline = new Label("Timeline");
        lblTimeline.setPadding(new Insets(10));
        //Right vBox
        VBox rightVbox = new VBox();
        rightVbox.getChildren().add(lblTimeline);
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPrefSize(bandeauWidth, bandeauHeigth);

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

        rightPane.getChildren().clear();

        //Todo : externaliser ça
        double centreRightPane = rightPaneWidth/2;
        double xPoint =  centreRightPane;
        double yFirstPoint = 50;
        double yLastPoint = rightPaneHeigth - 100;
        final int radiusAffichageTimeline = 11;
        double widthLabelTime = 75;
        double heightLabelTime = 9; //Todo : L'avoir dynamiquement ? ça a l'air chiant
        LocalTime heureDebutTournee = LocalTime.of(8,0);
        LocalTime heureFinTournee = LocalTime.of(
                tournee.mItineraires.get(tournee.mItineraires.size()-1).getTroncons().get(0).getOrigine().getLivraison().getDateLivraison().getHour() +
                tournee.mItineraires.get(tournee.mItineraires.size()-1).getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison().getHour() +
                tournee.mItineraires.get(tournee.mItineraires.size()-1).getDuree().getHour(),
                tournee.mItineraires.get(tournee.mItineraires.size()-1).getTroncons().get(0).getOrigine().getLivraison().getDateLivraison().getMinute() +
                        tournee.mItineraires.get(tournee.mItineraires.size()-1).getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison().getMinute() +
                        tournee.mItineraires.get(tournee.mItineraires.size()-1).getDuree().getMinute());
        final double deliveryWidth = 40.0;
        final double deliveryHeight = 40.0;
        final int decalageLabelLivraison = 25;

        System.out.println(heureFinTournee);

        //Titre
        Label lblTimeline = new Label("Timeline");
        lblTimeline.setPadding(new Insets(10));
        //Right vBox
        VBox rightVbox = new VBox();
        rightVbox.getChildren().add(lblTimeline);
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPrefSize(bandeauWidth, bandeauHeigth);


        //Point de départ et label
        Circle pointEntrepotDepart = new Circle(radiusAffichageTimeline);
        pointEntrepotDepart.setFill(Color.rgb(244,39,70));
        pointEntrepotDepart.relocate(xPoint - radiusAffichageTimeline,yFirstPoint - radiusAffichageTimeline);

        Label lblEntrepotDepartHeure = new Label(heureDebutTournee.toString());
        lblEntrepotDepartHeure.setLayoutX(centreRightPane - widthLabelTime);
        lblEntrepotDepartHeure.setLayoutY(yFirstPoint- heightLabelTime);
        lblEntrepotDepartHeure.setTextFill(Color.grayRgb(96));

        Label lblEntrepotDepart = new Label("Entrepôt");
        lblEntrepotDepart.setLayoutX(centreRightPane + decalageLabelLivraison);
        lblEntrepotDepart.setLayoutY(yFirstPoint- heightLabelTime);
        lblEntrepotDepart.setTextFill(Color.grayRgb(96));

        //Point d'arrivée
        Circle pointEntrepotArrivee = new Circle(radiusAffichageTimeline);
        pointEntrepotArrivee.setFill(Color.rgb(244,39,70));
        pointEntrepotArrivee.relocate(xPoint - radiusAffichageTimeline,yLastPoint - radiusAffichageTimeline);

        Label lblEntrepotArriveeHeure = new Label(heureFinTournee.toString());
        lblEntrepotArriveeHeure.setLayoutX(centreRightPane - widthLabelTime);
        lblEntrepotArriveeHeure.setLayoutY(yLastPoint- heightLabelTime);
        lblEntrepotArriveeHeure.setTextFill(Color.grayRgb(96));

        Label lblEntrepotArrivee = new Label("Entrepôt");
        lblEntrepotArrivee.setLayoutX(centreRightPane + decalageLabelLivraison);
        lblEntrepotArrivee.setLayoutY(yLastPoint- heightLabelTime);
        lblEntrepotArrivee.setTextFill(Color.grayRgb(96));

        int compteurLivraison = 1;
        double yRelocateFromLastPoint = yFirstPoint;
        Pane pointPane = new Pane();
        Pane linePane = new Pane();

        for (Itineraire itineraire: tournee.mItineraires) {

            if(itineraire.getTroncons().get(0).getOrigine().getType() != Point.Type.LIVRAISON){
                continue;
            }

            //Points
            Circle pointIti = new Circle(radiusAffichageTimeline);
            pointIti.setFill(Color.rgb(56, 120, 244));
            LocalTime heurex = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateArrivee();
            double yRelocate = ((localTimeToSecond(heurex) -  localTimeToSecond(heureDebutTournee)) / (localTimeToSecond(heureFinTournee) - localTimeToSecond(heureDebutTournee)))
                    * (yLastPoint - yFirstPoint)
                    + yFirstPoint;
            pointIti.relocate(xPoint - radiusAffichageTimeline, yRelocate - radiusAffichageTimeline);

            LocalTime heureLivraisonx = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison();
            double yRelocateLivraison = ((localTimeToSecond(heureLivraisonx) -  localTimeToSecond(heureDebutTournee)) / (localTimeToSecond(heureFinTournee) - localTimeToSecond(heureDebutTournee)))
                    * (yLastPoint - yFirstPoint)
                    + yFirstPoint;

            if (heurex != heureLivraisonx) {
                //Points
                Circle pointItiLivraison = new Circle(radiusAffichageTimeline);
                pointItiLivraison.setFill(Color.rgb(56, 120, 244));

                pointItiLivraison.relocate(xPoint - radiusAffichageTimeline, yRelocateLivraison - radiusAffichageTimeline);

                pointPane.getChildren().add(pointItiLivraison);

                Rectangle rectangle = new Rectangle(radiusAffichageTimeline * 2, yRelocateLivraison - yRelocate);
                rectangle.setFill(Color.rgb(56, 120, 244));
                rectangle.relocate(xPoint - radiusAffichageTimeline, yRelocate);

                pointPane.getChildren().add(rectangle);

                //Label arrivée
                DateTimeFormatter dtfArrivee = DateTimeFormatter.ofPattern("HH:mm");
                Label lblpointItiHeureArrivee = new Label(heurex.format(dtfArrivee));
                lblpointItiHeureArrivee.setLayoutX(centreRightPane - widthLabelTime);
                lblpointItiHeureArrivee.setLayoutY(yRelocate - heightLabelTime);
                lblpointItiHeureArrivee.setTextFill(Color.grayRgb(96));

                //Label Livraison machintruc
                Label lblpointItiArrivee = new Label("Arrivée " +compteurLivraison);
                lblpointItiArrivee.setLayoutX(centreRightPane + decalageLabelLivraison);
                lblpointItiArrivee.setLayoutY(yRelocate - heightLabelTime);
                lblpointItiArrivee.setTextFill(Color.grayRgb(96));

                rightPane.getChildren().add(lblpointItiHeureArrivee);
                rightPane.getChildren().add(lblpointItiArrivee);
            }

            //Label heure
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            Label lblpointItiHeure = new Label(heureLivraisonx.format(dtf));
            lblpointItiHeure.setLayoutX(centreRightPane - widthLabelTime);
            lblpointItiHeure.setLayoutY(yRelocateLivraison - heightLabelTime);
            lblpointItiHeure.setTextFill(Color.grayRgb(96));

            //Label Livraison machintruc
            Label lblpointItiLivraison = new Label("Livraison " +compteurLivraison);
            lblpointItiLivraison.setLayoutX(centreRightPane + decalageLabelLivraison);
            lblpointItiLivraison.setLayoutY(yRelocateLivraison - heightLabelTime);
            lblpointItiLivraison.setTextFill(Color.grayRgb(96));

            compteurLivraison++;

            // 3lignes d'accroche

            //lignes
            Line line = new Line();
            line.setStroke(Color.grayRgb(133));
            line.setStrokeWidth(1);
            //line.getStrokeDashArray().addAll(4d); //pointillés
            line.setStartX(xPoint);
            line.setStartY(yRelocateFromLastPoint);
            line.setEndX(xPoint);

            //Cas spécial dernier troncon
            if(Point.Type.ENTREPOT == itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getType()){
                line.setEndY(yLastPoint);
            }
            else{
                line.setEndY(yRelocate);
            }

            yRelocateFromLastPoint = yRelocateLivraison;

            //Affichage
            pointPane.getChildren().add(pointIti);
            linePane.getChildren().add(line);
            rightPane.getChildren().add(lblpointItiHeure);
            rightPane.getChildren().add(lblpointItiLivraison);
        }




        //Voiture
        final String test = new File("..").toURI().toString();
        final String imageURI = new File("images/delivery-icon.jpg").toURI().toString();
        final Image image = makeTransparent(new Image(imageURI, deliveryWidth, deliveryWidth, true, false));
        final ImageView imageView = new ImageView(image);
        imageView.relocate(centreRightPane - deliveryWidth/2,yFirstPoint - image.getHeight()/2);

        imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });


        //bouton modifier
        Button modifierTimeline = new Button();
        modifierTimeline.setText("Modifier");
        modifierTimeline.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

            }
        });
        //Right vBox
        VBox rightVboxDown = new VBox();
        rightVboxDown.getChildren().add(modifierTimeline);
        rightVboxDown.setAlignment(Pos.BOTTOM_CENTER);
        rightVboxDown.setPadding(new Insets(35));
        rightVboxDown.setPrefSize(bandeauWidth, bandeauHeigth);

        //Affichage
        rightPane.getChildren().add(lblEntrepotDepartHeure);
        rightPane.getChildren().add(lblEntrepotDepart);
        rightPane.getChildren().add(lblEntrepotArriveeHeure);
        rightPane.getChildren().add(lblEntrepotArrivee);
        pointPane.getChildren().add(pointEntrepotDepart);
        pointPane.getChildren().add(pointEntrepotArrivee);
        rightPane.getChildren().add(rightVbox);
        rightPane.getChildren().add(rightVboxDown);
        rightPane.getChildren().add(linePane);
        rightPane.getChildren().add(pointPane);
        rightPane.getChildren().add(imageView);
    }

    public Image makeTransparent(Image inputImage) {
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);



                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if (r >= 0xFF
                        && g >= 0xFF
                        && b >= 0xFF) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }
        return outputImage;
    }

    private double localTimeToMinute(LocalTime time){
        return (time.getHour()*60 + time.getMinute());
    }
    private double localTimeToSecond(LocalTime time){
        return (time.getHour()*60*60 + time.getMinute()*60 + time.getSecond());
    }
}