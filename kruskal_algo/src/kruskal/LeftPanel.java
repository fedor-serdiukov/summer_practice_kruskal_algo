package kruskal;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/** Левая панель первой версии. */
public class LeftPanel extends JPanel {
    private static final String[] TOGGLE_VALUES = {"Включено", "Выключено"};

    public LeftPanel(GraphPanel graphPanel) {
        configurePanel(220);

        JPanel content = createContentPanel("Настройки");

        addSetting(content, 0, "Веса рёбер", true,
                graphPanel::setShowWeights);

        addSetting(content, 1, "Отрицательные веса", true,
                graphPanel::setAllowNegativeWeights);

        add(content, BorderLayout.NORTH);
        add(createHint("Настройки отображения и построения графа."),
                BorderLayout.SOUTH);
    }

    private void configurePanel(int width) {
        setLayout(new BorderLayout());
        setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, new Color(215, 215, 215)),
                new EmptyBorder(14, 12, 14, 12)
        ));
        setPreferredSize(new Dimension(width, 0));
    }

    private JPanel createContentPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());

        TitledBorder border = new TitledBorder(
                new LineBorder(new Color(205, 205, 205)),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP
        );

        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));

        panel.setBorder(border);
        return panel;
    }

    private void addSetting(JPanel panel, int row, String labelText,
                            boolean initialValue,
                            java.util.function.Consumer<Boolean> action) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(row == 0 ? 10 : 14, 10, 10, 10);

        JCheckBox checkBox = new JCheckBox(labelText, initialValue);
        checkBox.addActionListener(e -> action.accept(checkBox.isSelected()));

        panel.add(checkBox, gbc);
    }

    private JComponent createHint(String text) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setOpaque(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(getFont().deriveFont(Font.PLAIN, 11f));
        area.setForeground(new Color(95, 95, 95));
        area.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                new EmptyBorder(10, 2, 0, 2)
        ));
        return area;
    }
}
