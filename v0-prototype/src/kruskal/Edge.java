package kruskal;

/** Неориентированное взвешенное ребро графа  */
public class Edge {
    private final Vertex u;
    private final Vertex v;
    private int weight;

    public Edge(Vertex u, Vertex v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public Vertex getU() { return u; }
    public Vertex getV() { return v; }
    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    /** Проверяет соединяет ли ребро те же две вершины (без учёта направления) */
    public boolean connects(Vertex a, Vertex b) {
        return (u == a && v == b) || (u == b && v == a);
    }

    public boolean isIncidentTo(Vertex a) {
        return u == a || v == a;
    }

    @Override
    public String toString() {
        return "(" + u.getId() + " — " + v.getId() + ", вес " + weight + ")";
    }
}
