package com.sudoku;

import com.sudoku.logic.SudokuGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SudokuGeneratorTest {

    @Test
    public void testFullBoardGeneration() {
        int[][] board = SudokuGenerator.generateFullBoard();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                assertTrue(board[row][col] >= 1 && board[row][col] <= 9,
                        "Valor inválido en la celda [" + row + "][" + col + "]");
            }
        }
    }
}
