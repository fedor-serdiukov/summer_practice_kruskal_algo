package kruskal;

import javax.swing.*;
import java.awt.*;

/**
 * Журнал работы алгоритма
 * текстовая область, в которую построчно добавляются описания шагов
 */
public class LogPanel extends JPanel {

    private final JTextArea area = new JTextArea(7, 20);

    public LogPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Журнал работы алгоритма"));
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    public void append(String line) {
        area.append(line + System.lineSeparator());
        area.setCaretPosition(area.getDocument().getLength());
    }

    public void clear() {
        area.setText("");
    }

    /** Текст-заглушка */
    public void setPlaceholder(String text) {
        area.setText(text + System.lineSeparator());
    }
}
