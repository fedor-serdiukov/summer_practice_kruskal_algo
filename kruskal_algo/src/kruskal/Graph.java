package kruskal;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Модель неориентированного взвешенного графа.
 * Хранит вершины и рёбра, поддерживает редактирование
 * и загрузку/сохранение в текстовый формат
 *
 *   N M
 *   u1 v1 w1
 *   ...
 *   uM vM wM
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

    public void removeVertex(Vertex v) {
        edges.removeIf(e -> e.isIncidentTo(v));
        vertices.remove(v);
    }

    /** Возвращает null, если ребро добавить нельзя (петля или дубликат). */
    public Edge addEdge(Vertex u, Vertex v, int weight) {
        if (u == v || findEdge(u, v) != null) {
            return null;
        }
        Edge e = new Edge(u, v, weight);
        edges.add(e);
        return e;
    }

    public void removeEdge(Edge e) {
        edges.remove(e);
    }

    public Edge findEdge(Vertex u, Vertex v) {
        for (Edge e : edges) {
            if (e.connects(u, v)) return e;
        }
        return null;
    }

    public void clear() {
        vertices.clear();
        edges.clear();
        nextId = 1;
    }

    /** Сохранение графа в файл. Вершины перенумеровываются в порядке 1..N */
    public void saveToFile(File file) throws IOException {
        Map<Vertex, Integer> index = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) {
            index.put(vertices.get(i), i + 1);
        }
        try (PrintWriter out = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            out.println(vertices.size() + " " + edges.size());
            for (Edge e : edges) {
                out.println(index.get(e.getU()) + " " + index.get(e.getV()) + " " + e.getWeight());
            }
        }
    }

    /**
     * Загрузка графа из файла с проверками формата.
     * При ошибке выбрасывается IOException с указанием проблемной строки.
     * Вершины расставляются по окружности
     */
    public void loadFromFile(File file, int width, int height) throws IOException {
        List<int[]> loadedEdges = new ArrayList<>();
        int n;
        try (Scanner sc = new Scanner(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            if (!sc.hasNextInt()) throw new IOException("Строка 1: не удалось прочитать число вершин");
            n = sc.nextInt();
            if (!sc.hasNextInt()) throw new IOException("Строка 1: не удалось прочитать число рёбер");
            int m = sc.nextInt();
            if (n < 0 || m < 0) throw new IOException("Строка 1: отрицательное число вершин или рёбер");
            for (int i = 0; i < m; i++) {
                String where = "Строка " + (i + 2) + " (ребро №" + (i + 1) + "): ";
                if (!sc.hasNextInt()) throw new IOException(where + "не удалось прочитать первую вершину");
                int u = sc.nextInt();
                if (!sc.hasNextInt()) throw new IOException(where + "не удалось прочитать вторую вершину");
                int v = sc.nextInt();
                if (!sc.hasNextInt()) throw new IOException(where + "не удалось прочитать вес");
                int w = sc.nextInt();
                if (u < 1 || u > n || v < 1 || v > n) {
                    throw new IOException(where + "номер вершины вне диапазона 1.." + n);
                }
                loadedEdges.add(new int[]{u, v, w});
            }
        }

        clear();
        // Расставляем вершины по окружности в центре рабочей области.
        double cx = width / 2.0, cy = height / 2.0;
        double r = Math.max(60, Math.min(width, height) / 2.0 - 60);
        Vertex[] byNumber = new Vertex[n + 1];
        for (int i = 1; i <= n; i++) {
            double angle = 2 * Math.PI * (i - 1) / Math.max(1, n) - Math.PI / 2;
            byNumber[i] = addVertex(cx + r * Math.cos(angle), cy + r * Math.sin(angle));
        }
        for (int[] e : loadedEdges) {
            addEdge(byNumber[e[0]], byNumber[e[1]], e[2]);
        }
    }
}
