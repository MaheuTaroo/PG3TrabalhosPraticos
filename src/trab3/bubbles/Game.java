package trab3.bubbles;

import trab3.bubbles.strategies.Strategy;

public interface Game {
	void setStrategy(Strategy s);
	void addListener(GameListener v);
	
	void select(int l, int c);
	void start();
	void stop();
	
	int getNumberOfLines();
	int getNumberOfColumns();
	Score getScore();
	int getTime();
}
