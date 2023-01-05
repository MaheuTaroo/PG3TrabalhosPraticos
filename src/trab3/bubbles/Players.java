package trab3.bubbles;

public class Players {
    String playerName;
    int gamesPlayed, gamesWon,
        bestScore, avgScore,
        bestTime, avgTime,
        minBubbles, avgBubbles, maxBubbles;

    public Players(String name, int playedGames, int victories,
                                int bestPoints, int averagePoints,
                                int quickestTime, int averageTime,
                                int leastBubbles, int averageBubbles, int mostBubbles) {
        playerName = name;
        gamesPlayed = playedGames;
        gamesWon = victories;
        bestScore = bestPoints;
        avgScore = averagePoints;
        bestTime = quickestTime;
        avgTime = averageTime;
        minBubbles = leastBubbles;
        avgBubbles = averageBubbles;
        maxBubbles = mostBubbles;
    }

    public Players(String name) {
        this(name, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public boolean equals(Object o) {
        return o instanceof Players && playerName.equals(((Players) o).playerName);
    }
}
