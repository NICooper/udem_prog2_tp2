package client.client_fx;

import client.controllers.ClientController;
import client.models.Sessions;
import client.views.ClientView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import models.Course;

import java.util.List;

import static client.client_fx.LayoutHelpers.*;

public class ClientFx extends Application implements ClientView {
    static ClientController controller;

    @Override
    public void start(Stage stage) {
        var root = createHBox(Pos.CENTER, 10, (node) -> {});

        var coursePane = createVBox(Pos.TOP_CENTER, new Insets(10, 6, 10, 6), 10, 240, (node) -> root.getChildren().add(node));

        createPanelHeader("Liste des cours", (node) -> coursePane.getChildren().add(node));

        var observableCourseList = FXCollections.<Course>observableArrayList();
        TableView<Course> table = createTwoColumnTable(
                "Code", "code", "Course", "name", observableCourseList,
                (node) -> coursePane.getChildren().add(node)
        );

        coursePane.getChildren().add(new Separator());

        var courseFilterPane = createHBox(Pos.CENTER, 50, (node) -> coursePane.getChildren().add(node));

        var sessionSelector = createChoiceBox(List.of(Sessions.getSessions()), (node) -> courseFilterPane.getChildren().add(node));
        sessionSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            controller.setSession(newVal, (text, validation) -> {});
        });
        sessionSelector.getSelectionModel().select(0);

        var loadCoursesButton = createButton("Charger", 80, (node) -> courseFilterPane.getChildren().add(node));
        var courseValidation = createValidationText(200, (node) -> coursePane.getChildren().add(node));

        loadCoursesButton.setOnAction((event) ->
            controller.loadCourseList((result) -> {
                observableCourseList.setAll(result.data);
                if (result.success) {
                    courseValidation.setText("\n");
                } else {
                    courseValidation.setText(result.message);
                }
            })
        );

        root.getChildren().add(new Separator());


        var registrationPane = createVBox(Pos.TOP_CENTER, new Insets(10, 6, 10, 6), 10, 240, (node) -> root.getChildren().add(node));

        createPanelHeader("Formulaire d'inscription", (node) -> registrationPane.getChildren().add(node));

        int gridCol1Width = 80;
        int gridCol2Width = 160;
        var grid = createGrid(new Insets(24, 0, 24, 0), new int[] {gridCol1Width, gridCol2Width}, (node) -> registrationPane.getChildren().add(node));

        var firstNameField = createLabelledField("Prénom", (node) -> grid.add(node, 0, 0, 1, 1), (node) -> grid.add(node, 1, 0, 1, 1));
        var firstNameValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 1, 1, 1));
        addTextFieldEvents(firstNameField, firstNameValidation, controller::setFirstName);

        var lastNameField = createLabelledField("Nom", (node) -> grid.add(node, 0, 2, 1, 1), (node) -> grid.add(node, 1, 2, 1, 1));
        var lastNameValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 3, 1, 1));
        addTextFieldEvents(lastNameField, lastNameValidation, controller::setLastName);

        var emailField = createLabelledField("Courriel", (node) -> grid.add(node, 0, 4, 1, 1), (node) -> grid.add(node, 1, 4, 1, 1));
        var emailValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 5, 1, 1));
        addTextFieldEvents(emailField, emailValidation, controller::setEmail);

        var matriculeField = createLabelledField("Matricule", (node) -> grid.add(node, 0, 6, 1, 1), (node) -> grid.add(node, 1, 6, 1, 1));
        var matriculeValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 7, 1, 1));
        addTextFieldEvents(matriculeField, matriculeValidation, controller::setMatricule);

        var sendPane = createHBox(Pos.CENTER, 0, (node) -> grid.add(node, 1, 8, 1, 1));

        var sendButton = createButton("Envoyer", 80, (node) -> sendPane.getChildren().add(node));
        var sendValidation = createValidationText(gridCol2Width, (node) -> grid.add(node, 1, 9, 1, 1));
        sendValidation.setTextAlignment(TextAlignment.CENTER);
        sendButton.setDisable(true);

        controller.registerFormStatusCallback((valid) -> sendButton.setDisable(!valid));

        sendButton.setOnAction((event) ->
            controller.registerToCourse(
                (result) -> {
                    sendValidation.setText(result.message);
                    if (result.success) {
                        table.getSelectionModel().clearSelection();
                        sendButton.setDisable(true);
                    }
                }
            )
        );

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                controller.setCourse(newVal, (course, validation) -> {});
                sendValidation.setText("\n");
            }
        });

        var scene = new Scene(root, 600, 400);

        stage.setScene(scene);
        prepareStage(stage, "Inscription UdeM", 560, 640, 320, 480);
        stage.show();

        formatTable(table, 70);

//        sendButton.setDisable(true);

//        courses.setAll(new Course("Programmation 2", "IFT1025", "Hiver"), new Course("Genie Logiciel", "IFT2255", "Automne"));

    }

    public static void main(ClientController clientController) {
        controller = clientController;
        launch();
    }
}