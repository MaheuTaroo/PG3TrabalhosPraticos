package trab3.bubbles.pieces;

import trab3.bubbles.Board;

import java.util.function.Predicate;

public class DiagonalBubble extends SelectableBubble {
	// TODO - A classe que estende e o código implementado é só para que possa ser instanciado
	private static final Dir[] DIRECTIONS = { Dir.UP, Dir.DOWN, Dir.LEFT, Dir.RIGHT, Dir.LEFT_UP, Dir.LEFT_DOWN, Dir.RIGHT_UP, Dir.RIGHT_DOWN};
	private int color;
	public DiagonalBubble(Board b, int l, int c, int cl) {
		super(b, l, c);
		color = cl;
	}
	public int getColor() {	return color; }

	public void select() {
		selectIf(b -> b.getColor() == getColor());
	}

	protected void selectContiguos(Predicate<Bubble> pred) {
		for (Dir d: DIRECTIONS)
			board.getBubble(getLine() + d.deltaLine, getColumn() + d.deltaColumn).selectIf(pred);
	}
}
