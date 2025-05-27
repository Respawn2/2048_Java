package com.example.game;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveStrategy {
    private static final Logger logger = LoggerFactory.getLogger(MoveStrategy.class);

    public enum Direction { LEFT, RIGHT, UP, DOWN }

    public boolean move(int[] grid, GameState state, Direction direction) {
        boolean moved = false;
        if (direction == Direction.LEFT) {
            moved = moveLeft(grid, state);
        } else if (direction == Direction.RIGHT) {
            moved = moveRight(grid, state);
        } else if (direction == Direction.UP) {
            moved = moveUp(grid, state);
        } else if (direction == Direction.DOWN) {
            moved = moveDown(grid, state);
        }
        logger.debug("Move direction={}, moved={}", direction, moved);
        return moved;
    }

    private boolean moveLeft(int[] grid, GameState state) {
        boolean moved = false;
        for (int row = 0; row < 4; row++) {
            int[] line = new int[4];
            for (int col = 0; col < 4; col++) {
                line[col] = grid[row * 4 + col];
            }
            int[] result = processLine(line, state);
            for (int col = 0; col < 4; col++) {
                int index = row * 4 + col;
                if (grid[index] != result[col]) {
                    moved = true;
                    grid[index] = result[col];
                }
            }
        }
        return moved;
    }

    private boolean moveRight(int[] grid, GameState state) {
        boolean moved = false;
        for (int row = 0; row < 4; row++) {
            int[] line = new int[4];
            for (int col = 0; col < 4; col++) {
                line[col] = grid[row * 4 + (3 - col)];
            }
            int[] result = processLine(line, state);
            for (int col = 0; col < 4; col++) {
                int index = row * 4 + (3 - col);
                if (grid[index] != result[col]) {
                    moved = true;
                    grid[index] = result[col];
                }
            }
        }
        return moved;
    }

    private boolean moveUp(int[] grid, GameState state) {
        boolean moved = false;
        for (int col = 0; col < 4; col++) {
            int[] line = new int[4];
            for (int row = 0; row < 4; row++) {
                line[row] = grid[row * 4 + col];
            }
            int[] result = processLine(line, state);
            for (int row = 0; row < 4; row++) {
                int index = row * 4 + col;
                if (grid[index] != result[row]) {
                    moved = true;
                    grid[index] = result[row];
                }
            }
        }
        return moved;
    }

    private boolean moveDown(int[] grid, GameState state) {
        boolean moved = false;
        for (int col = 0; col < 4; col++) {
            int[] line = new int[4];
            for (int row = 0; row < 4; row++) {
                line[row] = grid[(3 - row) * 4 + col];
            }
            int[] result = processLine(line, state);
            for (int row = 0; row < 4; row++) {
                int index = (3 - row) * 4 + col;
                if (grid[index] != result[row]) {
                    moved = true;
                    grid[index] = result[row];
                }
            }
        }
        return moved;
    }

    private int[] processLine(int[] line, GameState state) {
        List<Integer> filtered = new ArrayList<>();
        for (int value : line) {
            if (value != 0) filtered.add(value);
        }

        List<Integer> merged = new ArrayList<>();
        for (int i = 0; i < filtered.size(); i++) {
            if (i + 1 < filtered.size() && filtered.get(i).equals(filtered.get(i + 1))) {
                int newValue = filtered.get(i) * 2;
                merged.add(newValue);
                state.setScore(state.getScore() + newValue);
                i++;
            } else {
                merged.add(filtered.get(i));
            }
        }

        int[] result = new int[4];
        for (int i = 0; i < merged.size(); i++) {
            result[i] = merged.get(i);
        }
        return result;
    }
}