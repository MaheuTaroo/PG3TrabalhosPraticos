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
    public static void applyBorder(javax.swing.text.JTextComponent jtc, String title) {
        jtc.setBorder(BorderFactory.createTitledBorder(title));
    }

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
                              new JMenuItem("Families"), new JMenuItem("Greater families"),
                              new JMenuItem("Family"), new JMenuItem("Families") };



        ActionListener[] itemListeners = { this::load, null, this::clear, this::exit};

        for (int i = 0; i < fileItems.length; i++) {
            file.add(fileItems[i]);
             if (itemListeners[i] != null) fileItems[i].addActionListener(itemListeners[i]);
        }

        for(JMenuItem jmi : saveItems)
            save.add(jmi);

        for (JMenuItem jmi : infoItems)
            info.add(jmi);

        setJMenuBar(menu);

        JPanel pList = new JPanel(), pName = new JPanel();
        pList.setLayout(new GridLayout(2, 1));

        // TODO - acabar de modelar a janela
    }

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
        System.exit(0);
    }
}
