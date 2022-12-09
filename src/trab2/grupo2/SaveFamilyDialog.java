package trab2.grupo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class SaveFamilyDialog extends JDialog {
    private final Families<Collection<String>> families;

    public SaveFamilyDialog(JFrame parent, Families<Collection<String>> f, JFileChooser jfc) {
        super(parent, "Choose a family to save");
        setModal(true);
        families = f;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(300, 200);
        setResizable(false);

        JLabel label = new JLabel("Choose the family to save", SwingConstants.CENTER);

        JComboBox<String> surnameCombo = new JComboBox<>();
        surnameCombo.setEditable(false);
        for (String surname : families.getSurnames()) {
            surnameCombo.addItem(surname);
        }

        JButton save = new JButton("Save"), cancel = new JButton("Cancel");

        save.addActionListener(ae -> {
            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(jfc.getSelectedFile())) {
                    StringBuilder text = new StringBuilder();
                    for (String name : families.getNames((String)surnameCombo.getSelectedItem())) {
                        text.append((text.length() == 0) ? "" : "\n").append(name);
                    }

                    fw.write(text.toString());
                    close(ae);
                }
                catch (IOException ie) {
                    JOptionPane.showMessageDialog(null, "An error occurred while saving the names: " + ie.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancel.addActionListener(this::close);

        Container c = getContentPane();

        JPanel lbPanel = new JPanel(), comboPanel = new JPanel(new GridBagLayout()), buttonPanel = new JPanel(new GridLayout(1, 2));

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        lbPanel.add(label);

        buttonPanel.add(save);
        buttonPanel.add(cancel);

        comboPanel.add(surnameCombo);

        c.add(lbPanel, BorderLayout.NORTH);
        c.add(comboPanel, BorderLayout.CENTER);
        c.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void close(ActionEvent ae) {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
