package kruskal;

import javax.swing.*;
import java.awt.*;

/**
 * Главное окно
 */
public class MainFrame extends JFrame {

    private final Graph graph = new Graph();

    public MainFrame() {
        super("Визуализатор алгоритма Краскала — прототип");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 760);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);

        buildDemoGraph();

        GraphPanel graphPanel = new GraphPanel(graph);
        InfoPanel infoPanel = new InfoPanel();
        LogPanel logPanel = new LogPanel();
        logPanel.setPlaceholder("Журнал работы алгоритма будет вестись начиная с 1-й версии.");

        add(buildToolBar(), BorderLayout.NORTH);
        add(new LeftPanel(), BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);
    }

    /** Демонстрационный граф для показа отображения вершин, рёбер и весов. */
    private void buildDemoGraph() {
        Vertex v1 = graph.addVertex(300, 160);
        Vertex v2 = graph.addVertex(560, 130);
        Vertex v3 = graph.addVertex(700, 320);
        Vertex v4 = graph.addVertex(480, 430);
        Vertex v5 = graph.addVertex(250, 360);
        graph.addEdge(v1, v2, 4);
        graph.addEdge(v2, v3, 5);
        graph.addEdge(v3, v4, 2);
        graph.addEdge(v4, v5, 7);
        graph.addEdge(v5, v1, 3);
        graph.addEdge(v1, v4, 6);
        graph.addEdge(v2, v4, 8);
    }

    /** Панель управления */
    private JToolBar buildToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        JButton newButton = new JButton("Создать граф");
        JButton loadButton = new JButton("Загрузить");
        JButton saveButton = new JButton("Сохранить");
        JButton clearButton = new JButton("Очистить");

        newButton.addActionListener(e -> notYet("Создание графа", 1));
        loadButton.addActionListener(e -> notYet("Загрузка из файла", 1));
        saveButton.addActionListener(e -> notYet("Сохранение в файл", 1));
        clearButton.addActionListener(e -> notYet("Очистка рабочего поля", 1));

        JComboBox<String> modeBox = new JComboBox<>(new String[]{
                "Режим: добавление вершин",
                "Режим: добавление рёбер",
                "Режим: перемещение",
                "Режим: удаление"
        });
        modeBox.setToolTipText("Режимы редактирования будут реализованы в 1-й и 2-й версиях");
        modeBox.setMaximumSize(modeBox.getPreferredSize());

        JButton runButton = new JButton("Запустить алгоритм");
        JButton prevButton = new JButton("Предыдущий шаг");
        JButton nextButton = new JButton("Следующий шаг");
        JToggleButton autoButton = new JToggleButton("Автовыполнение");
        JButton resetButton = new JButton("Сброс");
        JSlider speedSlider = new JSlider(100, 2000, 900);

        runButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        autoButton.setEnabled(false);
        resetButton.setEnabled(false);
        speedSlider.setEnabled(false);
        runButton.setToolTipText("Алгоритм будет реализован в 1-й версии");
        prevButton.setToolTipText("Пошаговое выполнение — во 2-й версии");
        nextButton.setToolTipText("Пошаговое выполнение — во 2-й версии");
        autoButton.setToolTipText("Автовыполнение — во 2-й версии");
        resetButton.setToolTipText("Сброс визуализации — в 1-й версии");
        speedSlider.setToolTipText("Регулятор скорости автовыполнения — во 2-й версии");
        speedSlider.setMaximumSize(new Dimension(150, 28));
        speedSlider.setPreferredSize(new Dimension(150, 28));

        bar.add(newButton);
        bar.add(loadButton);
        bar.add(saveButton);
        bar.add(clearButton);
        bar.addSeparator();
        bar.add(modeBox);
        bar.addSeparator();
        bar.add(runButton);
        bar.add(prevButton);
        bar.add(nextButton);
        bar.add(autoButton);
        bar.add(new JLabel("  Задержка, мс: "));
        bar.add(speedSlider);
        bar.add(resetButton);
        return bar;
    }

    private void notYet(String what, int version) {
        JOptionPane.showMessageDialog(this,
                what + " будет реализовано в " + version + "-й версии.",
                "Прототип", JOptionPane.INFORMATION_MESSAGE);
    }
}
