package kruskal;

/** Вершина графа -id и координаты на рабочем поле */
public class Vertex {
    private final int id;
    private double x;
    private double y;

    public Vertex(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }

    public void moveTo(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
