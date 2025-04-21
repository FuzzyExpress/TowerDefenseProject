package Utility;

public class ScoreManager {
    private int score;

    public ScoreManager() {
        this.score = 5000; // Starting score
    }

    public void addPoints(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public boolean canAfford(int amount) {
        return score >= amount;
    }

    public void reset() {
        score = 5000;
    }

    public void deductScore(int amount) {
        score = Math.max(0, score - amount);
    }

}
