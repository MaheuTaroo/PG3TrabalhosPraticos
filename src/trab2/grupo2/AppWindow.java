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

        setSize(600, 400);
        setResizable(false);


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

        JMenuItem[] saveItems = {new JMenuItem("Names"), new JMenuItem("Families")};

        ActionListener[] saveItemListeners = {this::saveNames, this::saveFamilies};

        JMenu save = addActionListeners(saveItems, saveItemListeners, "Save");

        JMenuItem[] fileItems = { new JMenuItem("Load"), save,
                                  new JMenuItem("Clear"), new JMenuItem("Exit") },
                    infoItems = { new JMenuItem("Names"), new JMenuItem("Surnames"),
                                  new JMenuItem("Greater families"), new JMenuItem("Family"),
                                  new JMenuItem("Families") };

        ActionListener[] fileItemListeners = { this::load, null, this::clear, this::exit},
                         infoItemListeners = { /* TODO - implementar listeners */ };


        JMenu file = addActionListeners(fileItems, fileItemListeners, "File"),
              info = addActionListeners(infoItems, infoItemListeners, "Info");

        menu.add(file);
        menu.add(info);

        setJMenuBar(menu);

        JPanel pName = new JPanel();

        pName.setLayout(new GridLayout(1, 3));
        pName.add(name);
        pName.add(surname);
        pName.add(add);

        Container c = getContentPane();

        c.add(list, BorderLayout.CENTER);
        c.add(pName, BorderLayout.SOUTH);

        // TODO - acabar de modelar a janela

        setVisible(true);
    }

    private static JMenu addActionListeners(JMenuItem[] items, ActionListener[] listeners, String text) {
        JMenu menu = new JMenu(text);
        for (int i = 0; i < items.length; i++) {
            menu.add(items[i]);
            if (listeners[i] != null) items[i].addActionListener(listeners[i]);
        }

        return menu;
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
