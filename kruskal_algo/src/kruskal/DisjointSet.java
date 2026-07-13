package kruskal;

import java.util.HashMap;
import java.util.Map;

/**
 * Система непересекающихся множеств
 */
public class DisjointSet {
    private final Map<Vertex, Vertex> parent = new HashMap<>();
    private final Map<Vertex, Integer> rank = new HashMap<>();

    public void makeSet(Vertex v) {
        parent.put(v, v);
        rank.put(v, 0);
    }

    public Vertex find(Vertex v) {
        Vertex p = parent.get(v);
        if (p != v) {
            p = find(p);          // сжатие путей
            parent.put(v, p);
        }
        return p;
    }

    /** Объединяет множества; возвращает false, если вершины уже в одном множестве. */
    public boolean union(Vertex a, Vertex b) {
        Vertex ra = find(a), rb = find(b);
        if (ra == rb) return false;
        int rka = rank.get(ra), rkb = rank.get(rb);
        if (rka < rkb) {
            parent.put(ra, rb);
        } else if (rka > rkb) {
            parent.put(rb, ra);
        } else {
            parent.put(rb, ra);
            rank.put(ra, rka + 1);
        }
        return true;
    }

    public boolean sameSet(Vertex a, Vertex b) {
        return find(a) == find(b);
    }
}
