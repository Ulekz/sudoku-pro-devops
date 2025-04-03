package com.sudoku.ui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class SudokuBoard extends GridPane {

    private final TextField[][] cells = new TextField[9][9];

    public SudokuBoard(int[][] solution) {
        setAlignment(Pos.CENTER);
        setHgap(0);
        setVgap(0);
        setStyle("""
            -fx-padding: 20;
            -fx-background-color: #ffffff;
            -fx-border-color: #9b59b6;
            -fx-border-width: 3px;
            -fx-border-radius: 10px;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.3, 0, 5);
        """);


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(60, 60);
                cell.setFont(Font.font("Consolas", 20));
                cell.setStyle("-fx-background-color: #fafafa; -fx-border-color: #ccc;");
                cell.setAlignment(Pos.CENTER);

                StringBuilder style = new StringBuilder("-fx-background-color: white; -fx-border-color: #ccc;");

                if (col % 3 == 0) style.append(" -fx-border-left-width: 2px;");
                if (row % 3 == 0) style.append(" -fx-border-top-width: 2px;");
                if (col == 8) style.append(" -fx-border-right-width: 2px;");
                if (row == 8) style.append(" -fx-border-bottom-width: 2px;");

                cell.setStyle(style.toString());

                // Solo permitir números del 1 al 9
                final int r = row;
                final int c = col;
                cell.textProperty().addListener((obs, oldValue, newValue) -> {
                    if (!newValue.matches("[1-9]?")) {
                        cell.setText(oldValue);
                    } else {
                        // Verificar contra la solución
                        if (newValue.isEmpty()) {
                            cell.setStyle(style.toString());
                        } else {
                            int entered = Integer.parseInt(newValue);
                            if (entered == solution[r][c]) {
                                cell.setStyle(style + "-fx-background-color: #ccffcc;"); // Verde
                            } else {
                                cell.setStyle(style + "-fx-background-color: #ffcccc;"); // Rojo
                            }
                        }
                    }
                });

                cells[row][col] = cell;
                add(cell, col, row);
            }
        }
    }

    public TextField[][] getCells() {
        return cells;
    }
}
