package trab2.grupo1;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class AppWindow extends JFrame implements ActionListener {

    private final JTextField filename = new JTextField();
    private final JTextArea expressions = createWithBorder("Expressions"), results = createWithBorder("Results");

    private static JTextArea createWithBorder(String title) {
        JTextArea jta = new JTextArea();

        jta.setBorder(BorderFactory.createTitledBorder(title));

        return jta;
    }

    public AppWindow() {
        super("Expressions");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(525, 525);
        setMinimumSize(new Dimension(525, 525));

        filename.setBorder(BorderFactory.createTitledBorder("File name"));

        expressions.setEditable(false);
        results.setEditable(false);

        JButton evaluate = new JButton("Evaluate");
        evaluate.addActionListener(this);

        Container c = getContentPane();

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 2));
        p.add(expressions);
        p.add(results);

        c.add(filename, BorderLayout.NORTH);
        c.add(evaluate, BorderLayout.SOUTH);
        c.add(p, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        expressions.setText("");
        results.setText("");

        try {
            StreamUtils.evaluate(filename.getText(), (s, i) -> {
                expressions.append(s + "\n");
                results.append(s + (i == null ? " ERROR" : i) + "\n");
            });
        }
        catch (IOException ioe) {
            results.append("ERROR: " + ioe.getMessage());
        }
    }
}
