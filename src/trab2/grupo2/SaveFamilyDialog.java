package trab2.grupo2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
            FileNameExtensionFilter familiesFilter = new FileNameExtensionFilter("Families file", "fam");
            jfc.addChoosableFileFilter(familiesFilter);
            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = jfc.getSelectedFile().getAbsolutePath();
                if (!filename.endsWith(".fam")) filename += ".fam";
                try (PrintWriter pw = new PrintWriter(filename)) {
                    StringBuilder text = new StringBuilder();
                    for (String name : families.getNames((String)surnameCombo.getSelectedItem())) {
                        pw.println(name);
                    }
                    close(ae);
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred while saving the names: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            jfc.removeChoosableFileFilter(familiesFilter);
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
