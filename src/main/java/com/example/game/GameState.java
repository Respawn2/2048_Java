package com.example.game;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameState {
    private static final Logger logger = LoggerFactory.getLogger(GameState.class);
    private static GameState instance;
    private int[] grid;
    private int score;
    private int targetValue;
    private int difficulty;
    private boolean gameOver;
    private boolean win;

    private GameState() {
        reset();
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void reset() {
        grid = new int[16];
        score = 0;
        //difficulty = 1;
        //targetValue = (int) Math.pow(2, 3 + difficulty);
        gameOver = false;
        win = false;
        addNewTile();
        addNewTile();
        logger.info("Game reset: targetValue={}", targetValue);
    }

    public void setDifficulty(int level) {
        difficulty = Math.max(1, Math.min(10, level));
        targetValue = (int) Math.pow(2, 3 + difficulty);
        reset();
        logger.info("Difficulty set: level={}, targetValue={}", level, targetValue);
    }

    public boolean addNewTile() {
        if (gameOver || win) {
            logger.debug("Cannot add tile: gameOver={} or win={}", gameOver, win);
            return false;
        }

        Random rand = new Random();
        int emptyCells = 0;
        for (int cell : grid) {
            if (cell == 0) emptyCells++;
        }
        if (emptyCells == 0) {
            logger.debug("No empty cells for new tile");
            return false;
        }

        int pos;
        do {
            pos = rand.nextInt(16);
        } while (grid[pos] != 0);

        grid[pos] = rand.nextFloat() < 0.9 ? 2 : 4;
        logger.debug("New tile added at position {}: value={}", pos, grid[pos]);
        return true;
    }

    public boolean isGameOver() {
        if (win) {
            logger.debug("Game not over: already won");
            return false;
        }
        if (gridContains(0)) {
            logger.debug("Game not over: empty cells exist");
            return false;
        }

        for (int i = 0; i < 16; i++) {
            int row = i / 4;
            int col = i % 4;
            if (col < 3 && grid[i] == grid[i + 1]) {
                logger.debug("Game not over: horizontal merge possible at {}", i);
                return false;
            }
            if (row < 3 && grid[i] == grid[i + 4]) {
                logger.debug("Game not over: vertical merge possible at {}", i);
                return false;
            }
        }
        gameOver = true;
        logger.info("Game over: no moves left");
        return true;
    }

    public boolean isWin() {
        if (win) {
            logger.debug("Win state already set");
            return true;
        }
        for (int cell : grid) {
            logger.debug("Checking tile: value={}, targetValue={}", cell, targetValue);
            if (cell == targetValue) {
                win = true;
                logger.info("Win detected: tile={} matches targetValue={}", cell, targetValue);
                return true;
            }
        }
        return false;
    }

    private boolean gridContains(int value) {
        for (int cell : grid) {
            if (cell == value) return true;
        }
        return false;
    }

    public int[] getGrid() { return grid; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getTargetValue() { return targetValue; }
    public boolean isGameOverFlag() { return gameOver; }
    public boolean isWinFlag() { return win; }
}