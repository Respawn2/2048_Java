package com.example.game;

import com.example.game.MoveStrategy.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game2048 {
    private static final Logger logger = LoggerFactory.getLogger(Game2048.class);
    private final GameState state;
    private final MoveStrategy moveStrategy;

    public Game2048() {
        state = GameState.getInstance();
        moveStrategy = new MoveStrategy();
    }

    public boolean move(String direction) {
        if (state.isGameOverFlag() || state.isWinFlag()) {
            logger.debug("Move blocked: gameOver={} or win={}", state.isGameOverFlag(), state.isWinFlag());
            return false;
        }

        Direction moveDirection;
        switch (direction) {
            case "ArrowLeft":
                moveDirection = Direction.LEFT;
                break;
            case "ArrowRight":
                moveDirection = Direction.RIGHT;
                break;
            case "ArrowUp":
                moveDirection = Direction.UP;
                break;
            case "ArrowDown":
                moveDirection = Direction.DOWN;
                break;
            default:
                logger.warn("Invalid direction: {}", direction);
                return false;
        }

        boolean moved = moveStrategy.move(state.getGrid(), state, moveDirection);
        if (moved) {
            state.addNewTile();
            if (state.isWin()) {
                logger.info("Win condition met after move");
            }
            state.isGameOver();
        }
        logger.debug("Move processed: direction={}, moved={}", direction, moved);
        return moved;
    }

    public void resetGame() {
        state.reset();
        logger.info("Game reset");
    }

    public void setDifficulty(int level) {
        state.setDifficulty(level);
        logger.info("Difficulty set to level {}", level);
    }

    public int[] getGrid() {
        return state.getGrid();
    }

    public int getScore() {
        return state.getScore();
    }

    public int getTargetValue() {
        return state.getTargetValue();
    }

    public boolean isGameOver() {
        return state.isGameOverFlag();
    }

    public boolean isWin() {
        return state.isWinFlag();
    }
}