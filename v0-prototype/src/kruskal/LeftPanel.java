package kruskal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Левая панель настроек прототипа.
 */
public class LeftPanel extends JPanel {

    public LeftPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(12, 12, 12, 12));
        setPreferredSize(new Dimension(190, 0));

        addSectionTitle("Настройки");

        addLabel("Вес рёбер:");
        JComboBox<String> weightModeBox = new JComboBox<>(new String[]{
                "Включены"
        });
        configureControl(weightModeBox);
        weightModeBox.setEnabled(false);
        weightModeBox.setToolTipText("Настройка будет доступна в следующих версиях");
        add(weightModeBox);

        addVerticalStrut(10);

        addLabel("Допустимые веса:");
        JComboBox<String> weightDirectionBox = new JComboBox<>(new String[]{
                "Положительные"
        });
        configureControl(weightDirectionBox);
        weightDirectionBox.setEnabled(false);
        weightDirectionBox.setToolTipText("Настройка будет доступна в следующих версиях");
        add(weightDirectionBox);

        addVerticalStrut(10);

        JCheckBox showWeightsBox = new JCheckBox("Показывать веса", true);
        showWeightsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        showWeightsBox.setEnabled(false);
        showWeightsBox.setToolTipText("Настройка будет доступна в следующих версиях");
        add(showWeightsBox);

        add(Box.createVerticalGlue());
        addSeparator();

        JTextArea hintArea = new JTextArea(
                "Параметры отображения графа будут доступны в следующих версиях."
        );
        hintArea.setEditable(false);
        hintArea.setOpaque(false);
        hintArea.setFocusable(false);
        hintArea.setLineWrap(true);
        hintArea.setWrapStyleWord(true);
        hintArea.setFont(getFont().deriveFont(Font.PLAIN, 11f));
        hintArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        add(hintArea);
    }

    private void addSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        addVerticalStrut(10);
    }

    private void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        addVerticalStrut(4);
    }

    private void configureControl(JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    }

    private void addSeparator() {
        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(separator);
        addVerticalStrut(10);
    }

    private void addVerticalStrut(int height) {
        add(Box.createVerticalStrut(height));
    }
}
