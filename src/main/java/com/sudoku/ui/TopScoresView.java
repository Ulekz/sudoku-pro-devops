package com.sudoku.ui;

import com.sudoku.logic.GameHistoryEntry;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class TopScoresView {

    public static void showTopScores(List<GameHistoryEntry> topEntries, String difficulty) {
        TableView<GameHistoryEntry> table = new TableView<>();

        TableColumn<GameHistoryEntry, String> playerCol = new TableColumn<>("Jugador");
        playerCol.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createStringBinding(() -> {
            String name = cell.getValue().getPlayerName();
            return (name == null || name.trim().isEmpty()) ? "Anónimo" : name;
        }));

        TableColumn<GameHistoryEntry, String> timeCol = new TableColumn<>("Tiempo");
        timeCol.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createStringBinding(() -> {
            int t = cell.getValue().getTimeInSeconds();
            return String.format("%02d:%02d", t / 60, t % 60);
        }));

        TableColumn<GameHistoryEntry, String> dateCol = new TableColumn<>("Fecha");
        dateCol.setCellValueFactory(cell -> javafx.beans.binding.Bindings.createStringBinding(
                () -> cell.getValue().getDateTime().toLocalDate().toString()));

        // 💎 Estilo especial para el top 1
        Callback<TableColumn<GameHistoryEntry, String>, TableCell<GameHistoryEntry, String>> highlightTop = col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                if (!empty && getIndex() == 0) {
                    setStyle("-fx-font-weight: bold; -fx-text-fill: gold;");
                } else {
                    setStyle("");
                }
            }
        };

        playerCol.setCellFactory(highlightTop);
        timeCol.setCellFactory(highlightTop);

        table.getColumns().addAll(playerCol, timeCol, dateCol);
        table.getItems().addAll(topEntries);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox root = new VBox(table);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #f0f0ff);");
        root.setPrefSize(420, 320);

        Stage dialog = new Stage();
        dialog.setTitle("Top 10 - " + difficulty);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root));
        dialog.show();
    }
}
