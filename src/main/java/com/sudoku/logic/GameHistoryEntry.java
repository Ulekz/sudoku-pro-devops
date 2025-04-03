package com.sudoku.logic;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameHistoryEntry implements Serializable {
    private final String playerName;
    private final String difficulty;
    private final int timeInSeconds;
    private final LocalDateTime dateTime;

    public GameHistoryEntry(String playerName, String difficulty, int timeInSeconds, LocalDateTime dateTime) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.timeInSeconds = timeInSeconds;
        this.dateTime = dateTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
