package views.client_fx;

import shared.models.DataValidation;
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
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LayoutHelpers {

    static void prepareStage(Stage stage, String title, int minWidth, int maxWidth, int minHeight, int maxHeight) {
        stage.setMinWidth(minWidth);
        stage.setMaxWidth(maxWidth);
        stage.setMinHeight(minHeight);
        stage.setMaxHeight(maxHeight);

        stage.setTitle(title);
    }

    static void createPanelHeader(String text, Consumer<Node> addToScene) {
        var header = new Label(text);
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFont(Font.font(18));

        addToScene.accept(header);
    }

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
        }

        tableColumns.get(tableColumns.size() - 1).prefWidthProperty().setValue(tableWidth - widthAccum);
    }

    static Button createButton(String text, int width, Consumer<Node> addToScene) {
        Button button = new Button(text);
        button.setMinWidth(width);
        button.setMaxWidth(width);

        addToScene.accept(button);

        return button;
    }

    static ChoiceBox<String> createChoiceBox(List<String> choices, Consumer<Node> addToScene) {
        ChoiceBox<String> choiceBox = new javafx.scene.control.ChoiceBox<>();
        for (String choice : choices) {
            choiceBox.getItems().add(choice);
        }

        addToScene.accept(choiceBox);

        return choiceBox;
    }

    static TextField createLabelledField(String labelText, Consumer<Node> addLabelToScene, Consumer<Node> addFieldToScene) {
        Label label = new Label(labelText);
        TextField field = new TextField();

        addLabelToScene.accept(label);
        addFieldToScene.accept(field);

        return field;
    }

    static void addTextFieldEvents(TextField field, Text validationText, BiConsumer<String, BiConsumer<String, DataValidation>> controllerFunc) {
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

    static Text createValidationText(int wrappingWidth, Consumer<Node> addToScene) {
        Text validationText = new Text("\n");
        validationText.setFill(Color.RED);
        validationText.setFont(new Font(10));
        validationText.setWrappingWidth(wrappingWidth);
        addToScene.accept(validationText);

        return validationText;
    }

    static VBox createVBox(Pos alignment, Insets padding, int spacing, int minWidth, Consumer<Node> addToScene) {
        VBox vBox = new VBox();
        vBox.setAlignment(alignment);
        vBox.setPadding(padding);
        vBox.setSpacing(spacing);
        vBox.setMinWidth(minWidth);

        addToScene.accept(vBox);

        return vBox;
    }

    static HBox createHBox(Pos alignment, int spacing, Consumer<Node> addToScene) {
        HBox hBox = new HBox();
        hBox.setAlignment(alignment);
        hBox.setSpacing(spacing);

        addToScene.accept(hBox);

        return hBox;
    }

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
