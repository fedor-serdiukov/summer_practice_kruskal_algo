package kruskal;

import javax.swing.*;
import java.awt.*;

/**
 * Панель информации (прототип)
 */
public class InfoPanel extends JPanel {

    public InfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Информация"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        setPreferredSize(new Dimension(260, 0));

        addRow(new JLabel("Шаг: —"));
        addRow(new JLabel("Ребро: —"));
        addRow(new JLabel("Вес ребра: —"));
        addRow(new JLabel("Действие:"));

        JTextArea actionArea = new JTextArea(
                "Алгоритм не запущен. Постройте граф и нажмите «Запустить алгоритм».", 4, 20);
        actionArea.setLineWrap(true);
        actionArea.setWrapStyleWord(true);
        actionArea.setEditable(false);
        actionArea.setFocusable(false);
        actionArea.setOpaque(false);
        actionArea.setFont(getFont());
        actionArea.setAlignmentX(LEFT_ALIGNMENT);
        add(actionArea);
        add(Box.createVerticalStrut(8));

        addRow(new JLabel("Вес остова: 0"));
        addRow(new JLabel("Выбрано рёбер: 0"));
        add(Box.createVerticalGlue());
    }

    private void addRow(JComponent c) {
        c.setAlignmentX(LEFT_ALIGNMENT);
        add(c);
        add(Box.createVerticalStrut(6));
    }
}
