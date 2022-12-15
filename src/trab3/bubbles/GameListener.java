package trab3.bubbles;

public interface GameListener extends BubbleListener {
	void scoreChange(Score g);
	void gameStop(Score g);
	void gameStart(Score g);
}
