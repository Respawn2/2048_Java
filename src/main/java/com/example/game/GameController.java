package com.example.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private final Game2048 game;

    public GameController() {
        this.game = new Game2048();
    }

    @GetMapping("/state")
    public GameResponse getState() {
        GameResponse response = new GameResponse(game.getGrid(), game.getScore(), game.getTargetValue(), game.isGameOver(), game.isWin());
        logger.debug("Get state: win={}, gameOver={}", response.isWin(), response.isGameOver());
        return response;
    }

    @PostMapping("/move")
    public GameResponse move(@RequestBody MoveRequest request) {
        boolean moved = game.move(request.getDirection());
        GameResponse response = new GameResponse(game.getGrid(), game.getScore(), game.getTargetValue(), game.isGameOver(), game.isWin());
        logger.info("Move processed: direction={}, moved={}, win={}, gameOver={}",
                request.getDirection(), moved, response.isWin(), response.isGameOver());
        return response;
    }

    @PostMapping("/reset")
    public GameResponse reset() {
        game.resetGame();
        GameResponse response = new GameResponse(game.getGrid(), game.getScore(), game.getTargetValue(), game.isGameOver(), game.isWin());
        logger.info("Game reset");
        return response;
    }

    @PostMapping("/difficulty")
    public GameResponse setDifficulty(@RequestBody DifficultyRequest request) {
        game.setDifficulty(request.getLevel());
        GameResponse response = new GameResponse(game.getGrid(), game.getScore(), game.getTargetValue(), game.isGameOver(), game.isWin());
        logger.info("Difficulty set: level={}", request.getLevel());
        return response;
    }

    public static class GameResponse {
        private final int[] grid;
        private final int score;
        private final int targetValue;
        private final boolean gameOver;
        private final boolean win;

        public GameResponse(int[] grid, int score, int targetValue, boolean gameOver, boolean win) {
            this.grid = grid;
            this.score = score;
            this.targetValue = targetValue;
            this.gameOver = gameOver;
            this.win = win;
        }

        public int[] getGrid() { return grid; }
        public int getScore() { return score; }
        public int getTargetValue() { return targetValue; }
        public boolean isGameOver() { return gameOver; }
        public boolean isWin() { return win; }
    }

    public static class MoveRequest {
        private String direction;

        public String getDirection() { return direction; }
        public void setDirection(String direction) { this.direction = direction; }
    }

    public static class DifficultyRequest {
        private int level;

        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
    }
}