package kruskal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Рабочая область (прототип): отображает граф — вершины кругами с номерами,
 * рёбра линиями с подписями весов
 */
public class GraphPanel extends JPanel {

    public enum Mode { ADD_VERTEX, ADD_EDGE, DELETE }

    private static final int VERTEX_RADIUS = 18;

    private static final Color COLOR_VERTEX = new Color(70, 130, 220);
    private static final Color COLOR_VERTEX_BORDER = new Color(25, 60, 120);
    private static final Color COLOR_VERTEX_SELECTED = new Color(255, 160, 40);
    private static final Color COLOR_EDGE_DEFAULT = new Color(120, 120, 120);
    private static final Color COLOR_EDGE_ACCEPTED = new Color(30, 160, 60);
    private static final Color COLOR_WEIGHT_BG = new Color(255, 255, 255, 220);

    private final Graph graph;
    private Mode mode = Mode.ADD_VERTEX;

    private Vertex selectedVertex;   // первая вершина при создании ребра

    private boolean algorithmMode = false;
    private boolean allowNegativeWeights = true;
    private boolean showWeights = true;
    private final Set<Edge> resultEdges = new HashSet<>(); // рёбра итогового остова

    /** Слушатель изменений графа (для обновления состояния кнопок и т.п.). */
    private Runnable changeListener = () -> { };

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (algorithmMode) return;
                Point2D p = new Point2D.Double(e.getX(), e.getY());

                if (e.getClickCount() == 2) {
                    Edge edge = findEdgeAt(p);
                    if (edge != null) {
                        changeWeight(edge);
                        return;
                    }
                }
                if (!SwingUtilities.isLeftMouseButton(e)) return;

