package com.sudoku.logic;

import java.util.Random;

public class SudokuGenerator {
    private static final int SIZE = 9;
    private static final int[][] board = new int[SIZE][SIZE];
    private static final Random rand = new Random();

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    /**
     * Genera un tablero completo válido.
     */
    public static int[][] generateFullBoard() {
        clearBoard();
        fillBoard(0, 0);
        return copyBoard(board);
    }

    /**
     * Genera un tablero con celdas ocultas según la dificultad.
     */
    public static int[][] generatePuzzleBoard(Difficulty difficulty) {
        generateFullBoard(); // Genera la solución completa

        int[][] puzzle = copyBoard(board);

        int cellsToRemove = switch (difficulty) {
            case EASY -> 30;
            case MEDIUM -> 45;
            case HARD -> 55;
        };

        while (cellsToRemove > 0) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);

            if (puzzle[row][col] != 0) {
                puzzle[row][col] = 0;
                cellsToRemove--;
            }
        }

        return puzzle;
    }

    /**
     * Retorna la solución completa generada.
     */
    public static int[][] getSolution() {
        return copyBoard(board);
    }

    // ---------------------------------------
    // MÉTODOS PRIVADOS
    // ---------------------------------------

    private static boolean fillBoard(int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;

        int[] numbers = shuffleArray();

        for (int num : numbers) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;

                if (fillBoard(nextRow, nextCol)) return true;

                board[row][col] = 0;
            }
        }

        return false;
    }

    private static boolean isSafe(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[boxRowStart + i][boxColStart + j] == num)
                    return false;

        return true;
    }

    private static int[] shuffleArray() {
        int[] nums = new int[SIZE];
        for (int i = 0; i < SIZE; i++) nums[i] = i + 1;

        for (int i = SIZE - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        return nums;
    }

    private static int[][] copyBoard(int[][] original) {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    private static void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0;
            }
        }
    }
}
