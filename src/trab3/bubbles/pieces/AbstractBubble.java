package trab3.bubbles.pieces;

import trab3.bubbles.Board;

import java.util.Objects;

public abstract class AbstractBubble implements Bubble {
	protected final Board board;
	private int line, column;
	public AbstractBubble(Board b, int l, int c ) {
		board = b; line=l; column= c;
	}
	public final int getLine()    { return line;    }
	public final int getColumn()  { return column;  }

	public boolean moveTo( int l, int c ) {
		line=l; column=c;
		board.putBubble( this);
		return true;
	}
	
	public boolean equals( Object o ) {
		if ( !(o instanceof Bubble b) ) return false;
		return getColumn()==b.getColumn() &&
				getLine()==b.getLine() &&
				getColor() == b.getColumn() &&
				isSelected() == b.isSelected();
	}

	public int hashCode() {
		return Objects.hash(getLine(), getColumn(), getColor(), isSelected());
	}

	public String toString()  { return getLine()+":"+getColumn(); }
}
