package views.client_fx;

import controllers.ClientController;
import shared.models.Sessions;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import shared.models.Course;

import java.util.List;

import static views.client_fx.LayoutHelpers.*;

/**
 * Une Application JavaFx qui sert de GUI pour une application cliente par laquelle un utilisateur pour consulter les
 * cours offerts par l'UdeM et de s'inscrire à un des cours.
 */
public class ClientFx extends Application {
    static ClientController controller;

    /**
     * Construit le stage et la scène de l'application JavaFx.
     * Appellé automatiquement par main().
     *
     * @param stage le Stage principal de l'application JavaFX
     */
    @Override
    public void start(Stage stage) {
        var root = createHBox(Pos.CENTER, 10, (node) -> {});


        //////////////
        // Course Pane
        //////////////

        var coursePane = createVBox(Pos.TOP_CENTER, new Insets(10, 6, 10, 6), 10, 240, (node) -> root.getChildren().add(node));

        createPanelHeader("Liste des cours", (node) -> coursePane.getChildren().add(node));


        // Table

        var observableCourseList = FXCollections.<Course>observableArrayList();
        TableView<Course> table = createTwoColumnTable(
                "Code", "code", "Course", "name", observableCourseList,
                (node) -> coursePane.getChildren().add(node)
        );

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                controller.setCourseByCode(newVal.getCode(), (course, validation) -> {});
            }
        });

        coursePane.getChildren().add(new Separator());


        // Session Dropdown

        var courseFilterPane = createHBox(Pos.CENTER, 50, (node) -> coursePane.getChildren().add(node));

        var sessionSelector = createChoiceBox(List.of(Sessions.getSessions()), (node) -> courseFilterPane.getChildren().add(node));
        sessionSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            controller.setSession(newVal, (text, validation) -> {});
        });
        sessionSelector.getSelectionModel().select(0);


        // Load Courses Button

        var loadCoursesButton = createButton("Charger", 80, (node) -> courseFilterPane.getChildren().add(node));
        var courseValidation = createValidationText(200, (node) -> coursePane.getChildren().add(node));

        loadCoursesButton.setOnAction((event) ->
            controller.loadCourseList((result) -> {
                if (result.success) {
                    observableCourseList.setAll(result.data);
                    courseValidation.setText("\n");
                } else {
                    courseValidation.setText(result.message);
                }
            })
        );


        root.getChildren().add(new Separator());

        ////////////////////
        // Registration Pane
        ////////////////////

        var registrationPane = createVBox(Pos.TOP_CENTER, new Insets(10, 6, 10, 6), 10, 240, (node) -> root.getChildren().add(node));

        createPanelHeader("Formulaire d'inscription", (node) -> registrationPane.getChildren().add(node));

        int gridCol1Width = 80;
        int gridCol2Width = 160;
        var grid = createGrid(new Insets(24, 0, 24, 0), new int[] {gridCol1Width, gridCol2Width}, (node) -> registrationPane.getChildren().add(node));


        // First Name

        var firstNameField = createLabelledField("Prénom", (node) -> grid.add(node, 0, 0, 1, 1), (node) -> grid.add(node, 1, 0, 1, 1));
        var firstNameValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 1, 1, 1));
        addTextFieldEvents(firstNameField, firstNameValidation, controller::setFirstName);


        // Last Name

        var lastNameField = createLabelledField("Nom", (node) -> grid.add(node, 0, 2, 1, 1), (node) -> grid.add(node, 1, 2, 1, 1));
        var lastNameValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 3, 1, 1));
        addTextFieldEvents(lastNameField, lastNameValidation, controller::setLastName);


        // Email

        var emailField = createLabelledField("Courriel", (node) -> grid.add(node, 0, 4, 1, 1), (node) -> grid.add(node, 1, 4, 1, 1));
        var emailValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 5, 1, 1));
        addTextFieldEvents(emailField, emailValidation, controller::setEmail);


        // Matricule

        var matriculeField = createLabelledField("Matricule", (node) -> grid.add(node, 0, 6, 1, 1), (node) -> grid.add(node, 1, 6, 1, 1));
        var matriculeValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 7, 1, 1));
        addTextFieldEvents(matriculeField, matriculeValidation, controller::setMatricule);

        var sendPane = createHBox(Pos.CENTER, 0, (node) -> grid.add(node, 1, 8, 1, 1));


        // Send Form Button

        var sendButton = createButton("Envoyer", 80, (node) -> sendPane.getChildren().add(node));
        sendButton.setDisable(true);

        Alert alert = new Alert(Alert.AlertType.NONE, "");

        controller.registerFormStatusCallback((valid) -> sendButton.setDisable(!valid));

        sendButton.setOnAction((event) ->
            controller.registerToCourse(
                (result) -> {
                    if (result.success) {
                        table.getSelectionModel().clearSelection();
                        sendButton.setDisable(true);
                        alert.setAlertType(Alert.AlertType.CONFIRMATION);
                    }
                    else {
                        alert.setAlertType(Alert.AlertType.ERROR);
                    }
                    alert.setContentText(result.message);
                    alert.show();
                }
            )
        );


        ////////////////
        // Present Stage
        ////////////////

        var scene = new Scene(root, 600, 400);

        stage.setScene(scene);

        stage.setMinWidth(560);
        stage.setMaxWidth(640);
        stage.setMinHeight(320);
        stage.setMaxHeight(480);

        stage.setTitle("Inscription UdeM");

        stage.show();

        formatTable(table, 70);
    }

    /**
     * Démarre l'application JavaFx.
     * Le point d'entrée de ClientFx.
     * @param clientController le controlleur par lequel ClientFx peut communiquer avec le modèle.
     */
    public static void main(ClientController clientController) {
        controller = clientController;
        launch();
    }
}