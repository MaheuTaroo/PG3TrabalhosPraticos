package trab3.bubbles;

import java.util.ArrayList;
import java.util.Arrays;
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
	private final int[] percentagePerColor = { 1, 1, 21, 21, 21, 12, 12, 12 };
	private final int[] numberPerColor; // Depende da dimensão do tabuleiro

	private final Bubble[][] board; // Tabuleiro de jogo - existem 2 linhas
	                                // a mais e por cada linha 2 colunas.

	// Objectos que pretendem ser avisados de modificações no jogo
	private final ArrayList<GameListener> listener = new ArrayList<>();
	// Estratégia do jogo -
	private Strategy strategy = new StrategyGravitational();

	// Informação que vai sendo atualizada ao longo do jogo
	private int bubbles, points;
	long timeStart, // Colocado com a hora currente no start.
		 timeStop;  // Colocado a 0 no start e atualizado com a hora currente do stop.

	public BubbleGame(int numberLines, int numberColumns)       {
		board = new Bubble[numberLines + 2][numberColumns + 2];

		int numberOfBubbles = numberLines * numberColumns;
		numberPerColor = calculateNumberPerColor(percentagePerColor, numberOfBubbles, 2);
		positionsShuffle = fillPositions(numberOfBubbles, IntUnaryOperator.identity());

		// Preencher o tabuleiro com a bolha fiticia - DUMMY_BUBBLE.
		for (int line= 0; line < board.length - 1; ++line)
			Arrays.fill(board[line], DUMMY_BUBBLE);
		board[board.length-1] = board[0];
	}

	// << Implementação da interface Board >>
	public final int getNumberOfColumns() {
		return board[0].length - 2;
	}
	public final int getNumberOfLines() {
		return board.length-2;
	}
	public Bubble getBubble(int l, int c) {
		return board[l+1][c+1];
	}
	public void putBubble( Bubble b ) {
		board[b.getLine() + 1][b.getColumn() + 1] = b;
		unselected(b);
	}
	public void putHole(int line, int column) {
		putBubble(new Hole(this, line, column));
		if (line == getNumberOfLines() - 1)
			strategy.freeColumn(this, column);
	}

	// << Implementação da interface  BubbleListener >>
	public void selected(Bubble b) {
		 strategy.add(b);
		 listener.forEach(li-> li.selected(b));
	}
	public void unselected(Bubble b) {
		listener.forEach(li-> li.unselected(b));
	}

	// << Implementação da interface Game >>
	public final Score getScore() {
		return new Score(getTime(), bubbles, points);
	}
	public int getTime() {
		long time = timeStop == 0 ? System.currentTimeMillis() : timeStop;
		return (int)(time - timeStart) / 1000;
	}
	public void addListener(GameListener v) {
		listener.add(v);
	}

	public void setStrategy(Strategy s) {
		stop();
		strategy = s;
	}

	public void select(int line, int column ) {
		Bubble b = getBubble(line, column);
		if ( b.isSelected() ) {
			int n = strategy.removeSelected( this );
			bubbles -= n;
			points += b.getColor() * Math.pow(2, n - 2);
			listener.forEach(li-> li.scoreChange( getScore() ));
		}
		else
			strategy.select(b);
	}

	public void start() {
		if (timeStop == 0) stop();
		timeStop = 0;
		timeStart = System.currentTimeMillis();
		points = 0;
		bubbles = getNumberOfLines()*getNumberOfColumns();
		fillBoard(positionsShuffle.length, numberPerColor, positionsShuffle) ;
		listener.forEach(li -> li.gameStart(getScore()));
	}
	
	public void stop() {
		if (timeStop < timeStart) {
			timeStop = System.currentTimeMillis();
			int t = getTime();
			points += t / (bubbles == 0 ? 1 : bubbles);
			listener.forEach(li -> li.gameStop(getScore()));
		}
	}

	// << Métodos auxiliares para preencher o tabuleiro >>

	/**
	 * Produz um array com posições do tabuleiro que deveram ser preenchidas.
	 * As posições são calculadas por uma determinada função.
	 * @param numberOfBubbles número de posições a preencher.
	 * @param operation função para cálculo da posição
	 * @return o array de posições
	 */
	private int[] fillPositions(int numberOfBubbles, IntUnaryOperator operation) {
		int[] array = new int[numberOfBubbles];
		for (int pos = 0; pos < numberOfBubbles; pos++)
			array[pos] = operation.applyAsInt(pos);
		return array;
	}

	/**
	 * Calcula a quantidade de bolhas de uma determinada cor por colocar no tabuleiro de jogo,
	 * com base na percentagem respetiva e no total de bolhas do tabuleiro.
	 * @param percentages percentagem que deve ter cada cor
	 * @param totalOfBubbles número total de bolhas
	 * @return número de bolhas por cor
	 */
	private int[] calculateNumberPerColor(int[] percentages, int totalOfBubbles, int minimum) {
		int value;
		int[] colors = new int[8];

		for(int i = 0; i < percentages.length; i++) {
			value = (percentages[i] * totalOfBubbles) / 100;

			/* Define a percentagem de bolhas a introduzir no tabuleiro,
			 * prevenindo que as bolhas brancas e pretas ultrapassem o
			 * valor mínimo indicado */
			colors[i] = i < 2 ? Math.max(value, minimum) : value;
		}
		return colors;
	}

	/**
	 * Preenche o tabuleiro de forma aleatória, de acordo com as cores das peças e das suas
	 * posições. O preenchimento aleatório recorre à troca de bolhas entre a última posição
	 * não trocada (a partir do último elemento) e uma posição escolhida aleatoriamente, e
	 * aplica uma bolha nessa posição dependendo da sua cor.
	 * @param numberOfBubbles número total de peças
	 * @param numbersColor número de peças para cada cor
	 * @param positions array que contém todas as posições das bolhas
	 */
	private void fillBoard(int numberOfBubbles, int[] numbersColor, int[] positions) {
		for (int color = 0; color < numbersColor.length; color++) {
			for (int numberOfColor = 0; numberOfColor < numbersColor[color]; numberOfColor++) {
				// Aborta a troca quando não houverem mais bolhas a trocar
				if (numberOfBubbles == 0) break;

				// Troca o valor no índice obtido com o valor no último valor não trocado
				int randomIndex = randomize.nextInt(numberOfBubbles--);
				int pos= positions[randomIndex]; // Posição a ser ocupada
				positions[randomIndex] = positions[numberOfBubbles];
				positions[numberOfBubbles] = pos;

				//Posiciona a bolha no tabuleiro dependendo da cor
				int  line = pos / getNumberOfColumns(), column = pos % getNumberOfColumns();
				switch (color) {
					case 0 -> putBubble(new White(this, line, column));
					case 1 -> putBubble(new Black(this, line, column));
					default -> {
						putBubble(color < numbersColor.length - 3 ? new CrossBubble(this, line, column, color) :
																	new DiagonalBubble(this, line, column, color));
					}
				}
			}

			// Aborta a troca quando não houverem mais bolhas a trocar
			if (numberOfBubbles == 0) break;
		}
	}
}
