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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class MainWindow extends Application
{
    final static double sceneHeight = 750;
    final static double bandeauWidth = 200;
    final static double bandeauHeigth = sceneHeight;
    final static double rightPaneWidth = 200;
    final static double rightPaneHeigth = sceneHeight;
    final static double mapWidth = 800;
    final static double mapHeight = sceneHeight;
    final static double sceneWidth = mapWidth+bandeauWidth ;
    final static int radiusAffichageTimeline = 11;
    String fileName;

    // RightPane
    static double  centreRightPane = rightPaneWidth/2;
    double  xPoint =  centreRightPane;
    double yFirstPoint = 50;
    double yLastPoint = rightPaneHeigth - 100;
    static  double widthLabelTime = 75;
    double heightLabelTime = 9; //Todo : L'avoir dynamiquement ? ça a l'air chiant
    double deliveryWidth = 40.0;
    double deliveryHeight = 40.0;

    double orgSceneY;
    double orgTranslateY;


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
                plan.print(mapPane);
            }
        });

        //Partie Demande Livraison du bandeau
        comboBoxDemandeLivraison.setPromptText("Choisir une DL");
        comboBoxDemandeLivraison.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                if(t1.equals(DLOptions.get(0)))
                {
                    mapPane.getChildren().clear();
                    plan.print(mapPane);
                    return;
                }

                fileName = t1;
                plan.resetTypePoints();
                ddl = parser.parseDemandeDeLivraison("fichiersXML/"+t1+".xml");
                if (ddl == null) {
                    return;
                }

                mapPane.getChildren().clear();
                plan.print(mapPane);
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
                timeLineBuild(rightPane, tournee,mapPane,primaryStage, false);
                plan.print(mapPane);
                tournee.print(mapPane);
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

        plan.print(mapPane);

        BorderPane root = new BorderPane();
        root.setRight(rightPane);
        root.setCenter(mapPane);
        root.setLeft(leftPane);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void timeLineBuild(Pane rightPane, Tournee tournee, Pane mapPane, Stage primaryStage, boolean modeModifier){

        rightPane.getChildren().clear();

        //Todo : externaliser ça

        LocalTime heureDebutTournee = LocalTime.of(8,0);

        LocalTime heureFinTournee =
                tournee.getItineraires().get(tournee.getItineraires().size()-1).getTroncons().get(0).getOrigine().getLivraison().getDateLivraison()
                        .plus(tournee.getItineraires().get(tournee.getItineraires().size()-1).getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison())
                        .plus(tournee.getItineraires().get(tournee.getItineraires().size()-1).getDuree());

        final double dragAndDropWidth = 20;
        final double dragAndDropHeight = 20;

        final int decalageXIconDragAndDropPoint = 20;
        final int decalageYIconDragAndDropPoint = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");


        String popOverButtonStyle = //"-fx-background-radius: 5em; " +
                "-fx-min-width: " + radiusAffichageTimeline*10 + "px; " +
                "-fx-min-height: " + radiusAffichageTimeline*6 + "px; " +
                "-fx-max-width: " + radiusAffichageTimeline*10 + "px; " +
                "-fx-max-height: " + radiusAffichageTimeline*6 + "px; " +
                "-fx-background-color: transparent;" +
                "-fx-background-insets: 0px; " +
                "-fx-padding: 0px;";

        //Titre
        Label lblTimeline = new Label("Timeline");
        lblTimeline.setPadding(new Insets(10));
        //Right vBox
        VBox rightVbox = new VBox();
        rightVbox.getChildren().add(lblTimeline);
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPrefSize(bandeauWidth, bandeauHeigth);


        //Point de départ et label -------------------------------------
        Label lblEntrepotDepartHeure = new Label(heureDebutTournee.toString());
        lblEntrepotDepartHeure.setLayoutY(yFirstPoint- heightLabelTime);

        Label lblEntrepotDepart = new Label("Entrepôt");
        lblEntrepotDepart.setLayoutY(yFirstPoint- heightLabelTime);

        PointLivraisonUI pointEntrepotDepart = new PointLivraisonUI(xPoint,
                yFirstPoint, PointLivraisonUI.Type.ENTREPOT_DEPART,lblEntrepotDepartHeure, lblEntrepotDepart);

        tournee.getDemandeDeLivraison().getEntrepot().printHover(mapPane,primaryStage,pointEntrepotDepart.getButton(),
                "Entrepot - Depart : "+ heureDebutTournee.toString() );


        //Point d'arrivée -------------------------------------
        Label lblEntrepotArriveeHeure = new Label(heureFinTournee.toString());
        lblEntrepotArriveeHeure.setLayoutY(yLastPoint- heightLabelTime);

        Label lblEntrepotArrivee = new Label("Entrepôt");
        lblEntrepotArrivee.setLayoutY(yLastPoint- heightLabelTime);

        PointLivraisonUI pointEntrepotArrivee = new PointLivraisonUI(xPoint,
                yLastPoint, PointLivraisonUI.Type.ENTREPOT_DEPART,lblEntrepotArriveeHeure, lblEntrepotArrivee);

        tournee.getDemandeDeLivraison().getEntrepot().printHover(mapPane,primaryStage,pointEntrepotArrivee.getButton(),
                "Entrepot - Arrivee : "+ heureFinTournee.toString() );


        // Livraisons intermédiaires -------------------------------------
        int compteurLivraison = 1;
        double yRelocateFromLastPoint = yFirstPoint;
        Pane pointPane = new Pane();
        Pane linePane = new Pane();
        Pane accrochePointPane = new Pane();
        Pane labelPane = new Pane();


        for (Itineraire itineraire: tournee.getItineraires()) {

            if(itineraire.getTroncons().get(0).getOrigine().getType() != Point.Type.LIVRAISON){
                continue;
            }

            LocalTime heurex = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateArrivee();
            double yRelocate = ((localTimeToSecond(heurex) -  localTimeToSecond(heureDebutTournee)) / (localTimeToSecond(heureFinTournee) - localTimeToSecond(heureDebutTournee)))
                    * (yLastPoint - yFirstPoint)
                    + yFirstPoint;

            LocalTime heureLivraisonx = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison();
            double yRelocateLivraison = ((localTimeToSecond(heureLivraisonx) -  localTimeToSecond(heureDebutTournee)) / (localTimeToSecond(heureFinTournee) - localTimeToSecond(heureDebutTournee)))
                    * (yLastPoint - yFirstPoint)
                    + yFirstPoint;

            if (heurex != heureLivraisonx) { //Point oblong

                Rectangle rectangle = new Rectangle(radiusAffichageTimeline * 2, yRelocateLivraison - yRelocate);
                rectangle.relocate(xPoint - radiusAffichageTimeline, yRelocate);

                //Label arrivée
                Label lblpointItiHeureArrivee = new Label(heurex.format(dtf));
                lblpointItiHeureArrivee.setLayoutY(yRelocate - heightLabelTime);

                //Label Livraison machintruc
                Label lblpointItiArrivee = new Label("Arrivée " +compteurLivraison);
                lblpointItiArrivee.setLayoutY(yRelocate - heightLabelTime);

                PointLivraisonUI_Oblong pointLivraisonUI_oblong = new PointLivraisonUI_Oblong(xPoint, yRelocateLivraison, yRelocate,  rectangle, PointLivraisonUI.Type.LIVRAISON,lblpointItiHeureArrivee,lblpointItiArrivee);
                pointLivraisonUI_oblong.print(pointPane,labelPane);
            }

            //button sur chaque point de livraison
            Button btnPopover = new Button();
            btnPopover.relocate(xPoint - radiusAffichageTimeline*2, yRelocateLivraison - radiusAffichageTimeline*2);
            btnPopover.setStyle(popOverButtonStyle);
            itineraire.getTroncons().get(0).getOrigine().printHover(mapPane,primaryStage,btnPopover,
                    "Livraison " +compteurLivraison + " - Heure : " + heurex.format(dtf));

            //button sur chaque point de livraison pour la suppression
            if (modeModifier == true) {
                //TODO: le faire (méthode printSupressButton ?)
                Button btnSupress = new Button();
                btnSupress.relocate(xPoint - radiusAffichageTimeline*2, yRelocateLivraison - radiusAffichageTimeline*2);
                btnSupress.setStyle(popOverButtonStyle);
                itineraire.getTroncons().get(0).getOrigine().printSuppressButton(mapPane,primaryStage,btnSupress);
            }


            //Label heure
            Label lblpointItiHeure = new Label(heureLivraisonx.format(dtf));
            lblpointItiHeure.setLayoutY(yRelocateLivraison - heightLabelTime);

            //Label Livraison machintruc
            Label lblpointItiLivraison = new Label("Livraison " +compteurLivraison);
            lblpointItiLivraison.setLayoutY(yRelocateLivraison - heightLabelTime);

            compteurLivraison++;

            // 3lignes d'accroche
            if(modeModifier == true){
                final String imageURI = new File("images/drag2.jpg").toURI().toString();
                final Image image = makeTransparent(new Image(imageURI, dragAndDropWidth, dragAndDropHeight, false, true));
                final ImageView imageView = new ImageView(image);
                imageView.relocate(centreRightPane - dragAndDropWidth/2 - decalageXIconDragAndDropPoint,yRelocate - image.getHeight()/2 - decalageYIconDragAndDropPoint);
                accrochePointPane.getChildren().add(imageView);

                imageView.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                    }
                });
            }

            //lignes
            double marge = tournee.getMargesLivraison().get(itineraire.getTroncons().get(0).getOrigine()).getSeconds();
            double margeMax = localTimeToSecond(LocalTime.of(0,30)); //Tout vert

            if (marge > margeMax){
                marge = margeMax;
            }

            TronconUI tronconUI;
            //Cas spécial dernier troncon
            if(Point.Type.ENTREPOT == itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getType()){
                tronconUI = new TronconUI(xPoint, yRelocateFromLastPoint,yLastPoint , marge, margeMax);
            }
            else{
                tronconUI = new TronconUI(xPoint, yRelocateFromLastPoint, yRelocate, marge, margeMax);
            }
            tronconUI.print(linePane);

            yRelocateFromLastPoint = yRelocateLivraison;

            //PointUI
            PointLivraisonUI pointLivraisonUI = new PointLivraisonUI(xPoint,yRelocate, PointLivraisonUI.Type.LIVRAISON,lblpointItiHeure, lblpointItiLivraison);
            pointLivraisonUI.print(pointPane, labelPane);

            //Affichage
            //pointPane.getChildren().add(pointIti);
            rightPane.getChildren().add(lblpointItiHeure);
            rightPane.getChildren().add(lblpointItiLivraison);
            pointPane.getChildren().add(btnPopover);
        }

        //--------------------------- Fin for timeline

        //Voiture
        Pane voiturePane = new Pane();
        if(modeModifier == false){
            final String imageURI = new File("images/delivery-icon.jpg").toURI().toString();
            final Image image = makeTransparent(new Image(imageURI, deliveryWidth, deliveryHeight, true, false));
            deliveryHeight = image.getHeight();
            deliveryWidth = image.getWidth();
            ImageView imageView = new ImageView(image);
            imageView.relocate(centreRightPane - deliveryWidth/2,yFirstPoint - deliveryHeight/2);
            System.out.println(imageView.getY());

            voiturePane.getChildren().add(imageView);

            imageView.setOnMousePressed(deliveryOnMousePressedEventHandler);
            imageView.setOnMouseDragged(deliveryOnMouseDraggedEventHandler);
        }


        //bouton modifier
        Button modifierTimeline = new Button();
        if(modeModifier == false)
        {
            modifierTimeline.setText("Modifier");
            modifierTimeline.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    timeLineBuild(rightPane,tournee,mapPane,primaryStage,true);
                }
            });
        }
        else if (modeModifier == true){
            modifierTimeline.setText("Valider");
            modifierTimeline.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    timeLineBuild(rightPane,tournee,mapPane,primaryStage,false);
                }
            });
        }

        //Right vBox
        VBox rightVboxDown = new VBox();
        rightVboxDown.getChildren().add(modifierTimeline);
        rightVboxDown.setAlignment(Pos.BOTTOM_CENTER);
        rightVboxDown.setPadding(new Insets(35));
        rightVboxDown.setPrefSize(bandeauWidth, bandeauHeigth);

        //Affichage
        pointEntrepotDepart.print(pointPane, labelPane);
        pointEntrepotArrivee.print(pointPane, labelPane);

        rightPane.getChildren().add(rightVbox);
        rightPane.getChildren().add(rightVboxDown);
        rightPane.getChildren().add(linePane);
        rightPane.getChildren().add(labelPane);
        rightPane.getChildren().add(accrochePointPane);
        rightPane.getChildren().add(voiturePane);
        rightPane.getChildren().add(pointPane);


        ExportTournee exportTournee = new ExportTournee(tournee);
        exportTournee.exportFile(fileName);
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

    EventHandler<MouseEvent> deliveryOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneY = t.getSceneY();
                    orgTranslateY = ((ImageView)(t.getSource())).getTranslateY();

                }
            };

    EventHandler<MouseEvent> deliveryOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateY = ((ImageView)(t.getSource())).getTranslateY();
                    if (t.getSceneY() >= yFirstPoint - deliveryHeight/2 && t.getSceneY() <= yLastPoint + deliveryHeight/2) {
                        newTranslateY = orgTranslateY + offsetY;
                    }

                    ((ImageView)(t.getSource())).setTranslateY(newTranslateY);
                }
            };


    private double localTimeToMinute(LocalTime time){
        return (time.getHour()*60 + time.getMinute());
    }
    private double localTimeToSecond(LocalTime time){
        return (time.getHour()*60*60 + time.getMinute()*60 + time.getSecond());
    }
}