package trab3.bubbles;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;

public class HowToPlay extends JDialog {
    public HowToPlay() {
        // TODO - acabar ficheiro HowToPlay.txt e formular janela

        super();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\isel\\pg3\\Projetos\\PG3TrabalhosPraticos\\src\\trab3\\HowToPlay.txt"))) {
            String[] tips = br.lines().toArray(String[]::new);
            int index = 0;

        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "There was an error fetching the tutorial: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
