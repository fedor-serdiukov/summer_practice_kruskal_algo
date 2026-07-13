package kruskal;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель графа (прототип): только хранение вершин и рёбер
 * для отображения демонстрационного графа
 */
public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();
    private int nextId = 1;

    public List<Vertex> getVertices() { return vertices; }
    public List<Edge> getEdges() { return edges; }

    public Vertex addVertex(double x, double y) {
        Vertex v = new Vertex(nextId++, x, y);
        vertices.add(v);
        return v;
    }

    public Edge addEdge(Vertex u, Vertex v, int weight) {
        Edge e = new Edge(u, v, weight);
        edges.add(e);
        return e;
    }
}
