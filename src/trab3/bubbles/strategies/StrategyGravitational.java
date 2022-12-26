package trab3.bubbles.strategies;

import java.util.*;

import trab3.bubbles.Board;
import trab3.bubbles.pieces.Bubble;

public class StrategyGravitational implements Strategy {
	//Conjunto de bolhas selecionadas
	protected final List<Bubble> bubbles = new ArrayList<Bubble>();
	//Numero de bolhas minimas do conjunto
	private final int minimum;

	public StrategyGravitational(int min) {
		this.minimum = min;
	}
	public StrategyGravitational() {
		this(2);
	}

	public int numberOfBubbles() {
		return bubbles.size();
	}
	public void add(Bubble p) {
		bubbles.add(p);
	}

	/**
	 * Seleciona um conjunto de bolhas originado a partir da bolha
	 * clicada,caso essa seleção possua mais do que o número mínimo
	 * de bolhas obrigatório.
	 * @param origin bolha que foi selecionada primeiro
	 * @return o número de bolhas selecionadas
	 */
	public int select(Bubble origin) {
		if (!bubbles.isEmpty()) unselect();
		origin.select();
		if (numberOfBubbles() < minimum) unselect();
		return numberOfBubbles();
	}

	/**
	 * Remove o conjunto de bolhas selecionado, ordena o tabuleiro
	 * e aplica as regras da estratégia selecionada
	 * @param board tabuleiro
	 * @return o número de bolhas eliminadas
	 */
	public int removeSelected( Board board ) {
		int n = numberOfBubbles();
		// Ordena nas colunas e para a mesma coluna das
		// linhas maiores para as menores
		Comparator<Bubble> cmp = (b1, b2) -> {
			int res = b1.getColumn() - b2.getColumn();
			return res != 0 ? res : b2.getLine() - b1.getLine();
		};
		Collections.sort(bubbles, cmp);
		strategy(board);
		return n;
	}

	/**
	 * Elimina as bolhas selecionadas e move as bolhas posicionadas
	 * nas linhas superiores tantas colunas quanto o número total de
	 * colunas ocupadas pelas bolhas selecionadas
	 * @param board tabuleiro
	 */
	protected void strategy(Board board) {
		int n = bubbles.size();
		for (Bubble bubbleRemove: bubbles) {
			if (bubbleRemove.isSelected()) {
				int putLine = bubbleRemove.getLine(), line;
				for (line = putLine - 1; line >= 0; --line) {
					Bubble bubbleMove = board.getBubble(line, bubbleRemove.getColumn());
					if (!bubbleMove.isSelected()) {
						if (bubbleMove.moveTo(putLine, bubbleRemove.getColumn()))
							--putLine;
						else break;
					} else
						bubbleMove.unselect();
				}
				for (++line; line <= putLine; ++line) {
					board.putHole(line, bubbleRemove.getColumn());
				}
			}
		}
		bubbles.clear();
	}

	/**
	 * Liberta a coluna do tabuleiro indicada pelo índice
	 * fornecido, preenchendo-a com espaços vazios
	 * @param board tabuleiro
	 * @param column coluna a libertar
	 */
	public void freeColumn(Board board, int column ) {
		if (column != board.getNumberOfColumns() - 1) {
			for (int l = 0; l < board.getNumberOfLines(); ++l) {
				Bubble hole  = board.getBubble(l, column);
				int c = column;
				while (board.getBubble(l, c + 1).moveTo(l, c)) ++c;
				hole.moveTo(l, c);
			}
		}
	}

	/**
	 * Desmarca o conjunto de bolhas selecionadas.
	 * @return o número de bolhas que foram desmarcadas
	 */
	private int unselect() {
		// Guarda o número de bolhas para poder retornar
		// no fim o número de bolhas desmarcadas
		int numberSelected = numberOfBubbles();
		// Desmarca cada uma das bolhas do conjunto evocando
		// o método unselect sobre cada bolha
		bubbles.forEach(Bubble::unselect);
		// Assinala que o conjunto está vazio
		bubbles.clear();
		return numberSelected;
	}

	// Identificação desta estratégia
	public String toString() {
		return "gravitational";
	}
}
