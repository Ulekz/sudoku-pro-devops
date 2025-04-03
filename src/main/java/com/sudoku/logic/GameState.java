package com.sudoku.logic;

import java.io.Serializable;

public class GameState implements Serializable {
    private int[][] currentBoard;
    private int[][] solution;
    private boolean[][] lockedCells;

    public GameState(int[][] currentBoard, int[][] solution, boolean[][] lockedCells) {
        this.currentBoard = currentBoard;
        this.solution = solution;
        this.lockedCells = lockedCells;
    }

    public int[][] getCurrentBoard() {
        return currentBoard;
    }

    public int[][] getSolution() {
        return solution;
    }

    public boolean[][] getLockedCells() {
        return lockedCells;
    }
}
