package trab2.grupo2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class AppWindow extends JFrame {
    private final JTextField name, surname;
    private final JTextArea list;
    private final Families<Collection<String>> families;
    private final TreeMap<String, Collection<String>> map = new TreeMap<>();
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
        add.addActionListener(ae -> families.addName(name.getText().trim() + " " + surname.getText().trim()));

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
                         infoItemListeners = { this::fetchNames, this::fetchSurnames, this::fetchGreaterFamilies, null, null };
        // TODO - implementar listeners


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

    private boolean checkForFamilies() {
        if (families.getSurnames().size() == 0) JOptionPane.showMessageDialog(null, "There are no stored families to save", "Warning", JOptionPane.WARNING_MESSAGE);

        return families.getSurnames().size() != 0;
    }


    // Eventos de fileItems

    private void load(ActionEvent ae) {
        try {
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                families.addNames(jfc.getSelectedFile());
        }
        catch (IOException io) {
            JOptionPane.showMessageDialog(null, "There was an error loading the given family file: " + io.getLocalizedMessage());
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
    }


    // Eventos de saveItems

    private void saveNames(ActionEvent actionEvent) {
        if (checkForFamilies()) new SaveFamilyDialog(this, families, jfc);
    }

    private void saveFamilies(ActionEvent actionEvent) {
        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(jfc.getSelectedFile())) {
                for (String surname: families.getSurnames()) {
                    for (String name : families.getNames(surname)) {
                        fw.write("\n\t" + name);
                    }
                }

            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "An error occurred while saving the names: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // Eventos de infoItems

    private void fetchSurnames(ActionEvent actionEvent) {
        list.setText("");
        for (String surname : families.getSurnames()) {
            list.append((list.getText().isEmpty() ? "" : "\n") + surname);
        }
    }

    private void fetchNames(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            list.setText("");
            for (String surname : families.getSurnames()) {
                list.append((list.getText().isEmpty() ? "" : "\n") + surname);
                for (String name : families.getNames(surname))
                    list.append("\n" + name);
            }
        }
    }

    private void fetchGreaterFamilies(ActionEvent actionEvent) {
        list.setText("");

        for (String surname: families.getGreaterFamilies()) {
            list.append((list.getText().isEmpty() ? "" : "\n") + name);

            for (String name: families.getNames(surname))
                list.append("\n\t" + name);
        }
    }
}