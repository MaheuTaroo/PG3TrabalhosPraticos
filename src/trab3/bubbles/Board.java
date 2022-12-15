package trab3.bubbles;

import trab3.bubbles.pieces.Bubble;

public interface Board extends BubbleListener {
	int getNumberOfLines();
	int getNumberOfColumns();
	Bubble getBubble(int l, int c);
	void putBubble( Bubble b);
	void putHole(int l, int c);
}
