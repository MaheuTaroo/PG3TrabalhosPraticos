package trab3.bubbles;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    //TODO
    private String playerName;
    private Score score;

    private final JLabel[] labels = { new JLabel("00:00"), new JLabel("0"), new JLabel("0") };

    public ScorePanel(Score s, int time, String player) {
        super(new GridLayout(1, 2));

        setScore(s);
        setTime(time);
        setPlayerName(player);

        JPanel currentGame = new JPanel(new GridBagLayout()), bestGame = new JPanel(new GridBagLayout());

        // TODO - acabar layout
    }

    public ScorePanel() {
        this(new Score(0, 0, 0), 0, "UNDEFINED");
    }

    public Score getScore() {
        return score;
    }
    public void setScore(Score s) {
        score = s;
        setTime(s.time);
        labels[1].setText(String.valueOf(s.bubbles));
        labels[2].setText(String.valueOf(s.points));
    }

    public int getTime() {
        return score.time;
    }
    public void setTime(int time) {
        score = new Score(time, score.bubbles, score.points);
        labels[0].setText(String.format("%2d:%2d", time / 60, time % 60));
    }

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String player) {
        playerName = player;
    }
}
