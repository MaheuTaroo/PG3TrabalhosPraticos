package trab3.bubbles.pieces;

import trab3.bubbles.Board;

import java.util.function.Predicate;

public class Black extends SelectableBubble {
	public Black(Board b, int l, int c ) {
		super(b,l,c);
	}
	public int getColor() {
		return 1;
	}

	public void select() {
		selectIf(b -> b.getColumn() == getColumn());
	}

	protected void selectContiguos(Predicate<Bubble> pred) {
		for (int l = 0; l < board.getNumberOfLines(); l++)
			board.getBubble(l, getColumn()).selectIf(pred);
	}
}
