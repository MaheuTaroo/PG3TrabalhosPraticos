package trab2.grupo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Collection;

public class FamilyChooserDialog extends JDialog {

    public FamilyChooserDialog(JFrame parent, Families<Collection<String>> f, String[] familyToReturn) {
        super(parent, "Choose a family to save");
        setModal(true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setSize(300, 200);
        setResizable(false);

        JLabel label = new JLabel("Choose the family to list", SwingConstants.CENTER);

        JComboBox<String> surnameCombo = new JComboBox<>();
        surnameCombo.setEditable(false);
        for (String surname : f.getSurnames()) {
            surnameCombo.addItem(surname);
        }

        JButton choose = new JButton("Choose"), cancel = new JButton("Cancel");

        choose.addActionListener(ae -> {
            familyToReturn[0] = (String)surnameCombo.getSelectedItem();
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        cancel.addActionListener(ae -> {
            familyToReturn[0] = null;
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        Container c = getContentPane();

        JPanel lbPanel = new JPanel(), comboPanel = new JPanel(new GridBagLayout()), buttonPanel = new JPanel(new GridLayout(1, 2));

        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        lbPanel.add(label);

        buttonPanel.add(choose);
        buttonPanel.add(cancel);

        comboPanel.add(surnameCombo);

        c.add(lbPanel, BorderLayout.NORTH);
        c.add(comboPanel, BorderLayout.CENTER);
        c.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
