package client.client_fx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.Course;

public class ClientFx extends Application {
    String[] sessions = new String[]{"Automne", "Hiver", "Ete"};

    @Override
    public void start(Stage stage) {
        var root = new HBox();
        var panneauGauche = new VBox();
        var etiquetteCours = new Label("Liste des cours");
        etiquetteCours.setTextAlignment(TextAlignment.CENTER);
        etiquetteCours.setFont(Font.font(16));
        var tableauCours = new TableView<Course>();

        var panneauFiltrageCours = new HBox();
        var filtrageDropdown = new javafx.scene.control.ChoiceBox<>();
        for (var session : sessions) {
            filtrageDropdown.getItems().add(session);
        }
        filtrageDropdown.setValue(sessions[0]);
        var boutonCharger = new Button("charger");
        panneauFiltrageCours.getChildren().add(filtrageDropdown);
        panneauFiltrageCours.getChildren().add(boutonCharger);

        panneauGauche.getChildren().add(etiquetteCours);
        panneauGauche.getChildren().add(tableauCours);
        panneauGauche.getChildren().add(new Separator());
        panneauGauche.getChildren().add(panneauFiltrageCours);
        panneauGauche.setSpacing(5);
        root.getChildren().add(panneauGauche);

        root.getChildren().add(new Separator());

        var panneauDroite = new VBox();
        var etiquetteFormulaire = new Label("Formulaire d'inscription");

        var panneauPrenom = new HBox();
        var etiquettePrenom = new Label("Prénom");
        etiquettePrenom.setTextAlignment(TextAlignment.CENTER);
        etiquettePrenom.setFont(Font.font(16));
        var champPrenom = new TextField();
        panneauPrenom.getChildren().add(etiquettePrenom);
        panneauPrenom.getChildren().add(champPrenom);

        var panneauNom = new HBox();
        var etiquetteNom = new Label("Nom");
        var champNom = new TextField();
        panneauNom.getChildren().add(etiquetteNom);
        panneauNom.getChildren().add(champNom);

        var panneauCourriel = new HBox();
        var etiquetteCourriel = new Label("Courriel");
        var champCourriel = new TextField();
        panneauCourriel.getChildren().add(etiquetteCourriel);
        panneauCourriel.getChildren().add(champCourriel);

        var panneauMatricule = new HBox();
        var etiquetteMatricule = new Label("Matricule");
        var champMatricule = new TextField();
        panneauMatricule.getChildren().add(etiquetteMatricule);
        panneauMatricule.getChildren().add(champMatricule);

        var boutonEnvoyer = new Button("envoyer");

        panneauDroite.getChildren().add(etiquetteFormulaire);
        panneauDroite.getChildren().add(panneauPrenom);
        panneauDroite.getChildren().add(panneauNom);
        panneauDroite.getChildren().add(panneauCourriel);
        panneauDroite.getChildren().add(panneauMatricule);
        panneauDroite.getChildren().add(boutonEnvoyer);
        root.getChildren().add(panneauDroite);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);


        Scene scene = new Scene(root, 640, 480);

        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();
    }

    public static void main() {
        launch();
    }
}