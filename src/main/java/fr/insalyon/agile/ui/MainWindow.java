package fr.insalyon.agile.ui;

import fr.insalyon.agile.designpattern.command.CdeAjout;
import fr.insalyon.agile.designpattern.command.CdeSupprime;
import fr.insalyon.agile.designpattern.command.ListeDeCdes;
import fr.insalyon.agile.export.ExportTournee;
import fr.insalyon.agile.modele.*;
import fr.insalyon.agile.parser.ParserXML;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.controlsfx.control.PopOver;

import java.time.Duration;
import java.util.*;

import java.io.File;
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
    final static int radiusAffichageTimeline = 7;
    String fileName;

    // RightPane
    static double  centreRightPane = rightPaneWidth/2;
    double  xPoint =  centreRightPane;
    double yFirstPoint = 50;
    ArrayList<Pair<Point, Double>> yPoints;
    double yLastPoint = rightPaneHeigth - 100;
    static  double widthLabelTime = 75;
    double heightLabelTime = 9;
    double deliveryWidth = 100.0;
    double deliveryHeight = 70.0;

    double orgSceneY;
    double orgTranslateY;

    final Button buttonPlan = new Button("Charger Plan...");
    final Button buttonDDL = new Button("Charger Demande de Livraison...");
    final FileChooser fileChooser = new FileChooser();

    Plan plan;
    Point vehicule;
    ParserXML parser;
    Tournee tournee;
    DemandeDeLivraison ddl;
    ListeDeCdes listeDeCdes;
    Map<Itineraire, TronconUI> timeLineItineraires;
    Pane mapPane;

    public MainWindow(){
        parser = new ParserXML();
        yPoints = new ArrayList<>();
        listeDeCdes = new ListeDeCdes();
        timeLineItineraires = new HashMap<>();
        plan = parser.parsePlan("fichiersXML/planLyonPetit.xml");
    }

    /**
     * Fonction main lançant la fenêtre principale
     * @param args pas utilisé
     */
    public static void main(String[] args) {
        new MainWindow();

        launch(args);
    }

    /**
     * Controlleur principal qui crée et gère l'ensemble de l'interface.
     * @param primaryStage primary stage javaFX de notre fenêtre
     */
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

        Pane rightPane = new Pane();


        // LeftVBox
        VBox leftVbox = new VBox();
        VBox globalLeftBox = new VBox();
        final String imageURI = new File("images/logo.png").toURI().toString();
        final Image logo = makeTransparent(new Image(imageURI, 250, 50, true, true));
        ImageView logoView = new ImageView(logo);
        logoView.setLayoutX(bandeauWidth/2 - 85);
        logoView.setLayoutY(30);

        Label fileLabelPlan = new Label("Aucun fichier chargé.");
        fileLabelPlan.setWrapText(true);
        leftVbox.getChildren().add(fileLabelPlan);

        //Partie Plan du bandeau
        buttonPlan.setOnAction(event -> {
            fileChooser.setInitialDirectory(new File("fichiersXML/"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                fileLabelPlan.setText(file.getName());
                mapPane.getChildren().clear();
                rightPane.getChildren().clear();
                buildTimelineTitle(rightPane);

                plan = parser.parsePlan(file.getAbsolutePath());
                plan.print(mapPane);
            }
        });
        leftVbox.getChildren().add(buttonPlan);

        Label fileLabelDDL = new Label("Aucun fichier chargé.");
        fileLabelDDL.setWrapText(true);
        leftVbox.getChildren().add(fileLabelDDL);

        //Partie Demande Livraison du bandeau
        buttonDDL.setWrapText(true);
        buttonDDL.setTextAlignment(TextAlignment.CENTER);
        buttonDDL.setMaxWidth(120);
        buttonDDL.setOnAction(event -> {

            if(tournee != null){
                for (Itineraire it : tournee.getItineraires()) {
                    for (Troncon tr : it.getTroncons()) {
                        tr.setLongueurParcourue(mapPane,0);
                    }
                }
            }


            fileChooser.setInitialDirectory(new File("fichiersXML/"));
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                fileName = file.getAbsolutePath();
                fileLabelDDL.setText(file.getName());
                plan.resetTypePoints();
                tournee = null;
                ddl = parser.parseDemandeDeLivraison(fileName);

                if (ddl == null) {
                    return;
                }

                rightPane.getChildren().clear();
                buildTimelineTitle(rightPane);

                mapPane.getChildren().clear();
                plan.print(mapPane);
            }
        });

        buildTimelineTitle(rightPane);

        Button btnCalculerTournee = new Button();
        btnCalculerTournee.setText("Calculer tournée");
        leftVbox.setSpacing(20);
        btnCalculerTournee.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (ddl != null) {
                    tournee = ddl.calculerTournee();
                    mapPane.getChildren().clear();
                    timeLineBuild(rightPane, tournee,mapPane,primaryStage, false);
                    plan.print(mapPane);
                    tournee.print(mapPane);

                    vehicule = new Point("", tournee.getDemandeDeLivraison().getEntrepot().getX(), tournee.getDemandeDeLivraison().getEntrepot().getY());
                    vehicule.setVehicule();
                    vehicule.print(mapPane);
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Erreur");
                    alert.setContentText("Aucune demande de livraison chargée.");

                    alert.showAndWait();
                }
            }
        });

        Button btnUndo = new Button();
        btnUndo.setText("Annuler");
        btnUndo.setMinWidth(80);
        leftVbox.setSpacing(20);
        btnUndo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                listeDeCdes.undo();
                //Recalcul tournée
                mapPane.getChildren().clear();
                timeLineBuild(rightPane, tournee,mapPane,primaryStage, false);
                plan.print(mapPane);
                tournee.print(mapPane);

                vehicule = new Point("", tournee.getDemandeDeLivraison().getEntrepot().getX(), tournee.getDemandeDeLivraison().getEntrepot().getY());
                vehicule.setVehicule();
                vehicule.print(mapPane);
            }
        });

        Button btnRedo = new Button();
        btnRedo.setText("Rétablir");
        btnRedo.setMinWidth(80);
        leftVbox.setSpacing(20);
        btnRedo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                listeDeCdes.redo();
                //Recalcul tournée
                mapPane.getChildren().clear();
                timeLineBuild(rightPane, tournee,mapPane,primaryStage, false);
                plan.print(mapPane);
                tournee.print(mapPane);

                vehicule = new Point("", tournee.getDemandeDeLivraison().getEntrepot().getX(), tournee.getDemandeDeLivraison().getEntrepot().getY());
                vehicule.setVehicule();
                vehicule.print(mapPane);
            }
        });

        //Hbox of Ajouter-Valider
        HBox hBoxUndoRedo = new HBox();
        hBoxUndoRedo.getChildren().add(btnUndo);
        hBoxUndoRedo.getChildren().add(btnRedo);
        hBoxUndoRedo.setAlignment(Pos.BOTTOM_CENTER);
        hBoxUndoRedo.setSpacing(8);
        hBoxUndoRedo.setPrefSize(bandeauWidth, bandeauHeigth);

        VBox leftVboxDown = new VBox();
        leftVboxDown.getChildren().add(hBoxUndoRedo);
        leftVboxDown.setAlignment(Pos.BOTTOM_CENTER);
        leftVboxDown.setPadding(new Insets(35));
        leftVboxDown.setPrefSize(bandeauWidth, 50);


        Button btnExportTournee = new Button();
        btnExportTournee.setText("Exporter tournée");
        leftVbox.setSpacing(20);
        btnExportTournee.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                ExportTournee exportTournee = new ExportTournee(tournee);
                try {
                    fileChooser.setInitialDirectory(new File("exportTournee/"));
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if (file != null) {
                        exportTournee.exportFile(file.getAbsolutePath());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Export de la tournée");
                        alert.setContentText("Le fichier a bien été exporté.");

                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Erreur");
                    alert.setContentText("Erreur lors de l'export de la tournée : " + e.getMessage());

                    alert.showAndWait();
                }

            }
        });


        // --------------------------------
        //VBOX

        leftVbox.getChildren().add(buttonDDL);
        leftVbox.getChildren().add(btnCalculerTournee);
        leftVbox.getChildren().add(btnExportTournee);
        leftVbox.setPrefSize(bandeauWidth, bandeauHeigth);
        leftVbox.setAlignment(Pos.CENTER);

        globalLeftBox.getChildren().add(leftVbox);
        globalLeftBox.getChildren().add(leftVboxDown);
        globalLeftBox.setPrefSize(bandeauWidth, bandeauHeigth);
        //Left Pane
        Pane leftPane = new Pane();
        leftPane.getChildren().add(logoView);

        leftPane.getChildren().add(globalLeftBox);

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

    /**
     * Construit la timeline pour la tournée qui vient d'être calculée
     *
     * @param rightPane : Le pane où l'on veut placer la timeline
     * @param tournee : La tournée à afficher sur la timeline
     * @param mapPane : Le pane du plan sur lequel est affichée la tournée actuelle
     * @param primaryStage : Le primary stage pour cette application
     * @param modeModifier : Spécifie si l'on peut modifier ou non la tournée
     */
    public void timeLineBuild(Pane rightPane, Tournee tournee, Pane mapPane, Stage primaryStage, boolean modeModifier){
        yPoints.clear();

        rightPane.getChildren().clear();

        LocalTime heureDebutTournee = LocalTime.of(8,0);

        LocalTime heureFinTournee =
                tournee.getItineraires().get(tournee.getItineraires().size()-1).getTroncons().get(0).getOrigine().getLivraison().getDateLivraison()
                        .plus(tournee.getItineraires().get(tournee.getItineraires().size()-1).getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison())
                        .plus(tournee.getItineraires().get(tournee.getItineraires().size()-1).getDuree());

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

        Label lblEntrepotArriveeHeure = new Label(heureFinTournee.format(dtf));
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

        for (Itineraire itineraire: tournee.getItineraires()) {
            TronconUI tronconUI;
            TronconUI lastTronconUI;

            if(itineraire.getTroncons().get(0).getOrigine().getType() == Point.Type.ENTREPOT){
                LocalTime heureDepart = tournee.getDemandeDeLivraison().getDepart();

                //region <plan>
                for (int i = 1; i < itineraire.getTroncons().size(); i++) {
                    heureDepart = heureDepart.plusSeconds((long)(((itineraire.getTroncons().get(i-1).getLongueur() * 3600) / 1000) / 15));
                    double yPosition = computeY(heureDepart, heureDebutTournee, heureFinTournee);

                    yPoints.add(new Pair<>(itineraire.getTroncons().get(i).getOrigine(), yPosition));
                }
                //endregion

                double marge = tournee.getMargesLivraison().get(itineraire.getTroncons().get(0).getOrigine()).getSeconds();
                double margeMax = localTimeToSecond(LocalTime.of(0,30)); //Tout vert

                if (marge > margeMax){
                    marge = margeMax;
                }

                double yFinTronconUI = computeY(itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getLivraison().getDateLivraison(), heureDebutTournee, heureFinTournee);
                tronconUI = new TronconUI(xPoint, yFirstPoint, yFinTronconUI, marge, margeMax);
                tronconUI.print(linePane);
                timeLineItineraires.put(itineraire, tronconUI);

                continue;
            }

            //Points
            LocalTime heurex = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateArrivee();
            LocalTime heureLivraisonx = itineraire.getTroncons().get(0).getOrigine().getLivraison().getDateLivraison();
            LocalTime heureDepart = heureLivraisonx.plus(itineraire.getTroncons().get(0).getOrigine().getLivraison().getDureeLivraison());
            LocalTime iterateurHeure = heureDepart;

            double yRelocateArrivee = computeY(heurex, heureDebutTournee, heureFinTournee);
            double yRelocateLivraison = computeY(heureLivraisonx, heureDebutTournee, heureFinTournee);
            double yRelocateDepart = computeY(heureDepart, heureDebutTournee, heureFinTournee);

            yPoints.add(new Pair<>(itineraire.getTroncons().get(0).getOrigine(), yRelocateArrivee));


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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm");


            Label lblpointItiHeureFinLivraison = new Label(heureDepart.format(dtf));

            if (heureDepart.isBefore(heureLivraisonx.plus(Duration.ofMinutes(2)))) {
                lblpointItiHeureFinLivraison.setLayoutY(yRelocateLivraison - heightLabelTime);
                lblpointItiHeureFinLivraison.setVisible(false);
            }


            //Label Livraison machintruc
            Label lblpointItiLivraison = new Label("Livraison " + compteurLivraison);
            lblpointItiLivraison.setLayoutY(yRelocateLivraison + (yRelocateDepart-yRelocateLivraison)/2 - heightLabelTime);

            //region <lignes - tronçons>

            double marge = tournee.getMargesLivraison().get(itineraire.getTroncons().get(0).getOrigine()).getSeconds();
            double margeMax = localTimeToSecond(LocalTime.of(0,30)); //Tout vert

            if (marge > margeMax){
                marge = margeMax;
            }

            //Cas spécial dernier troncon
            if(Point.Type.ENTREPOT == itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getType()){
                tronconUI = new TronconUI(xPoint, yRelocateFromLastPoint ,yRelocateLivraison , marge, margeMax);
                lastTronconUI = new TronconUI(xPoint, yRelocateDepart, yLastPoint , marge, margeMax);
                lastTronconUI.print(linePane);
            }
            else{
                tronconUI = new TronconUI(xPoint, yRelocateFromLastPoint, yRelocateLivraison, marge, margeMax);
            }
            tronconUI.print(linePane);
            timeLineItineraires.put(itineraire, tronconUI);
            //endregion

            PointLivraisonUI_Oblong pointLivraisonUI_oblong = new PointLivraisonUI_Oblong(xPoint, yRelocateDepart, yRelocateLivraison, PointLivraisonUI.Type.LIVRAISON,lblpointItiHeureDebutLivraison,lblpointItiHeureFinLivraison,lblpointItiLivraison);
            pointLivraisonUI_oblong.print(pointPane,labelPane,buttonPane);
            //endregion

            //button SUPPRIMER sur chaque point de livraison pour la suppression
            if (modeModifier == true) {
                //boutton supprimer

                Button boutonSuppr = new Button("Supprimer");
                // boutonSuppr.setFocusTraversable(false);
                boutonSuppr.setMaxHeight(60);
                boutonSuppr.setStyle("-fx-background-color: transparent;"
                );

                itineraire.getTroncons().get(0).getOrigine().printSuppressButton(mapPane,
                        primaryStage, pointLivraisonUI_oblong.getButton(), boutonSuppr);
                PopOver supprPopover = itineraire.getTroncons().get(0).getOrigine().getSupprPopover();
                boutonSuppr.setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                listeDeCdes.ajoute(new CdeSupprime(tournee, itineraire.getTroncons().get(0).getOrigine(), mapPane));
                                timeLineBuild(rightPane, tournee, mapPane, primaryStage, false);
                                mapPane.getChildren().clear();
                                plan.print(mapPane);
                                tournee.print(mapPane);
                                supprPopover.hide();
                            }
                        });
            }
            compteurLivraison++;

            yRelocateFromLastPoint = yRelocateDepart;

            Point origineLivraison = itineraire.getTroncons().get(0).getOrigine();
            long secondsMarge = tournee.getMargesLivraison().get(origineLivraison).getSeconds();

            //hover sur chaque livraison
            origineLivraison.printGlowHover(mapPane,primaryStage,pointLivraisonUI_oblong.getButton(),
                    String.format(
                            lblpointItiLivraison.getText() +
                                    " - "+
                                    itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getDestination().getId()+
                                    " "+
                                    itineraire.getTroncons().get(itineraire.getTroncons().size() - 1).getNomRue()+
                                    "\nHeure d'Arrivée : "+
                                    origineLivraison.getLivraison().getDateArrivee().format(dtf) +
                                    "\nHeure de début : "+
                                    origineLivraison.getLivraison().getDateLivraison().format(dtf)+
                                    "\nDurée livraison : " +
                                    origineLivraison.getLivraison().getDureeLivraison().toMinutes() +
                                    " min\n" +
                                    "Marge : " +
                                    String.format("%d h %02d min", secondsMarge/ 3600, (secondsMarge % 3600) / 60, (secondsMarge % 60))),
                    pointLivraisonUI_oblong.getRectangle());
        }

        //endregion


        //region <Voiture>
        if(modeModifier == false){
            final String imageURI = new File("images/delivery-icon-fleche.png").toURI().toString();
            final Image image = new Image(imageURI, deliveryWidth, deliveryHeight, true, false);
            deliveryHeight = image.getHeight();
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
                    Pane panePopOver = new Pane();
                    PopOver popOver = new PopOver();

                    HBox hBoxAjoutInPopover = new HBox();
                    Label lblAjoutInPopover = new Label("Position de la Livraison : ");
                    TextField txtFieldInPopover = new TextField();
                    txtFieldInPopover.setPrefWidth(60);

                    hBoxAjoutInPopover.getChildren().add(lblAjoutInPopover);
                    hBoxAjoutInPopover.getChildren().add(txtFieldInPopover);
                    hBoxAjoutInPopover.setAlignment(Pos.CENTER);
                    hBoxAjoutInPopover.setPadding(new Insets(20, 20,5,20));

                    HBox hBoxDuree = new HBox();
                    Label lblDureeInPopover = new Label("Durée de la Livraison : ");
                    TextField txtFieldDuree = new TextField();
                    txtFieldDuree.setPrefWidth(60);

                    hBoxDuree.getChildren().add(lblDureeInPopover);
                    hBoxDuree.getChildren().add(txtFieldDuree);
                    hBoxDuree.setAlignment(Pos.CENTER);
                    hBoxDuree.setPadding(new Insets(5, 20,10,20));

                    VBox vBoxAjoutInPopover = new VBox();
                    vBoxAjoutInPopover.getChildren().add(hBoxAjoutInPopover);
                    vBoxAjoutInPopover.getChildren().add(hBoxDuree);
                    Button buttonAjoutInPopover = new Button("Valider");

                    for (Point p : plan.getPoints()) {
                        p.setButtonEventHandler(event1 -> {
                            if (modeModifier) {
                                txtFieldInPopover.setText(p.getId());
                            }
                        });
                    }

                    buttonAjoutInPopover.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            Point pointSelectionne = null;
                            Itineraire itineraireSelectionne = null;

                            for (Point point : plan.getPoints()) {
                                if(point.getId().equals(txtFieldInPopover.getText()))
                                {
                                    pointSelectionne = point;
                                    break;
                                }
                            }
                            if(pointSelectionne == null)
                                return;

                            long duree;
                            try {
                                duree = Long.parseLong(txtFieldDuree.getText());
                            } catch (NumberFormatException e) {
                                duree = 0;
                            }

                            Iterator<Itineraire> iterator = tournee.getItineraires().iterator();
                            while (iterator.hasNext()){
                                Itineraire itineraire = iterator.next();

                                if(tournee.getItinerairesModifiable(pointSelectionne,  Duration.ofMinutes(duree),  itineraire)){
                                    itineraireSelectionne = itineraire;
                                    break;
                                }
                                /*else{
                                    timeLineItineraires.get(itineraire).getLine().setStrokeWidth(6);
                                    //timeLineItineraires.get(itineraire).getLine().getStrokeDashArray().addAll(4d);
                                }*/
                            }

                            if(itineraireSelectionne!=null){
                                listeDeCdes.ajoute(new CdeAjout(tournee, pointSelectionne, Duration.ofMinutes(duree), itineraireSelectionne, mapPane));
                            }

                            //Recalcul tournée
                            mapPane.getChildren().clear();
                            timeLineBuild(rightPane, tournee,mapPane,primaryStage, false);
                            plan.print(mapPane);
                            tournee.print(mapPane);

                            vehicule = new Point("", tournee.getDemandeDeLivraison().getEntrepot().getX(), tournee.getDemandeDeLivraison().getEntrepot().getY());
                            vehicule.setVehicule();
                            vehicule.print(mapPane);

                            popOver.hide();
                        }});

                    vBoxAjoutInPopover.getChildren().add(buttonAjoutInPopover);
                    vBoxAjoutInPopover.setAlignment(Pos.CENTER);

                    panePopOver.getChildren().add(vBoxAjoutInPopover);
                    panePopOver.setPadding(new Insets(5));

                    popOver.setAutoHide(false);
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

    }

    /**
     * Supprime les zones blanches d'une image, pour les rendre transparentes
     * @param inputImage : L'image à modifier
     * @return Une instance d'Image, dont les zones blanches ont été rendues transparentes
     */
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

    /**
     * Déplace le camion sur la timeline, ainsi que sur le plan
     */
    EventHandler<MouseEvent> deliveryOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneY = t.getSceneY();
                    orgTranslateY = ((ImageView)(t.getSource())).getTranslateY();
                }
            };

    /**
     * Déplace le camion sur la timeline, ainsi que sur le plan
     */
    EventHandler<MouseEvent> deliveryOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateY;
                    if (t.getSceneY() <= yFirstPoint) {
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
                        if (troncon != null) {
                            troncon.setLongueurParcourue(mapPane, troncon.getLongueur() * progress);
                        }
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
                            if (troncon != null) {
                                troncon.setLongueurParcourue(mapPane, troncon.getLongueur() * progress);
                            }
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
                        if (troncon != null) {
                            troncon.setLongueurParcourue(mapPane, troncon.getLongueur() * progress);
                        }
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


    /**
     * Transforme un LocalTime en secondes
     * @param time le LocalTime à passer en secondes
     * @return la valeur correspondante en secondes
     */
    private double localTimeToSecond(LocalTime time){
        return (time.getHour()*60*60 + time.getMinute()*60 + time.getSecond());
    }

    /**
     * Construit le titre de la timeline
     * @param rightPane Le pane sur lequel doit s'afficher la timeline
     */
    private void buildTimelineTitle(Pane rightPane){
        //Titre
        Label lblTimeline = new Label("Timeline");
        lblTimeline.setPadding(new Insets(10));
        //Right vBox
        VBox rightVbox = new VBox();
        rightVbox.getChildren().add(lblTimeline);
        rightVbox.setAlignment(Pos.TOP_CENTER);
        rightVbox.setPrefSize(bandeauWidth, bandeauHeigth);

        //Right Pane
        rightPane.getChildren().add(rightVbox);
    }
}