package kruskal;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Рабочая область (прототип): отображает граф — вершины кругами с номерами,
 * рёбра линиями с подписями весов
 */
public class GraphPanel extends JPanel {

    private static final int VERTEX_RADIUS = 18;

    private static final Color COLOR_VERTEX = new Color(70, 130, 220);
    private static final Color COLOR_VERTEX_BORDER = new Color(25, 60, 120);
    private static final Color COLOR_EDGE = new Color(120, 120, 120);
    private static final Color COLOR_WEIGHT_BG = new Color(255, 255, 255, 220);

    private final Graph graph;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);
    }

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

        Graphics2D hg = (Graphics2D) g.create();
        hg.setColor(new Color(90, 90, 90));
        hg.setFont(getFont().deriveFont(Font.PLAIN, 11f));
        hg.drawString("Прототип: показан демонстрационный граф; "
                + "редактирование мышью появится в 1-й версии.", 8, getHeight() - 8);
        hg.dispose();
    }

    private void drawEdge(Graphics2D g2, Edge e) {
        g2.setColor(COLOR_EDGE);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(e.getU().getX(), e.getU().getY(),
                e.getV().getX(), e.getV().getY()));

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

    private void drawVertex(Graphics2D g2, Vertex v) {
        int x = (int) v.getX(), y = (int) v.getY(), r = VERTEX_RADIUS;
        g2.setColor(COLOR_VERTEX);
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
