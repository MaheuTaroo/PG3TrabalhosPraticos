package trab2.grupo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;

public class AppWindow extends JFrame {
    private final JTextField name, surname;
    private final JTextArea list;
    private final Families<Collection<String>> families;
    JFileChooser jfc = new JFileChooser(".");

    public AppWindow() {
        super("Families");
        families = new Families<>(TreeMap::new, ArrayList::new);
        setDefaultCloseOperation(EXIT_ON_CLOSE);



        name = new JTextField();
        applyBorder(name, "Name");

        surname = new JTextField();
        applyBorder(surname, "Surname");

        list = new JTextArea();
        list.setEditable(false);
        applyBorder(list, "List");

        JScrollPane jsp = new JScrollPane(list);
        jsp.setVerticalScrollBar(jsp.createVerticalScrollBar());

        JButton add = new JButton("Add");
        add.addActionListener(actionListener -> {
            // TODO - acabar click event
            families.addName(name.getText().trim() + " " + surname.getText().trim());
        });

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File"), info = new JMenu("Info"), save = new JMenu("Save");
        JMenuItem[] fileItems = { new JMenuItem("Load"), save,
                                  new JMenuItem("Clear"), new JMenuItem("Exit") },
                    saveItems = { new JMenuItem("Names"), new JMenuItem("Families") },
                    infoItems = { new JMenuItem("Names"), new JMenuItem("Surnames"),
                                  new JMenuItem("Greater families"), new JMenuItem("Family"),
                                  new JMenuItem("Families") };



        ActionListener[] fileItemListeners = { this::load, null, this::clear, this::exit},
                         saveItemListeners = {this::saveNames, this::saveFamilies},
                         infoItemListeners = { /* TODO - implementar listeners */ };

        for (int i = 0; i < fileItems.length; i++) {
            file.add(fileItems[i]);
            if (i == 1) file.addSeparator();
            if (fileItemListeners[i] != null) fileItems[i].addActionListener(fileItemListeners[i]);
        }

        for(JMenuItem jmi : saveItems)
            save.add(jmi);

        for (JMenuItem jmi : infoItems)
            info.add(jmi);

        menu.add(file);
        menu.add(info);

        setJMenuBar(menu);

        JPanel pList = new JPanel(), pName = new JPanel();
        pList.setLayout(new GridLayout(2, 1));

        // TODO - acabar de modelar a janela

        setVisible(true);
    }

    public static void applyBorder(javax.swing.text.JTextComponent jtc, String title) {
        jtc.setBorder(BorderFactory.createTitledBorder(title));
    }


    // Eventos de fileItems

    private void load(ActionEvent ae) {
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            // TODO - acabar evento
        }
    }

    private void clear(ActionEvent actionEvent) {
        name.setText("");
        surname.setText("");
        list.setText("");
    }

    private void exit(ActionEvent actionEvent) {
        dispose();
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        //System.exit(0);
    }


    // Eventos de saveItems

    private void saveNames(ActionEvent actionEvent) {
        if (families.getSurnames().size() == 0) JOptionPane.showMessageDialog(null, "There are no stored families to save", "Warning", JOptionPane.WARNING_MESSAGE);
        else new SurnameChooserWindow(families, jfc);
    }

    private void saveFamilies(ActionEvent actionEvent) {
        // TODO - implementar evento
    }
}
