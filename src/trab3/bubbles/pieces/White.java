package trab3.bubbles.pieces;

import trab3.bubbles.Board;

import java.util.function.Predicate;

public class White extends SelectableBubble {
	public White(Board b, int l, int c ) { super(b,l,c);                    }
	public int getColor( )               { return 0;                        }

	public void select() {
		selectIf( b -> b.getLine() == getLine());
	}

	protected void selectContiguos(Predicate<Bubble> pred ) {
		for (int c = 0; c < board.getNumberOfColumns(); ++c )
			board.getBubble(getLine(), c).selectIf(pred);
	}

}
