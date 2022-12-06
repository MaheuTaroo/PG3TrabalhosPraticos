package trab2.grupo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class SurnameChooserWindow extends JFrame {
    private final Families<Collection<String>> families;
    private final JFileChooser fileChooser;

    public SurnameChooserWindow(Families<Collection<String>> f, JFileChooser jfc) {
        super("Choose a family to save");
        families = f;
        fileChooser = jfc;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(400, 300);
        setResizable(false);

        JLabel label = new JLabel("Choose the family to save");

        JComboBox<String> combo = new JComboBox<>();
        combo.setEditable(false);
        for (String surname : families.getSurnames()) {
            combo.addItem(surname);
        }

        JButton save = new JButton("Save"), cancel = new JButton("Cancel");

        save.addActionListener(ae -> {
            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(jfc.getSelectedFile())) {
                    String surname = (String)combo.getSelectedItem();
                    fw.write(surname);
                    for (String name : families.getNames(surname)) {
                        fw.write("\n\t" + name);
                    }

                    close(ae);
                }
                catch (IOException ie) {
                    JOptionPane.showMessageDialog(null, "An error occurred while saving the names: " + ie.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        cancel.addActionListener(this::close);

        Container c = getContentPane();
        c.add(label, BorderLayout.NORTH);
        c.add(combo, BorderLayout.CENTER);

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.add(save);
        p.add(cancel);

        c.add(p, BorderLayout.SOUTH);

        // TODO - corrigir modelo desta janela

        setVisible(true);
    }

    private void close(ActionEvent ae) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
