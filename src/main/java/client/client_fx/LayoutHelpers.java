package client.client_fx;

import client.models.DataValidation;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Un enemble de fonctions qui servent à créer des elements visuels d'une application JavaFX avec quelques propriétés
 * de base.
 */
public class LayoutHelpers {

    /**
     * Crée un nouveau Label qui sera utilisé comme titre d'une section d'une Scene.
     * @param text Le texte qui sera affiché.
     * @param addToScene Une fonction qui ajoute le Label au Node approprié de la Scene.
     */
    static void createPanelHeader(String text, Consumer<Node> addToScene) {
        var header = new Label(text);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(18));

        addToScene.accept(header);
    }

    /**
     * Crée une TableView qui contient deux colonnes et l'initialise.
     * @param col1Name Nom de la première colonne.
     * @param col1PropName Nom de la variable de T dont la donnée remplira la première colonne.
     * @param col2Name Nom de la deuxième colonne.
     * @param col2PropName Nom de la variable de T dont la donnée remplira la deuxième colonne.
     * @param tableData Une ObservableList d'objets T qui remplira la table.
     * @param addToScene Une fonction qui ajoute la TableView au Node approprié de la Scene.
     * @return La TableView créée par cette fonction.
     * @param <T> Le type des objets qui rempliront la TableView.
     */
    static <T> TableView<T> createTwoColumnTable(
            String col1Name, String col1PropName,
            String col2Name, String col2PropName,
            ObservableList<T> tableData,
            Consumer<Node> addToScene
    ) {
        TableView<T> table = new TableView<>();
        table.setItems(tableData);

        TableColumn<T, String> col1 = new TableColumn<>(col1Name);
        col1.setCellValueFactory(new PropertyValueFactory<>(col1PropName));

        TableColumn<T, String> col2 = new TableColumn<>(col2Name);
        col2.setCellValueFactory(new PropertyValueFactory<>(col2PropName));

        table.getColumns().setAll(col1, col2);

        addToScene.accept(table);

        return table;
    }

    /**
     * Change la largeur des colonne d'une TableView. La dernière colonne remplira la largeur de la TableView qui reste
     * peu importe la valeur choisie. À noter, le Stage auquel la TableView apartient doit être affiché pour que la
     * largeur de la TableView soit connue.
     * @param table La TableView à modifier.
     * @param widths Les largeurs voulues en pixels des colonnes de table.
     * @param <T> Le type des objets dans TableView.
     */
    static <T> void formatTable(TableView<T> table, Integer... widths) {
        var tableWidth = table.getWidth();
        var tableColumns = table.getColumns();

        int widthAccum = 0;
        for (int i = 0; i < widths.length; i++) {
            if (i < tableColumns.size()) {
                int width = widths[i];
                widthAccum += width;
                tableColumns.get(i).prefWidthProperty().setValue(width);
            }
            else {
                break;
            }
        }

        tableColumns.get(tableColumns.size() - 1).prefWidthProperty().setValue(tableWidth - widthAccum);
    }

    /**
     * Crée un nouveau Button et l'initialise.
     * @param text Le String qui apparaîtra sur le Button
     * @param width La largeur du Button en pixels.
     * @param addToScene Une fonction qui ajoute le Button au Node approprié de la Scene.
     * @return Le Button créé.
     */
    static Button createButton(String text, int width, Consumer<Node> addToScene) {
        Button button = new Button(text);
        button.setMinWidth(width);
        button.setMaxWidth(width);

        addToScene.accept(button);

        return button;
    }

    /**
     * Crée une nouvelle ChoiceBox et l'initialise.
     * @param choices Une List de String qui seront les options de la ChoiceBox.
     * @param addToScene Une fonction qui ajoute la ChoiceBox au Node approprié de la Scene.
     * @return La ChoiceBox créée.
     */
    static ChoiceBox<String> createChoiceBox(List<String> choices, Consumer<Node> addToScene) {
        ChoiceBox<String> choiceBox = new javafx.scene.control.ChoiceBox<>();
        for (String choice : choices) {
            choiceBox.getItems().add(choice);
        }

        addToScene.accept(choiceBox);

        return choiceBox;
    }

    /**
     * Crée un TextField avec son Label correspondant.
     * @param labelText Le String qui sera affiché sur le Label.
     * @param addLabelToScene Une fonction qui ajoute le Label au Node approprié de la Scene.
     * @param addFieldToScene Une fonction qui ajoute le TextField au Node approprié de la Scene.
     * @return Le TextField créé.
     */
    static TextField createLabelledField(String labelText, Consumer<Node> addLabelToScene, Consumer<Node> addFieldToScene) {
        Label label = new Label(labelText);
        TextField field = new TextField();

        addLabelToScene.accept(label);
        addFieldToScene.accept(field);

        return field;
    }

    /**
     * Ajoute des handler d'évènements à un TextField qui peut être validé.
     * Chaque fois que le texte dans TextField change, controllerFunc sera appelée. Quand le TextField perd le focus,
     * controllerFunc sera appelé et, selon le résultat de la validation, soit un bord rouge et du texte de validation
     * sera affichés, soit le bord rouge et le texte de validation sera cachés.
     * texte dans TextField est valide.
     * @param field Le TextField auquel les handlers seront ajoutés.
     * @param validationText Un Node Text de JavaFX qui va contenir le texte de validation.
     * @param controllerFunc Une fonction qui prend comme paramètres un String (le nouveau texte dans le TextField) et
     *                       une fonction qui elle prend le texte validé et un objet DataValidation qui déterminera si
     *                       le message de validation sera affiché et le texte dans ce message.
     */
    static void addTextFieldEvents(
            TextField field,
            Text validationText,
            BiConsumer<String, BiConsumer<String, DataValidation>> controllerFunc
    ) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            controllerFunc.accept(newVal, (name, validation) -> {});
        });

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                controllerFunc.accept(field.getText(), (name, validation) -> {
                    if (validation.isValid) {
                        validationText.setText("\n");
                        field.setText(name);
                        field.setStyle("");
                    }
                    else {
                        validationText.setText(validation.validationMessage);
                        field.setStyle("-fx-border-color: red;");
                    }
                });
            }
        });
    }

    /**
     * Crée un emplacement pour du texte et l'initialise avec les valeurs voulues pour afficher des erreurs de validation.
     * @param wrappingWidth Largeur maximale d'espace donné au texte de validation.
     * @param addToScene Une fonction qui ajoute ce Text au Node approprié de la Scene.
     * @return Le node Text créé.
     */
    static Text createValidationText(int wrappingWidth, Consumer<Node> addToScene) {
        Text validationText = new Text("\n");
        validationText.setFill(Color.RED);
        validationText.setFont(new Font(10));
        validationText.setWrappingWidth(wrappingWidth);
        addToScene.accept(validationText);

        return validationText;
    }

    /**
     * Crée une VBox et initialise quelques propriétés communes.
     * @param alignment Objet Pos de JavaFX qui sert à définir le positionnement horizontal et vertical d'un Node.
     * @param padding Objet Insets de JavaFX qui sert à définir la marge autour d'un Node.
     * @param spacing L'espace en pixels entre chaque Node de la VBox.
     * @param minWidth La largeur minimale de la VBox.
     * @param addToScene Une fonction qui ajoute la VBox au Node approprié de la Scene.
     * @return La VBox créée.
     */
    static VBox createVBox(Pos alignment, Insets padding, int spacing, int minWidth, Consumer<Node> addToScene) {
        VBox vBox = new VBox();
        vBox.setAlignment(alignment);
        vBox.setPadding(padding);
        vBox.setSpacing(spacing);
        vBox.setMinWidth(minWidth);

        addToScene.accept(vBox);

        return vBox;
    }

    /**
     * Crée une HBox et initialise quelques propriétés communes.
     * @param alignment Objet Pos de JavaFX qui sert à définir le positionnement horizontal et vertical d'un Node.
     * @param spacing L'espace en pixels entre chaque Node de la HBox.
     * @param addToScene Une fonction qui ajoute la HBox au Node approprié de la Scene.
     * @return La HBox créée.
     */
    static HBox createHBox(Pos alignment, int spacing, Consumer<Node> addToScene) {
        HBox hBox = new HBox();
        hBox.setAlignment(alignment);
        hBox.setSpacing(spacing);

        addToScene.accept(hBox);

        return hBox;
    }

    /**
     * Crée un GridPane et initialise quelques propriétés communes.
     * @param padding Objet Insets de JavaFX qui sert à définir la marge autour d'un Node.
     * @param columnWidths Les largeurs de chaque colonne du GridPane.
     * @param addToScene Une fonction qui ajoute le GridPane au Node approprié de la Scene.
     * @return Le GridPane créé.
     */
    static GridPane createGrid(Insets padding, int[] columnWidths, Consumer<Node> addToScene) {
        var grid = new GridPane();
        grid.setPadding(padding);
        for (int columnWidth : columnWidths) {
            grid.getColumnConstraints().add(new ColumnConstraints(columnWidth));
        }

        addToScene.accept(grid);

        return grid;
    }
}
