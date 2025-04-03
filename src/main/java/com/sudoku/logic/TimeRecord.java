package com.sudoku.logic;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TimeRecord implements Serializable {
    private final Map<String, Integer> bestTimes = new HashMap<>();

    public Integer getBestTime(String difficulty) {
        return bestTimes.getOrDefault(difficulty, null);
    }

    public void setBestTime(String difficulty, int seconds) {
        bestTimes.put(difficulty, seconds);
    }

    public Map<String, Integer> getAllTimes() {
        return bestTimes;
    }
}
