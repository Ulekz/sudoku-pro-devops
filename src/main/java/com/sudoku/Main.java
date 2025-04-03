package com.sudoku;

import com.sudoku.logic.*;
import com.sudoku.ui.SudokuBoard;
import com.sudoku.ui.TopScoresView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.sudoku.logic.SudokuGenerator.Difficulty;
import com.sudoku.logic.SudokuGenerator;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main extends Application {

    private int[][] solution;
    private TextField[][] cells;
    private TimeRecord timeRecord;
    private String currentPlayerName;
    private final File historyFile = new File("history.dat");

    @Override
    public void start(Stage primaryStage) {
        loadTimeRecords();

        TextInputDialog dialog = new TextInputDialog("Jugador");
        dialog.setTitle("Perfil de jugador");
        dialog.setHeaderText("Ingresa tu nombre para continuar:");
        dialog.setContentText("Nombre:");

        dialog.showAndWait().ifPresent(name -> {
            currentPlayerName = name.trim().isEmpty() ? "Jugador Anónimo" : name.trim();
        });

        showDifficultySelector(primaryStage);
    }

    private void loadTimeRecords() {
        File file = new File("records.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                timeRecord = (TimeRecord) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                timeRecord = new TimeRecord();
            }
        } else {
            timeRecord = new TimeRecord();
        }
    }

    private void saveTimeRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("records.dat"))) {
            oos.writeObject(timeRecord);
            System.out.println("🎯 Récord actualizado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGameHistory(GameHistoryEntry entry) {
        try (FileOutputStream fos = new FileOutputStream(historyFile, true);
             ObjectOutputStream oos = historyFile.length() == 0 ?
                     new ObjectOutputStream(fos) :
                     new AppendingObjectOutputStream(fos)) {
            oos.writeObject(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<GameHistoryEntry> loadTopScores(String difficulty) {
        List<GameHistoryEntry> all = new ArrayList<>();

        if (historyFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile))) {
                while (true) {
                    try {
                        GameHistoryEntry entry = (GameHistoryEntry) ois.readObject();
                        if (entry.getDifficulty().equalsIgnoreCase(difficulty)) {
                            all.add(entry);
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        all.sort(Comparator.comparingInt(GameHistoryEntry::getTimeInSeconds));
        return all.stream().limit(10).toList();
    }

    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private void showDifficultySelector(Stage stage) {
        Label label = new Label("Selecciona la dificultad:");
        Button easy = new Button("Fácil");
        Button medium = new Button("Media");
        Button hard = new Button("Difícil");

        easy.setOnAction(e -> launchGame(stage, Difficulty.EASY));
        medium.setOnAction(e -> launchGame(stage, Difficulty.MEDIUM));
        hard.setOnAction(e -> launchGame(stage, Difficulty.HARD));

        VBox selector = new VBox(15, label, easy, medium, hard);
        selector.setAlignment(Pos.CENTER);
        selector.setPadding(new Insets(30));

        Scene scene = new Scene(selector, 300, 200);
        stage.setTitle("Sudoku Pro - Selección de Dificultad");
        stage.setScene(scene);
        stage.show();
    }

    private void launchGame(Stage stage, SudokuGenerator.Difficulty difficulty) {
        int[][] puzzle = SudokuGenerator.generatePuzzleBoard(difficulty);
        solution = SudokuGenerator.getSolution();

        SudokuBoard board = new SudokuBoard(solution);
        cells = board.getCells();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = puzzle[row][col];
                if (value != 0) {
                    cells[row][col].setText(String.valueOf(value));
                    cells[row][col].setEditable(false);
                    cells[row][col].setStyle(cells[row][col].getStyle() + "-fx-background-color: #e0e0e0;");
                }
            }
        }

        Button hintButton = new Button("Mostrar Pista");
        Button verifyButton = new Button("Verificar Tablero");
        Button saveButton = new Button("Guardar Partida");
        Button loadButton = new Button("Cargar Partida");
        Button topButton = new Button("Ver Top 10");

        topButton.setOnAction(e -> {
            List<GameHistoryEntry> top = loadTopScores(difficulty.name());
            TopScoresView.showTopScores(top, difficulty.name());
        });

        Label timerLabel = new Label("Tiempo: 00:00");
        timerLabel.setStyle("""
            -fx-font-size: 16px;
            -fx-font-family: 'Consolas';
            -fx-text-fill: #333;
        """);

        Integer best = timeRecord.getBestTime(difficulty.name());
        if (best != null) {
            int min = best / 60;
            int sec = best % 60;
            timerLabel.setText("Tiempo: 00:00  |  Récord: " + String.format("%02d:%02d", min, sec));
        }

        final int[] seconds = {0};
        final Timeline[] timeline = new Timeline[1];

        timeline[0] = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            seconds[0]++;
            int min = seconds[0] / 60;
            int sec = seconds[0] % 60;
            timerLabel.setText(String.format("Tiempo: %02d:%02d", min, sec));
        }));
        timeline[0].setCycleCount(Timeline.INDEFINITE);
        timeline[0].play();

        hintButton.setOnAction(e -> {
            outer:
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (cells[row][col].getText().isEmpty()) {
                        int correctValue = solution[row][col];
                        cells[row][col].setText(String.valueOf(correctValue));
                        cells[row][col].setEditable(false);
                        cells[row][col].setStyle(cells[row][col].getStyle() + "-fx-background-color: #d0e7ff;");
                        break outer;
                    }
                }
            }
        });

        verifyButton.setOnAction(e -> {
            boolean complete = true;
            boolean correct = true;

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = cells[row][col].getText();
                    if (text.isEmpty()) {
                        complete = false;
                        continue;
                    }

                    int value = Integer.parseInt(text);
                    if (value != solution[row][col]) {
                        correct = false;
                        cells[row][col].setStyle(cells[row][col].getStyle().replace("-fx-background-color: #ccffcc;", ""));
                        cells[row][col].setStyle(cells[row][col].getStyle() + "-fx-background-color: #ffcccc;");
                    }
                }
            }

            if (complete && correct) {
                timeline[0].stop();
                int finalTime = seconds[0];
                Integer bestTime = timeRecord.getBestTime(difficulty.name());

                GameHistoryEntry entry = new GameHistoryEntry(
                        currentPlayerName,
                        difficulty.name(),
                        finalTime,
                        LocalDateTime.now()
                );
                saveGameHistory(entry);

                if (bestTime == null || finalTime < bestTime) {
                    timeRecord.setBestTime(difficulty.name(), finalTime);
                    saveTimeRecords();
                    showAlert(Alert.AlertType.INFORMATION, "¡Nuevo récord!",
                            "¡Ganaste y rompiste el récord con " + formatTime(finalTime) + "!");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "¡Felicidades!", "Has completado el Sudoku correctamente.");
                }
            } else if (!complete) {
                showAlert(Alert.AlertType.WARNING, "Sudoku Incompleto", "Aún faltan celdas por completar.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Errores encontrados", "Hay errores en el tablero.");
            }
        });

        saveButton.setOnAction(e -> {
            int[][] currentBoard = new int[9][9];
            boolean[][] locked = new boolean[9][9];

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = cells[row][col].getText();
                    currentBoard[row][col] = text.isEmpty() ? 0 : Integer.parseInt(text);
                    locked[row][col] = !cells[row][col].isEditable();
                }
            }

            GameState state = new GameState(currentBoard, solution, locked);

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sudoku_save.dat"))) {
                oos.writeObject(state);
                System.out.println("✅ Partida guardada.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        loadButton.setOnAction(e -> {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sudoku_save.dat"))) {
                GameState state = (GameState) ois.readObject();
                int[][] loadedBoard = state.getCurrentBoard();
                solution = state.getSolution();
                boolean[][] locked = state.getLockedCells();

                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        int value = loadedBoard[row][col];
                        cells[row][col].setText(value == 0 ? "" : String.valueOf(value));
                        cells[row][col].setEditable(!locked[row][col]);

                        if (!locked[row][col]) {
                            cells[row][col].setStyle("-fx-background-color: white;");
                        } else {
                            cells[row][col].setStyle("-fx-background-color: #e0e0e0;");
                        }
                    }
                }

                System.out.println("✅ Partida cargada.");
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        String buttonStyle = """
            -fx-font-size: 14px;
            -fx-font-family: 'Segoe UI';
            -fx-padding: 8 16;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
        """;

        verifyButton.setStyle(buttonStyle + "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        hintButton.setStyle(buttonStyle + "-fx-background-color: #3498db; -fx-text-fill: white;");
        saveButton.setStyle(buttonStyle + "-fx-background-color: #f1c40f; -fx-text-fill: black;");
        loadButton.setStyle(buttonStyle + "-fx-background-color: #e67e22; -fx-text-fill: white;");
        topButton.setStyle(buttonStyle + "-fx-background-color: #9b59b6; -fx-text-fill: white;");

        HBox buttonRow = new HBox(10, verifyButton, hintButton, saveButton, loadButton, topButton);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(10, 0, 0, 0));

        VBox root = new VBox(20, timerLabel, board, buttonRow);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #eae2f8, #d3cce3);");

        Scene scene = new Scene(root, 640, 750);
        stage.setTitle("Sudoku Pro - " + difficulty.name());
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
