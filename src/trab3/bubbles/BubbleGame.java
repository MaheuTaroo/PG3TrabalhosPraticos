package trab3.bubbles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.IntUnaryOperator;

import trab3.bubbles.pieces.*;
import trab3.bubbles.strategies.Strategy;
import trab3.bubbles.strategies.StrategyGravitational;

public class BubbleGame implements Game, Board {
	// A DUMMY_BUBBLE é uma bolha fiticia que permite que não
	//  existam testes aos limites do tabuleiro.
	protected final static Bubble DUMMY_BUBBLE = new Dummy();
	// Utilizado para preencher aleatóriamente as cores no tabuleiro
	private static final Random randomize = new Random();

	// Posições em que podem ser colocadas bolhas - usado no preenchimento aleatório
	private final int[] positionsShuffle; // Depende da dimensão do tabuleiro

	// Percentagem de bolhas para cada cor.
	private final int[]  percentagePerColor= { 1, 1, 21, 21, 21, 12, 12, 12 };
	private final int[]  numberPerColor; // Depende da dimensão do tabuleiro

	private final Bubble [][] board; // Tabuleiro de jogo - existem 2 linhas
	                                 // a mais e por cada linha 2 colunas.

	// Objectos que pretendem ser avisados de modificações no jogo
	private final ArrayList<GameListener> listener = new ArrayList<>();
	// Estratégia do jogo -
	private Strategy strategy = new StrategyGravitational();

	// Informação que vai sendo atualizada ao longo do jogo
	private int bubbles, points;
	long timeStart, // Colocado com a hora currente no start.
		 timeStop;  // Colocado a 0 no start e atualizado com a hora currente do stop.

	public BubbleGame( int numberLines, int numberColumns )       { 
		board = new Bubble[ numberLines+2 ] [ numberColumns+2 ];

		int numberOfBubbles = numberLines* numberColumns;
		numberPerColor = calculateNumberPerColor(percentagePerColor, numberOfBubbles, 2);
		positionsShuffle = fillPositions(numberOfBubbles, IntUnaryOperator.identity());

		// Preencher o tabuleiro com a bolha fiticia - DUMMY_BUBBLE.
		for ( int line= 0; line < board.length-1; ++line )
			Arrays.fill( board[line], DUMMY_BUBBLE);
		board[board.length-1] = board[0];
	}

	// << Implementação da interface Board >>
	public final int getNumberOfColumns() { return board[0].length-2; }
	public final int getNumberOfLines()   { return board.length-2;    }
	public Bubble getBubble(int l, int c) { return board[l+1][c+1];   }
	public void putBubble( Bubble b ) {
		board[ b.getLine()+1 ][ b.getColumn()+1 ] = b;
		unselected( b );
	}
	public void putHole(int line, int column) {
		putBubble(new Hole(this, line, column));
		if ( line == getNumberOfLines()-1 )
			strategy.freeColumn(this, column);
	}

	// << Implementação da interface  BubbleListener >>
	public void selected( Bubble b )   { strategy.add( b );	listener.forEach(li-> li.selected( b )); }
	public void unselected( Bubble b ) { listener.forEach(li-> li.unselected( b ));                  }

	// << Implementação da interface Game >>
	public final Score getScore() { return new Score(getTime(),bubbles, points); }
	public int getTime() {
		long time = (timeStop == 0)? System.currentTimeMillis() : timeStop;
		return (int)( time-timeStart)/1000;
	}
	public void addListener(GameListener v) { listener.add(v);       }

	public void setStrategy(Strategy s )    { stop(); strategy = s;  }

	public void select(int line, int column ) {
		Bubble b = getBubble(line, column);
		if ( b.isSelected() ) {
			int n = strategy.removeSelected( this );
			bubbles -= n;
			points += b.getColor() * Math.pow(2, n - 2);
			listener.forEach(li-> li.scoreChange( getScore() ));
		}
		else
			strategy.select( b );
	}

	public void start() {
		if ( timeStop == 0 ) stop();
		timeStop = 0;
		timeStart = System.currentTimeMillis();
		points = 0;
		bubbles = getNumberOfLines()*getNumberOfColumns();
		fillBoard( positionsShuffle.length, numberPerColor, positionsShuffle ) ;
		listener.forEach( li-> li.gameStart( getScore() ) );
	}
	
	public void stop() {
		if ( timeStop < timeStart ) {
			timeStop = System.currentTimeMillis();
			int time = getTime();
			if(bubbles == 0)
				points += time;
			else
				points += time/bubbles;
			listener.forEach(li-> li.gameStop( getScore() ));
		}
	}

	// << Métodos auxiliares para preencher o tabuleiro >>

	/**
	 * Produz um array com posições do tabuleiro que deveram ser preenchidas.
	 * As posições são calculadas por uma determinada função.
	 * @param numberOfBubbles - numero de posições a preencher.
	 * @param operation - função para calculo da posição
	 * @return o array de posições
	 */
	private int[] fillPositions(int numberOfBubbles, IntUnaryOperator operation) {
		int[] array = new int[numberOfBubbles];
		for (int pos = 0; pos < numberOfBubbles; ++pos )
			array[pos] = operation.applyAsInt(pos);
		return array;
	}

	/**
	 *  TODO - Descrever o algoritmo usado
	 * @param percentages - percentagem que deve ter cada cor
	 * @param totalOfBubbles - numero total de bolhas
	 * @return número de bolhas por cor
	 */
	private int[] calculateNumberPerColor(int[] percentages, int totalOfBubbles, int minimum) {
		int value;

		int [] colors = new int[8];
		for(int i = 0; i < percentages.length; i++) {
			value = percentages[i]*totalOfBubbles/100;
			if(i < 2) {
				value = value > 2 ? value : minimum;
			}
			colors[i] = value;
		}
		return colors;
	}

	/**
	 * TODO - Explicar o algoritmo e comentar o código
	 * @param numberOfBubbles número total de peças
	 * @param numbersColor número de peças para cada cor
	 * @param positions array que contém todas as posiçoes das bolhas
	 * @return
	 */
	private void fillBoard(int numberOfBubbles, int[] numbersColor, int[] positions) {
		for ( int color = 0; color < numbersColor.length; ++color) {
			for ( int numberOfColor = 0; numberOfColor < numbersColor[color] ; ++numberOfColor) {
				int randomIndex = randomize.nextInt(numberOfBubbles--);
				int pos= positions[randomIndex]; // Posição a ser ocupada
				positions[ randomIndex ] = positions[ numberOfBubbles ];
				positions[ numberOfBubbles ] = pos;
				int  line = pos/getNumberOfColumns(), column = pos%getNumberOfColumns();
				switch ( color ) {
					case 0 -> putBubble(new White(this, line, column));
					case 1 -> putBubble(new Black(this, line, column));
					default -> {
						if (color < numbersColor.length - 3)
							putBubble(new CrossBubble(this, line, column, color));
						else
							putBubble(new DiagonalBubble(this, line, column, color));
					}
				}
			}
		}
	}
}
