package trab3.bubbles;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.*;

public class HowToPlay extends JDialog {
    public HowToPlay() {
        // TODO - acabar ficheiro bubbles.txt e formular janela

        super();
        setTitle("How to play");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(500, 500);
        setResizable(false);

        try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\bubbles.txt"))) {
            String[] tips = br.lines().toArray(String[]::new);

            JTextArea howToPlay = new JTextArea();
            //howToPlay.read(br, null);

            JScrollPane scroll = new JScrollPane(howToPlay);
            scroll.setVerticalScrollBar(scroll.createVerticalScrollBar());
            scroll.setHorizontalScrollBar(scroll.createHorizontalScrollBar());

            howToPlay.setEditable(false);

            for (int i = 0; i < tips.length; i++) {
                howToPlay.append(tips[i]);
                if (i != tips.length - 1) howToPlay.append("\n");
            }

            JButton close = new JButton();
            close.addActionListener(al -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

            Container c = getContentPane();

            c.add(scroll, BorderLayout.CENTER);
            c.add(close, BorderLayout.SOUTH);

            setVisible(true);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error opening the \"how to play\" window: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
