package trab3.bubbles.pieces;

import trab3.bubbles.Board;

import java.util.Objects;
import java.util.function.Predicate;

public class CrossBubble extends SelectableBubble {
	private static final Dir[] DIRECTIONS = { Dir.UP, Dir.DOWN, Dir.LEFT, Dir.RIGHT};
	private int color;
	public CrossBubble(Board b, int l, int c, int cl) { super(b, l, c); color= cl; }
	public int getColor() { return color; }

	public void select( ) {
		selectIf(b -> b.getColor() == getColor());
	}

	protected void selectContiguos(Predicate<Bubble> pred ) {
		for ( Dir d: DIRECTIONS )
			board.getBubble(getLine()+d.deltaLine, getColumn()+d.deltaColumn).selectIf(pred);
	}

}
