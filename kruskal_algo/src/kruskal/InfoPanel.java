package kruskal;

import javax.swing.*;
import java.awt.*;

/**
 * Панель информации
 */
public class InfoPanel extends JPanel {

    private final JLabel stepLabel = new JLabel();
    private final JLabel edgeLabel = new JLabel();
    private final JLabel weightLabel = new JLabel();
    private final JLabel mstWeightLabel = new JLabel();
    private final JLabel chosenLabel = new JLabel();
    private final JTextArea actionArea = new JTextArea(4, 20);

    public InfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Информация"),
                BorderFactory.createEmptyBorder(4, 8, 8, 8)));
        setPreferredSize(new Dimension(260, 0));

        actionArea.setLineWrap(true);
        actionArea.setWrapStyleWord(true);
        actionArea.setEditable(false);
        actionArea.setFocusable(false);
        actionArea.setOpaque(false);
        actionArea.setFont(stepLabel.getFont());

        addRow(stepLabel);
        addRow(edgeLabel);
        addRow(weightLabel);
        addRow(new JLabel("Действие:"));
        actionArea.setAlignmentX(LEFT_ALIGNMENT);
        add(actionArea);
        add(Box.createVerticalStrut(8));
        addRow(mstWeightLabel);
        addRow(chosenLabel);
        add(Box.createVerticalGlue());

        showIdle();
    }

    private void addRow(JComponent c) {
        c.setAlignmentX(LEFT_ALIGNMENT);
        add(c);
        add(Box.createVerticalStrut(6));
    }

    /** Состояние до запуска алгоритма  */
    public void showIdle() {
        stepLabel.setText("Шаг: —");
        edgeLabel.setText("Ребро: —");
        weightLabel.setText("Вес ребра: —");
        actionArea.setText("Алгоритм не запущен. Постройте граф и нажмите «Запустить алгоритм».");
        mstWeightLabel.setText("Вес остова: 0");
        chosenLabel.setText("Выбрано рёбер: 0");
    }

    /** Состояние сразу после запуска  */
    public void showStart(int total) {
        stepLabel.setText("Шаг: 0 из " + total);
        edgeLabel.setText("Ребро: —");
        weightLabel.setText("Вес ребра: —");
        actionArea.setText("Рёбра отсортированы по возрастанию веса. "
                + "Нажимайте «Следующий шаг» или включите автовыполнение.");
        mstWeightLabel.setText("Вес остова: 0");
        chosenLabel.setText("Выбрано рёбер: 0");
    }

    /** Отображение конкретного шага алгоритма */
    public void showStep(KruskalStep step, int number, int total) {
        stepLabel.setText("Шаг: " + number + " из " + total);
        Edge e = step.getEdge();
        if (e != null) {
            edgeLabel.setText("Ребро: " + e.getU() + " — " + e.getV());
            weightLabel.setText("Вес ребра: " + e.getWeight());
        } else {
            edgeLabel.setText("Ребро: —");
            weightLabel.setText("Вес ребра: —");
        }
        actionArea.setText(step.getDescription());
        mstWeightLabel.setText("Вес остова: " + step.getMstWeight());
        chosenLabel.setText("Выбрано рёбер: " + step.getChosenCount());
    }
}
