package trab3.bubbles.pieces;

import trab3.bubbles.Board;

import java.util.function.Predicate;

public class Hole extends AbstractBubble {
	public Hole(Board m, int l, int c ) { super(m, l, c); }
	public int getColor()                           { return -1;      }
	public boolean isSelected()                     { return false;   }

	public void unselect()                          {  }
	public void select()                            {  }

	public boolean selectIf(Predicate<Bubble> pred) { return false;   }
	public boolean moveTo( int l, int c ) {
		if ( c == getColumn() ) return false;// Os buracos só se movem na horizontal
		return super.moveTo(l, c);
	}

	public String toString() { return super.toString()+ " hole"; }

}