                Vertex v = findVertexAt(p);
                switch (mode) {
                    case ADD_VERTEX -> {
                        if (v == null && findEdgeAt(p) == null) {
                            graph.addVertex(p.getX(), p.getY());
                            fireChange();
                        }
                    }
                    case ADD_EDGE -> handleAddEdgeClick(v);
                    case DELETE -> {
                        if (v != null) {
                            graph.removeVertex(v);
                            fireChange();
                        } else {
                            Edge edge = findEdgeAt(p);
                            if (edge != null) {
                                graph.removeEdge(edge);
                                fireChange();
                            }
                        }
                    }
                }
                repaint();
            }
        };
        addMouseListener(mouse);
    }

    // ---------- режимы и внешние команды ----------

    public void setMode(Mode mode) {
        this.mode = mode;
        selectedVertex = null;
        repaint();
    }

    public void setShowWeights(boolean showWeights) {
        this.showWeights = showWeights;
        repaint();
    }

    public void setChangeListener(Runnable listener) {
        this.changeListener = listener;
    }

    private void fireChange() {
        changeListener.run();
    }

    /** Включает режим отображения результата (редактирование блокируется). */
    public void setAlgorithmMode(boolean on) {
        algorithmMode = on;
        selectedVertex = null;
        if (!on) {
            resultEdges.clear();
        }
        repaint();
    }

    /** Подсвечивает зелёным рёбра итогового остова. */
    public void showResult(List<Edge> mstEdges) {
        resultEdges.clear();
        resultEdges.addAll(mstEdges);
        repaint();
    }

    // ---------- редактирование ----------

    private void handleAddEdgeClick(Vertex v) {
        if (v == null) {
            selectedVertex = null;
            return;
        }

        if (selectedVertex == null) {
            selectedVertex = v;

        } else if (selectedVertex != v) {

            Edge existingEdge = graph.findEdge(selectedVertex, v);

            if (existingEdge != null) {
                changeWeight(existingEdge);

            } else {
                Integer w = askWeight(
                        "Вес ребра " + selectedVertex + " — " + v + ":",
                        1
                );

                if (w != null) {
                    if (!allowNegativeWeights && w < 0) {
                        JOptionPane.showMessageDialog(this,
                                "Отрицательные веса рёбер запрещены.",
                                "Ошибка",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    graph.addEdge(selectedVertex, v, w);
                    fireChange();
                }
            }

            selectedVertex = null;

        } else {
            selectedVertex = null;
        }
    }

    private void changeWeight(Edge edge) {
        Integer w = askWeight(
                "Новый вес ребра " + edge.getU() + " — " + edge.getV() + ":",
                edge.getWeight()
        );

        if (w != null) {
            if (!allowNegativeWeights && w < 0) {
                JOptionPane.showMessageDialog(this,
                        "Отрицательные веса рёбер запрещены.",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            edge.setWeight(w);
            fireChange();
            repaint();
        }
    }

    /** Диалог ввода веса; возвращает null при отмене. */
    private Integer askWeight(String message, int initial) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, message, initial);
            if (s == null) return null;
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Вес должен быть целым числом.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ---------- поиск элементов под курсором ----------

    private Vertex findVertexAt(Point2D p) {
        List<Vertex> vs = graph.getVertices();
        for (int i = vs.size() - 1; i >= 0; i--) {
            Vertex v = vs.get(i);
            if (p.distance(v.getX(), v.getY()) <= VERTEX_RADIUS) return v;
        }
        return null;
    }

    private Edge findEdgeAt(Point2D p) {
        for (Edge e : graph.getEdges()) {
            double d = Line2D.ptSegDist(
                    e.getU().getX(), e.getU().getY(),
                    e.getV().getX(), e.getV().getY(),
                    p.getX(), p.getY());
            if (d <= 6) return e;
        }
        return null;
    }

    // ---------- отрисовка ----------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (Edge e : graph.getEdges()) {
            drawEdge(g2, e);
        }
        for (Vertex v : graph.getVertices()) {
            drawVertex(g2, v);
        }
        g2.dispose();

        // Подсказка в углу.
        Graphics2D hg = (Graphics2D) g.create();
        hg.setColor(new Color(90, 90, 90));
        hg.setFont(getFont().deriveFont(Font.PLAIN, 11f));
        String hint = algorithmMode
                ? "Показан результат алгоритма — редактирование заблокировано (нажмите «Сброс»)"
                : hintForMode();
        hg.drawString(hint, 8, getHeight() - 8);
        hg.dispose();
    }

    private String hintForMode() {
        return switch (mode) {
            case ADD_VERTEX -> "Щелчок — добавить вершину. Двойной щелчок по ребру — изменить вес.";
            case ADD_EDGE -> "Щёлкните первую вершину, затем вторую. Двойной щелчок по ребру — изменить вес.";
            case DELETE -> "Щелчок по вершине или ребру — удаление.";
        };
    }

    private void drawEdge(Graphics2D g2, Edge e) {
        boolean accepted = resultEdges.contains(e);
        g2.setColor(accepted ? COLOR_EDGE_ACCEPTED : COLOR_EDGE_DEFAULT);
        g2.setStroke(new BasicStroke(accepted ? 4f : 2f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(e.getU().getX(), e.getU().getY(),
                e.getV().getX(), e.getV().getY()));

        if (showWeights) {
            double mx = (e.getU().getX() + e.getV().getX()) / 2;
            double my = (e.getU().getY() + e.getV().getY()) / 2;
            String w = String.valueOf(e.getWeight());
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(w), th = fm.getAscent();
            g2.setColor(COLOR_WEIGHT_BG);
            g2.fillRoundRect((int) (mx - tw / 2.0 - 4), (int) (my - th / 2.0 - 3),
                    tw + 8, th + 6, 8, 8);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(w, (int) (mx - tw / 2.0), (int) (my + th / 2.0 - 1));
        }
    }

    public void setAllowNegativeWeights(boolean allowNegativeWeights) {
        this.allowNegativeWeights = allowNegativeWeights;

    }

    private void drawVertex(Graphics2D g2, Vertex v) {
        int x = (int) v.getX(), y = (int) v.getY(), r = VERTEX_RADIUS;
        g2.setColor(v == selectedVertex ? COLOR_VERTEX_SELECTED : COLOR_VERTEX);
        g2.fillOval(x - r, y - r, 2 * r, 2 * r);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(COLOR_VERTEX_BORDER);
        g2.drawOval(x - r, y - r, 2 * r, 2 * r);

        String label = String.valueOf(v.getId());
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.WHITE);
        g2.drawString(label, x - fm.stringWidth(label) / 2, y + fm.getAscent() / 2 - 1);
    }
}
