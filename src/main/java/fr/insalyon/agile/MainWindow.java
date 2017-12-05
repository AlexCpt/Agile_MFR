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
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


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
    final static int radiusAffichageTimeline = 7;
    String fileName;

    // RightPane
    static double  centreRightPane = rightPaneWidth/2;
    double  xPoint =  centreRightPane;
    double yFirstPoint = 50;
    ArrayList<Pair<Point, Double>> yPoints;
    double yLastPoint = rightPaneHeigth - 100;
    static  double widthLabelTime = 75;
    double heightLabelTime = 9; //Todo : L'avoir dynamiquement ? ça a l'air chiant
    double deliveryWidth = 100.0;
    double deliveryHeight = 70.0;

    double orgSceneY;
    double orgTranslateY;
    double orgSceneYLivraison;
    double orgTranslateYLivraison;

    double haut;
    double bas;


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
    Point vehicule;
    ParserXML parser;
    Tournee tournee;
    DemandeDeLivraison ddl;

    Pane mapPane;

    public MainWindow(){
        parser = new ParserXML();
        yPoints = new ArrayList<>();

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
        mapPane = new Pane();
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

                vehicule = new Point("", tournee.getDemandeDeLivraison().getEntrepot().getX(), tournee.getDemandeDeLivraison().getEntrepot().getY());
                vehicule.setVehicule();
                vehicule.print(mapPane);
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

    private double computeY(LocalTime heure, LocalTime heureDebut, LocalTime heureFin) {
        return ((localTimeToSecond(heure) -  localTimeToSecond(heureDebut)) / (localTimeToSecond(heureFin) - localTimeToSecond(heureDebut)))
                * (yLastPoint - yFirstPoint)
                + yFirstPoint;
    }

    public void timeLineBuild(Pane rightPane, Tournee tournee, Pane mapPane, Stage primaryStage, boolean modeModifier){
        yPoints.clear();


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
        final int decalage_x_PopoverAjouterLivraison = -25;
        final int decalage_y_PopoverAjouterLivraison = -7;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");


        String popOverButtonStyle = //"-fx-background-radius: 5em; " +
                "-fx-min-width: " + radiusAffichageTimeline*10 + "px; " +
                "-fx-min-height: " + radiusAffichageTimeline*6 + "px; " +
                "-fx-max-width: " + radiusAffichageTimeline*10 + "px; " +
                "-fx-max-height: " + radiusAffichageTimeline*6 + "px; " +
                "-fx-background-color: transparent;" +
                "-fx-background-insets: 0px; " +
                "-fx-padding: 0px;";

        //region <Titre>
        //Titre
        Label lblTimeline = new Label("Timeline");
        lblTimeline.setPadding(new Insets(10));
        //Right vBox
        VBox rightVbox = new VBox();
        rightVbox.getChildren().add(lblTimeline);
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPrefSize(bandeauWidth, bandeauHeigth);
//endregion

        //region <Entrepot Départ>

        //Point de départ et label -------------------------------------
        Label lblEntrepotDepartHeure = new Label(heureDebutTournee.toString());
        lblEntrepotDepartHeure.setLayoutY(yFirstPoint- heightLabelTime);

        Label lblEntrepotDepart = new Label("Entrepôt");
        lblEntrepotDepart.setLayoutY(yFirstPoint- heightLabelTime);

        PointLivraisonUI pointEntrepotDepart = new PointLivraisonUI(xPoint,
                yFirstPoint, PointLivraisonUI.Type.ENTREPOT_DEPART,lblEntrepotDepartHeure, lblEntrepotDepart);

        tournee.getDemandeDeLivraison().getEntrepot().printHover(mapPane,primaryStage,pointEntrepotDepart.getButton(),
                "Entrepot - Depart : "+ heureDebutTournee.toString() );
//endregion

        //region <Point d'arrivée>

        Label lblEntrepotArriveeHeure = new Label(heureFinTournee.toString());
        lblEntrepotArriveeHeure.setLayoutY(yLastPoint- heightLabelTime);

        Label lblEntrepotArrivee = new Label("Entrepôt");
        lblEntrepotArrivee.setLayoutY(yLastPoint- heightLabelTime);

        PointLivraisonUI pointEntrepotArrivee = new PointLivraisonUI(xPoint,
                yLastPoint, PointLivraisonUI.Type.ENTREPOT_DEPART,lblEntrepotArriveeHeure, lblEntrepotArrivee);

        tournee.getDemandeDeLivraison().getEntrepot().printHover(mapPane,primaryStage,pointEntrepotArrivee.getButton(),
                "Entrepot - Arrivee : "+ heureFinTournee.toString() );
//endregion

        //region <Livraisons intermédiaires>

        int compteurLivraison = 1;
        double yRelocateFromLastPoint = yFirstPoint;
        Pane pointPane = new Pane();
        Pane linePane = new Pane();
        Pane accrochePointPane = new Pane();
        Pane labelPane = new Pane();
        Pane buttonPane = new Pane();
        Pane mobilePane = new Pane();
        ImageviewExtended imageViewArrowActu = null;
        ImageviewExtended imageViewArrowPrecedent = null;


        for (Itineraire itineraire: tournee.getItineraires()) {

            if(itineraire.getTroncons().get(0).getOrigine().getType() == Point.Type.ENTREPOT){
                LocalTime heureDepart = tournee.getDemandeDeLivraison().getDepart();

                //region <plan>
                for (int i = 1; i < itineraire.getTroncons().size(); i++) {
                    heureDepart = heureDepart.plusSeconds((long)(((itineraire.getTroncons().get(i-1).getLongueur() * 3600) / 1000) / 15));
                    double yPosition = computeY(heureDepart, heureDebutTournee, heureFinTournee);

                    yPoints.add(new Pair<>(itineraire.getTroncons().get(i).getOrigine(), yPosition));
                }
                //endregion

                continue;
            }

            //Points
            LocalTime heurex = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateArrivee();
            LocalTime heureLivraisonx = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison();
            LocalTime heureDepart = heureLivraisonx.plus(itineraire.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison());
            LocalTime iterateurHeure = heureDepart;

            double yRelocateLivraison = computeY(heureLivraisonx, heureDebutTournee, heureFinTournee);
            double yRelocate = computeY(heurex, heureDebutTournee, heureFinTournee);
            double yRelocateDepart = computeY(heureDepart, heureDebutTournee, heureFinTournee);

            yPoints.add(new Pair<>(itineraire.getTroncons().get(0).getOrigine(), yRelocate));


            if (heurex != heureLivraisonx) {
                yPoints.add(new Pair<>(itineraire.getTroncons().get(0).getOrigine(), yRelocateLivraison));
            }
            yPoints.add(new Pair<>(itineraire.getTroncons().get(0).getOrigine(), yRelocateDepart));

            for (int i = 1; i < itineraire.getTroncons().size(); i++) {
                iterateurHeure = iterateurHeure.plusSeconds((long)(((itineraire.getTroncons().get(i-1).getLongueur() * 3600) / 1000) / 15));
                double yPosition = computeY(iterateurHeure, heureDebutTournee, heureFinTournee);

                yPoints.add(new Pair<>(itineraire.getTroncons().get(i).getOrigine(), yPosition));
            }

            //button sur chaque point de livraison
            Button btnPopover = new Button();
            btnPopover.relocate(xPoint - radiusAffichageTimeline*2, yRelocateLivraison - radiusAffichageTimeline*2);
            btnPopover.setStyle(popOverButtonStyle);

            //region <Point Oblong>
                //Label arrivée
            Label lblpointItiHeureDebutLivraison = new Label(heureLivraisonx.format(dtf));
            lblpointItiHeureDebutLivraison.setLayoutY(yRelocateLivraison - heightLabelTime);

            Label lblpointItiHeureFinLivraison = new Label(heureDepart.format(dtf));
            lblpointItiHeureFinLivraison.setLayoutY(yRelocateLivraison - heightLabelTime);

            //Label Livraison machintruc
            Label lblpointItiLivraison = new Label("Livraison " +compteurLivraison);
            lblpointItiLivraison.setLayoutY(yRelocateLivraison - heightLabelTime);

            //region <lignes - tronçons>

            double marge = tournee.getMargesLivraison().get(itineraire.getTroncons().get(0).getOrigine()).getSeconds();
            double margeMax = localTimeToSecond(LocalTime.of(0,30)); //Tout vert

            if (marge > margeMax){
                marge = margeMax;
            }

            TronconUI tronconUI;
            TronconUI lastTronconUI=null;
            //Cas spécial dernier troncon
            if(Point.Type.ENTREPOT == itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getType()){
                tronconUI = new TronconUI(xPoint, yRelocateFromLastPoint ,yRelocateLivraison , marge, margeMax);
                lastTronconUI = new TronconUI(xPoint, yRelocateDepart, yLastPoint , marge, margeMax);
                lastTronconUI.print(linePane);
            }
            else{
                tronconUI = new TronconUI(xPoint, yRelocateFromLastPoint, yRelocate, marge, margeMax);
            }
            tronconUI.print(linePane);
            //endregion

            PointLivraisonUI_Oblong pointLivraisonUI_oblong = new PointLivraisonUI_Oblong(xPoint, yRelocateDepart, yRelocateLivraison, PointLivraisonUI.Type.LIVRAISON,lblpointItiHeureDebutLivraison,lblpointItiHeureFinLivraison,lblpointItiLivraison);
            pointLivraisonUI_oblong.print(pointPane,labelPane,buttonPane);
            //endregion

            //button sur chaque point de livraison pour la suppression
            if (modeModifier == true) {
                //TODO: le faire (méthode printSupressButton ?)
                Button btnSupress = new Button();
                btnSupress.relocate(xPoint - radiusAffichageTimeline*2, yRelocateDepart - radiusAffichageTimeline*2);
                btnSupress.setStyle(popOverButtonStyle);
                itineraire.getTroncons().get(0).getOrigine().printSuppressButton(mapPane,primaryStage,btnSupress);
            }
            compteurLivraison++;


            // FlecheDéplacement de Livraison
            /*if(modeModifier == true){
                final String imageURI = new File("images/drag2.jpg").toURI().toString();
                final Image image = makeTransparent(new Image(imageURI, dragAndDropWidth, dragAndDropHeight, false, true));
                imageViewArrowActu = new ImageviewExtended(pointLivraisonUI_oblong, image);
                imageViewArrowActu.relocate(centreRightPane - dragAndDropWidth/2 - decalageXIconDragAndDropPoint, yRelocateLivraison + ((yRelocateDepart-yRelocateLivraison)/2)  - image.getHeight()/2 - decalageYIconDragAndDropPoint);
                mobilePane.getChildren().add(imageViewArrowActu);

                imageViewArrowActu.setOnMousePressed(deplacementLivraisonOnMousePressedEventHandler);
                imageViewArrowActu.setOnMouseDragged(deplacementLivraisonOnMouseDraggedEventHandler);
            }


                if(Point.Type.ENTREPOT == itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getType()){
                  //  imageViewArrowActu.setTronconUISuivant(lastTronconUI);
                }
                if (compteurLivraison != 2) {
                   // imageViewArrowPrecedent.setTronconUISuivant(tronconUI);
                }

               /* imageViewArrowActu.setTronconUIPrecedent(tronconUI);
                imageViewArrowActu.setOnMousePressed(deplacementLivraisonOnMousePressedEventHandler);
                imageViewArrowActu.setOnMouseDragged(deplacementLivraisonOnMouseDraggedEventHandler);
                imageViewArrowPrecedent = imageViewArrowActu;*/


            yRelocateFromLastPoint = yRelocateDepart;

            //hover sur chaque livraison
            itineraire.getTroncons().get(0).getOrigine().printHover(mapPane,primaryStage,pointLivraisonUI_oblong.getButton(),
                    lblpointItiLivraison.getText() + " - " + itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateArrivee());
        }

        //endregion


        //region <Voiture>
        if(modeModifier == false){
            final String imageURI = new File("images/delivery-icon-fleche.png").toURI().toString();
            final Image image = new Image(imageURI, deliveryWidth, deliveryHeight, true, false);
            deliveryHeight = image.getHeight();
            deliveryWidth = image.getWidth();
            ImageView imageView = new ImageView(image);

            imageView.relocate(0, yFirstPoint - deliveryHeight/2);

            mobilePane.getChildren().add(imageView);

            imageView.setOnMousePressed(deliveryOnMousePressedEventHandler);
            imageView.setOnMouseDragged(deliveryOnMouseDraggedEventHandler);
        }
        //endregion

        //region <bouton modifier>
        Button modifierTimeline = new Button();
        Button buttonAjout = null;

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
            modifierTimeline.setMinWidth(63);
            buttonAjout = new Button("Ajouter");
            buttonAjout.setMinWidth(63);

            buttonAjout.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    HBox hBoxAjoutInPopover = new HBox();
                    Label lblAjoutInPopover = new Label("Position de la Livraison : ");
                    TextField txtFieldInPopover = new TextField();
                    txtFieldInPopover.setPrefWidth(60);
                    hBoxAjoutInPopover.getChildren().add(lblAjoutInPopover);
                    hBoxAjoutInPopover.getChildren().add(txtFieldInPopover);
                    hBoxAjoutInPopover.setAlignment(Pos.CENTER);
                    hBoxAjoutInPopover.setPadding(new Insets(20, 20,10,20));

                    VBox vBoxAjoutInPopover = new VBox();
                    vBoxAjoutInPopover.getChildren().add(hBoxAjoutInPopover);
                    Button buttonAjoutInPopover = new Button("Valider");
                    buttonAjoutInPopover.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            //mettre truc de louise
                        }});

                    vBoxAjoutInPopover.getChildren().add(buttonAjoutInPopover);
                    vBoxAjoutInPopover.setAlignment(Pos.CENTER);

                    Pane panePopOver = new Pane();
                    panePopOver.getChildren().add(vBoxAjoutInPopover);
                    panePopOver.setPadding(new Insets(5));

                    PopOver popOver = new PopOver();
                    popOver.setAutoHide(true);
                    popOver.setContentNode(panePopOver);
                    popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);


                    popOver.show(modifierTimeline);
                    popOver.setX(popOver.getX() + decalage_x_PopoverAjouterLivraison);
                    popOver.setY(popOver.getY() + decalage_y_PopoverAjouterLivraison);
                }
            });
            modifierTimeline.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    timeLineBuild(rightPane,tournee,mapPane,primaryStage,false);
                }
            });
        }

        //Hbox of Ajouter-Valider
        HBox hBoxAjouterValider = new HBox();
        if(buttonAjout != null) { hBoxAjouterValider.getChildren().add(buttonAjout); }
        hBoxAjouterValider.getChildren().add(modifierTimeline);
        hBoxAjouterValider.setAlignment(Pos.BOTTOM_CENTER);
        hBoxAjouterValider.setSpacing(8);
        hBoxAjouterValider.setPrefSize(bandeauWidth, bandeauHeigth);


        //Right vBox
        VBox rightVboxDown = new VBox();
        rightVboxDown.getChildren().add(hBoxAjouterValider);
        rightVboxDown.setAlignment(Pos.BOTTOM_CENTER);
        rightVboxDown.setPadding(new Insets(35));
        rightVboxDown.setPrefSize(bandeauWidth, bandeauHeigth);

        //endregion

        //region <Affichage>
        pointEntrepotDepart.print(pointPane, labelPane, buttonPane);
        pointEntrepotArrivee.print(pointPane, labelPane, buttonPane);

        rightPane.getChildren().add(rightVbox);
        rightPane.getChildren().add(rightVboxDown);
        rightPane.getChildren().add(linePane);
        rightPane.getChildren().add(labelPane);
        rightPane.getChildren().add(accrochePointPane);
        rightPane.getChildren().add(pointPane);
        rightPane.getChildren().add(buttonPane);
        rightPane.getChildren().add(mobilePane);
        //endregion

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
                    double newTranslateY;
                    if (t.getSceneY() < yFirstPoint) {
                        newTranslateY = 0;
                    } else if (t.getSceneY() > yLastPoint) {
                        newTranslateY = yLastPoint - yFirstPoint;
                    } else {
                        newTranslateY = orgTranslateY + offsetY;
                    }

                    ((ImageView)(t.getSource())).setTranslateY(newTranslateY);

                    double yPos = newTranslateY + orgSceneY - orgTranslateY;

                    for (Itineraire it : tournee.getItineraires()) {
                        for (Troncon tr : it.getTroncons()) {
                            tr.setLongueurParcourue(mapPane,0);
                        }
                    }

                    Troncon troncon = null;

                    for (Troncon tr : plan.getGraph().get(tournee.getDemandeDeLivraison().getEntrepot())) {
                        if (tr.getDestination() == yPoints.get(0).getKey()) {
                            troncon = tr;
                        }
                    }

                    if (yPos <= yPoints.get(0).getValue()) {
                        double progress = (yPos - yFirstPoint) / (yPoints.get(0).getValue() - yFirstPoint);
                        troncon.setLongueurParcourue(mapPane,troncon.getLongueur() * progress);
                        double newX = tournee.getDemandeDeLivraison().getEntrepot().getX() + progress * (yPoints.get(0).getKey().getX() - tournee.getDemandeDeLivraison().getEntrepot().getX());
                        double newY = tournee.getDemandeDeLivraison().getEntrepot().getY() + progress * (yPoints.get(0).getKey().getY() - tournee.getDemandeDeLivraison().getEntrepot().getY());

                        vehicule.setX((int)newX);
                        vehicule.setY((int)newY);

                        vehicule.move(mapPane);
                        return;
                    } else if (troncon != null) {
                        troncon.setLongueurParcourue(mapPane, troncon.getLongueur());
                    }

                    for (int i = 1; i < yPoints.size(); i++) {
                        troncon = null;
                        for (Troncon tr : plan.getGraph().get(yPoints.get(i-1).getKey())) {
                            if (tr.getDestination() == yPoints.get(i).getKey()) {
                                troncon = tr;
                            }
                        }

                        if (yPos <= yPoints.get(i).getValue()) {
                            double progress = (yPos - yPoints.get(i-1).getValue()) / (yPoints.get(i).getValue() - yPoints.get(i-1).getValue());
                            troncon.setLongueurParcourue(mapPane, troncon.getLongueur() * progress);
                            double newX = yPoints.get(i-1).getKey().getX() + progress * (yPoints.get(i).getKey().getX() - yPoints.get(i-1).getKey().getX());
                            double newY = yPoints.get(i-1).getKey().getY() + progress * (yPoints.get(i).getKey().getY() - yPoints.get(i-1).getKey().getY());

                            vehicule.setX((int)newX);
                            vehicule.setY((int)newY);

                            vehicule.move(mapPane);
                            return;
                        } else if (troncon != null) {
                            troncon.setLongueurParcourue(mapPane, troncon.getLongueur());
                        }
                    }

                    troncon = null;
                    for (Troncon tr : plan.getGraph().get(yPoints.get(yPoints.size()-1).getKey())) {
                        if (tr.getDestination() == tournee.getDemandeDeLivraison().getEntrepot()) {
                            troncon = tr;
                        }
                    }

                    if (yPos >= yPoints.get(yPoints.size()-1).getValue()) {
                        double progress = (yPos - yPoints.get(yPoints.size()-1).getValue()) / (yLastPoint - yPoints.get(yPoints.size()-1).getValue());
                        troncon.setLongueurParcourue(mapPane, troncon.getLongueur() * progress);
                        double newX = yPoints.get(yPoints.size()-1).getKey().getX() + progress * (tournee.getDemandeDeLivraison().getEntrepot().getX() - yPoints.get(yPoints.size()-1).getKey().getX());
                        double newY = yPoints.get(yPoints.size()-1).getKey().getY() + progress * (tournee.getDemandeDeLivraison().getEntrepot().getY() - yPoints.get(yPoints.size()-1).getKey().getY());

                        vehicule.setX((int)newX);
                        vehicule.setY((int)newY);

                        vehicule.move(mapPane);
                        return;
                    } else if (troncon != null) {
                        troncon.setLongueurParcourue(mapPane, troncon.getLongueur());
                    }
                }
            };

    EventHandler<MouseEvent> deplacementLivraisonOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneY = t.getSceneY();
                    orgTranslateY = ((ImageView)(t.getSource())).getTranslateY();

                    ImageviewExtended imageView = ((ImageviewExtended)(t.getSource()));
                    haut = imageView.getTronconUIPrecedent().getLine().getStartY();
                    bas = imageView.getTronconUISuivant().getLine().getEndY();

                    System.out.println("haut " + haut +" bas " + bas);
                }
            };

    EventHandler<MouseEvent> deplacementLivraisonOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetY = t.getSceneY() - orgSceneYLivraison;
                    double newTranslateY = ((ImageView)(t.getSource())).getTranslateY();
                    if (t.getSceneY() >= yFirstPoint - deliveryHeight/2 && t.getSceneY() <= yLastPoint + deliveryHeight/2) {
                        newTranslateY = orgTranslateYLivraison + offsetY;
                    }

                    ((ImageView)(t.getSource())).setTranslateY(newTranslateY);
                    ImageviewExtended imageView = ((ImageviewExtended)(t.getSource()));
                    imageView.getPointLivraisonUI_oblong().setTranslateY(newTranslateY);

                    //imageView.getTronconUIPrecedent().getLine().setTranslateY(newTranslateY);
                    //imageView.getTronconUIPrecedent().getLine().setStartY(haut);

                    //imageView.getTronconUISuivant().getLine().setTranslateY(newTranslateY);
                    //imageView.getTronconUISuivant().getLine().setEndY(bas);


                    //imageView.getTronconUIPrecedent().getLine().setEndY(imageView.getTronconUIPrecedent().getLine().getEndY() + newTranslateY);
                    //imageView.getTronconUISuivant().getLine().setStartY(imageView.getTronconUISuivant().getLine().getStartY() + newTranslateY);

                    imageView.getTronconUIPrecedent().getLine().setEndY(newTranslateY);
                }
            };


    private double localTimeToSecond(LocalTime time){
        return (time.getHour()*60*60 + time.getMinute()*60 + time.getSecond());
    }

    public void pointerTronconPourValidationAjout(int numeroTroncon){

    }
}