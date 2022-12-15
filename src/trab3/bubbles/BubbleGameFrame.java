package trab3.bubbles;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import trab3.bubbles.pieces.Bubble;
import trab3.bubbles.strategies.Strategy;
import trab3.bubbles.strategies.StrategyGravitational;

public class BubbleGameFrame extends JFrame implements GameListener {
	public static final int GRID_WIDTH = 35;
	public static final Color HOLE_COLOR = Color.CYAN;
	protected final JPanel board;
	protected Game game;
	//ScorePanel current, best; //TODO
	//Players statistics;       //TODO
	private Timer t = new Timer(1000, this::updateTime);

	private void updateTime(ActionEvent actionEvent) {
		//TODO
	}

	public static class Itens  {
		private JMenuItem munItem;
		private ActionListener listener;
		public Itens(String text, ActionListener al) { this(new JMenuItem(text),al); }
		public Itens(JMenuItem mi, ActionListener al){ munItem = mi; listener = al;  }
		public JMenuItem getMenuItem()    { return munItem;  }
		public ActionListener getAction() { return listener; }
	}

	public final  Itens[] menuGameItens = {
			new Itens("start", e -> game.start()),
			new Itens("stop", e -> game.stop()),
			new Itens("exit", e -> System.exit(0))};

	public BubbleGameFrame(String title, Game g, Strategy s ) {
		super(title);
		setIconImage( new ImageIcon("src/trab3/bubbles.png").getImage());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container cp = this.getContentPane();

		game = g;
		game.addListener(this );
		game.setStrategy( s );

		cp.add( board = createBoard( g ) );

		JMenuBar mb= new JMenuBar();
		mb.add( createMenu("Game", menuGameItens) );
		setJMenuBar( mb );

		pack();
		setResizable(false);
	}

	// <<  Métodos auxiliares >>
	private static JMenu createMenu(String title, Itens... itens ){
		JMenu menu = new JMenu(title);
		for ( Itens item: itens) {
			JMenuItem mi = item.getMenuItem();
			if (item.getAction() != null)
				mi.addActionListener( item.getAction() );
			menu.add( mi );
		}
		return menu;
	}

	private static JPanel createBoard( Game game ) {
		JPanel p = new JPanel( new GridLayout(game.getNumberOfLines(),game.getNumberOfColumns()) );
		for (int i= 0; i < game.getNumberOfLines()*game.getNumberOfColumns(); ++i ) {
			JButton b= new JButton();
			b.setPreferredSize(new Dimension(GRID_WIDTH, GRID_WIDTH));
			b.setBackground(HOLE_COLOR);
			int pos = i;
			b.addActionListener( e -> game.select(pos/game.getNumberOfColumns(), pos%game.getNumberOfColumns()) );
			p.add(b);
		}
		return p;
	}

	// << Implementação da inteface BubbleListener */
	protected Color[] colors = { HOLE_COLOR, Color.WHITE, Color.BLACK,
	                             Color.RED, Color.GREEN, Color.BLUE,
			                     Color.YELLOW, Color.PINK, Color.MAGENTA };

	// Obter o componente que se encontra em determinada linha/coluna
	protected JComponent getComponent(int line, int col ) {
		return (JComponent)board.getComponent(line*game.getNumberOfColumns() + col);
	}
	public void selected( Bubble b )
		{ getComponent(b.getLine(), b.getColumn()).setBackground( Color.GRAY );             }
	public void unselected( Bubble p )
		{ getComponent(p.getLine(), p.getColumn()).setBackground(colors[p.getColor()+1]);   }

	// << Implementação da inteface GameListener >>
	public void scoreChange(Score s ) {	}
	public void gameStart(Score s)    {
		String msg = String.format("Time is %d, start with %d bubbles and %d points", s.time, s.bubbles, s.points);
		JOptionPane.showMessageDialog( this, msg, "START GAME", JOptionPane.PLAIN_MESSAGE);
	}
	public void gameStop(Score s)     {
		String msg = String.format("In %d seconds remain %d bubbles with %d points", s.time, s.bubbles, s.points);
		JOptionPane.showMessageDialog( this, msg, "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void main(String[] args) {
		new BubbleGameFrame("Bubbles",
				                 new BubbleGame(14,12),
				                 new StrategyGravitational() ).setVisible(true);
	}
}