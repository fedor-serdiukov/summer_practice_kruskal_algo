package kruskal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Главное окно
 */
public class MainFrame extends JFrame {

    private final Graph graph = new Graph();
    private final GraphPanel graphPanel = new GraphPanel(graph);
    private final InfoPanel infoPanel = new InfoPanel();
    private final LogPanel logPanel = new LogPanel();

    private final JButton runButton = new JButton("Запустить алгоритм");
    private final JButton nextButton = new JButton("Следующий шаг");
    private final JButton prevButton = new JButton("Предыдущий шаг");
    private final JToggleButton autoButton = new JToggleButton("Автовыполнение");
    private final JButton resetButton = new JButton("Сброс");

    private final JComboBox<String> modeBox = new JComboBox<>(new String[]{
            "Режим: добавление вершин",
            "Режим: добавление рёбер",
            "Режим: удаление"
    });

    /** Шаги последнего запуска (используются для журнала и итога). */
    private List<KruskalStep> steps;

    public MainFrame() {
        super("Визуализатор алгоритма Краскала — версия 1");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 760);
        setMinimumSize(new Dimension(900, 560));
        setLocationRelativeTo(null);

        add(buildToolBar(), BorderLayout.NORTH);
        add(new LeftPanel(graphPanel), BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(logPanel, BorderLayout.SOUTH);

        logPanel.setPlaceholder("Журнал пуст. Постройте граф и запустите алгоритм.");
        graphPanel.setChangeListener(this::updateButtons);
        updateButtons();
    }

    //панель управления

    private JToolBar buildToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        JButton newButton = new JButton("Создать граф");
        JButton loadButton = new JButton("Загрузить");
        JButton saveButton = new JButton("Сохранить");
        JButton clearButton = new JButton("Очистить");

        newButton.addActionListener(e -> newGraph());
        loadButton.addActionListener(e -> loadGraph());
        saveButton.addActionListener(e -> saveGraph());
        clearButton.addActionListener(e -> clearGraph());
        runButton.addActionListener(e -> runAlgorithm());
        resetButton.addActionListener(e -> resetVisualization());

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        autoButton.setEnabled(false);
        String soon = "Будет реализовано во 2-й версии";
        nextButton.setToolTipText(soon);
        prevButton.setToolTipText(soon);
        autoButton.setToolTipText(soon);

        modeBox.addActionListener(e -> graphPanel.setMode(
                GraphPanel.Mode.values()[modeBox.getSelectedIndex()]));
        modeBox.setMaximumSize(modeBox.getPreferredSize());

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
        bar.add(resetButton);
        return bar;
    }

    // работа с графом

    private void newGraph() {
        if (graph.getVertices().isEmpty() || confirm("Создать новый граф? Текущий граф будет удалён.")) {
            resetVisualization();
            graph.clear();
            graphPanel.repaint();
            updateButtons();
        }
    }

    private void clearGraph() {
        if (graph.getVertices().isEmpty() || confirm("Очистить рабочее поле?")) {
            resetVisualization();
            graph.clear();
            graphPanel.repaint();
            updateButtons();
        }
    }

    private boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Подтверждение",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void loadGraph() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try {
            resetVisualization();
            graph.loadFromFile(chooser.getSelectedFile(),
                    graphPanel.getWidth(), graphPanel.getHeight());
            graphPanel.repaint();
            updateButtons();
        } catch (IOException | RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Не удалось загрузить граф.\n" + ex.getMessage(),
                    "Ошибка загрузки", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveGraph() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().contains(".")) {
            file = new File(file.getParentFile(), file.getName() + ".txt");
        }
        try {
            graph.saveToFile(file);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Не удалось сохранить граф.\n" + ex.getMessage(),
                    "Ошибка сохранения", JOptionPane.ERROR_MESSAGE);
        }
    }

    // выполнение алгоритма (сразу до результата)

    private void runAlgorithm() {
        if (graph.getVertices().size() < 2) {
            JOptionPane.showMessageDialog(this,
                    "Добавьте хотя бы две вершины.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (graph.getEdges().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "В графе нет рёбер.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }

        steps = KruskalAlgorithm.run(graph);
        graphPanel.setAlgorithmMode(true);

        // Журнал работы: рассмотренное ребро, вес и принятое решение
        logPanel.clear();
        logPanel.append("Алгоритм запущен: " + graph.getVertices().size() + " вершин, "
                + graph.getEdges().size() + " рёбер. Рёбра отсортированы по возрастанию веса.");
        List<Edge> accepted = new ArrayList<>();
        for (KruskalStep s : steps) {
            switch (s.getType()) {
                case ACCEPT -> {
                    accepted.add(s.getEdge());
                    logPanel.append("Ребро " + s.getEdge()
                            + ": добавлено в остов. Вес остова: " + s.getMstWeight() + ".");
                }
                case REJECT -> logPanel.append("Ребро " + s.getEdge()
                        + ": отклонено — образует цикл.");
                case FINISH -> logPanel.append(s.getDescription());
                case CONSIDER -> { /* решение записывается строкой вердикта */ }
            }
        }

        // Итог: зелёная подсветка остова и сводка на панели информации
        graphPanel.showResult(accepted);
        KruskalStep last = steps.get(steps.size() - 1);
        infoPanel.showStep(last, steps.size(), steps.size());
        JOptionPane.showMessageDialog(this, last.getDescription(),
                "Результат", JOptionPane.INFORMATION_MESSAGE);
        updateButtons();
    }

    private void resetVisualization() {
        steps = null;
        graphPanel.setAlgorithmMode(false);
        infoPanel.showIdle();
        logPanel.setPlaceholder("Журнал пуст. Постройте граф и запустите алгоритм.");
        updateButtons();
    }

    /** Обновляет доступность кнопок в зависимости от состояния */
    private void updateButtons() {
        boolean running = steps != null;
        modeBox.setEnabled(!running);
        runButton.setEnabled(!running);
        resetButton.setEnabled(running);
    }
}
