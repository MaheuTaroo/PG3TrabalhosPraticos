package trab3.bubbles;

public class Score implements Comparable<Score> {
    // TODO - devem ser acrescentados novos métodos
    public final int time, bubbles, points;
    public Score(int time, int bubble, int score) {
        this.time = time;
        this.bubbles = bubble;
        this.points = score;
    }

    public int compareTo(Score o) {
        if (points != o.points) return points - o.points;
        if (bubbles != o.bubbles) return bubbles - o.bubbles;
        return time - o.time;
    }
}
