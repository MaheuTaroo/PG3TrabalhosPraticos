package trab2.grupo2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class AppWindow extends JFrame {
    private final JTextField name, surname;
    private final JTextArea list;
    private Families<Collection<String>> families;
    JFileChooser jfc = new JFileChooser(".");

    public AppWindow() {
        super("Families");
        families = new Families<>(LinkedHashMap::new, TreeSet::new);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(600, 400);
        setResizable(false);

        jfc.setAcceptAllFileFilterUsed(false);

        name = new JTextField();
        applyBorder(name, "Name");

        surname = new JTextField();
        applyBorder(surname, "Surname");

        list = new JTextArea();
        list.setEditable(false);
        applyBorder(list, "List");

        JScrollPane jsp = new JScrollPane(list);
        jsp.setVerticalScrollBar(jsp.createVerticalScrollBar());
        jsp.setHorizontalScrollBar(jsp.createHorizontalScrollBar());

        JButton add = new JButton("Add");
        add.addActionListener(ae -> {
            if (name.getText().isEmpty() || surname.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "There is not enough data to add a family member. Please check the name and/or surname boxes and try again", "Error", JOptionPane.ERROR_MESSAGE);
            else
                families.addName(name.getText().trim() + " " + surname.getText().trim());
        });

        JMenuBar menu = new JMenuBar();

        JMenuItem[] saveItems = { new JMenuItem("Names"), new JMenuItem("Surnames"), new JMenuItem("Family"), new JMenuItem("Families") };

        ActionListener[] saveItemListeners = { this::saveNames, this::saveSurnames, this::saveFamily, this::saveFamilies };

        JMenu save = addActionListeners(saveItems, saveItemListeners, "Save");

        JMenuItem[] fileItems = { new JMenuItem("Load"), save,
                                  new JMenuItem("Clear"), new JMenuItem("Exit") },
                    infoItems = { new JMenuItem("Names"), new JMenuItem("Surnames"),
                                  new JMenuItem("Greater families"), new JMenuItem("Family"),
                                  new JMenuItem("Families") };

        ActionListener[] fileItemListeners = { this::load, null, this::clear, this::exit},
                infoItemListeners = { this::fetchNames, this::fetchSurnames, this::fetchGreaterFamilies, this::fetchFamily, this::fetchFamilies };

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

        c.add(jsp, BorderLayout.CENTER);
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
        if (families.getSurnames().size() == 0)
            JOptionPane.showMessageDialog(null, "No families have been stored yet", "Warning", JOptionPane.WARNING_MESSAGE);

        return families.getSurnames().size() != 0;
    }


    // Eventos de fileItems

    private void load(ActionEvent ae) {
        FileNameExtensionFilter namesFilter = new FileNameExtensionFilter("Family names file", "nam");
        jfc.addChoosableFileFilter(namesFilter);
        try {
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                families.addNames(jfc.getSelectedFile());
        }
        catch (IOException io) {
            JOptionPane.showMessageDialog(null, "There was an error loading the given family file: " + io.getLocalizedMessage());
        }
        jfc.removeChoosableFileFilter(namesFilter);
    }

    private void clear(ActionEvent actionEvent) {
        name.setText("");
        surname.setText("");
        list.setText("");
        families = new Families<>(LinkedHashMap::new, TreeSet::new);
    }

    private void exit(ActionEvent actionEvent) {
        dispose();
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }


    // Eventos de saveItems

    private void saveNames(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            FileNameExtensionFilter namesFilter = new FileNameExtensionFilter("Family names file", "nam");
            jfc.addChoosableFileFilter(namesFilter);
            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = jfc.getSelectedFile().getAbsolutePath();
                if (!filename.endsWith(".nam")) filename += ".nam";
                try (PrintWriter pw = new PrintWriter(filename)) {
                    families.forEachName(pw::println);
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred while saving the names: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            jfc.removeChoosableFileFilter(namesFilter);
        }
    }

    private void saveSurnames(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            FileNameExtensionFilter namesFilter = new FileNameExtensionFilter("Family names file", "snam");
            jfc.addChoosableFileFilter(namesFilter);
            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = jfc.getSelectedFile().getAbsolutePath();
                if (!filename.endsWith(".snam")) filename += ".snam";
                try (PrintWriter pw = new PrintWriter(filename)) {
                    families.forEach((surname, name) -> pw.println(surname));
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred while saving the surnames: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            jfc.removeChoosableFileFilter(namesFilter);
        }
    }

    private void saveFamily(ActionEvent actionEvent) {
        if (checkForFamilies()) new SaveFamilyDialog(this, families, jfc);
    }

    private void saveFamilies(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            FileNameExtensionFilter familiesFilter = new FileNameExtensionFilter("Families file", "fam");
            jfc.addChoosableFileFilter(familiesFilter);
            if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String filename = jfc.getSelectedFile().getAbsolutePath();
                if (!filename.endsWith(".fam")) filename += ".fam";
                try (PrintWriter pw = new PrintWriter(filename)) {
                    families.printFamilies(pw, Collections.emptySet());
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occurred while saving the family: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            jfc.removeChoosableFileFilter(familiesFilter);
        }
    }


    // Eventos de infoItems

    private void fetchNames(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            list.setText("");

            families.forEachName(s -> list.append((list.getText().isEmpty() ? "" : "\n") + s));
        }
    }

    private void fetchSurnames(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            list.setText("");

            for (String surname : families.getSurnames()) {
                list.append((list.getText().isEmpty() ? "" : "\n") + surname);
            }
        }
    }

    private void fetchGreaterFamilies(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            list.setText("");

            for (String surname: families.getGreaterFamilies()) {
                list.append((list.getText().isEmpty() ? "" : "\n") + surname);

                for (String name: families.getNames(surname))
                    list.append("\n      " + name);
            }
        }
    }

    private void fetchFamily(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            list.setText("");

            // Array passada como referência para obter o valor escolhido
            String[] returningFamily = new String[] { null };

            new FamilyChooserDialog(this, families, returningFamily);
            if (returningFamily[0] != null) {
                list.setText(returningFamily[0]);

                for (String name : families.getNames(returningFamily[0])) {
                    list.append("\n\t" + name);
                }
            }
        }
    }

    private void fetchFamilies(ActionEvent actionEvent) {
        if (checkForFamilies()) {
            list.setText("");

            StringWriter sw = new StringWriter();
            families.printFamilies(new PrintWriter(sw), Collections.emptySet());

            list.setText(sw.toString());
        }
    }
}