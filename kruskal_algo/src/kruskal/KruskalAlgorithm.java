package kruskal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Алгоритм Краскала
 */
public class KruskalAlgorithm {

    /** Строит список шагов алгоритма для заданного графа. */
    public static List<KruskalStep> run(Graph graph) {
        List<KruskalStep> steps = new ArrayList<>();

        // Сортировка рёбер по возрастанию веса.
        List<Edge> sorted = new ArrayList<>(graph.getEdges());
        sorted.sort(Comparator.comparingInt(Edge::getWeight));

        // Каждая вершина — в отдельном множестве
        DisjointSet dsu = new DisjointSet();
        for (Vertex v : graph.getVertices()) {
            dsu.makeSet(v);
        }

        int mstWeight = 0;
        int chosen = 0;
        int needed = Math.max(0, graph.getVertices().size() - 1);

        // Последовательная обработка рёбер
        for (Edge e : sorted) {
            if (chosen == needed) break; // остов уже построен

            steps.add(new KruskalStep(KruskalStep.Type.CONSIDER, e, mstWeight, chosen,
                    "Рассматривается ребро " + e + "."));

            if (dsu.sameSet(e.getU(), e.getV())) {
                steps.add(new KruskalStep(KruskalStep.Type.REJECT, e, mstWeight, chosen,
                        "Вершины " + e.getU() + " и " + e.getV()
                                + " уже в одной компоненте — ребро образует цикл и отклоняется."));
            } else {
                dsu.union(e.getU(), e.getV());
                mstWeight += e.getWeight();
                chosen++;
                steps.add(new KruskalStep(KruskalStep.Type.ACCEPT, e, mstWeight, chosen,
                        "Вершины " + e.getU() + " и " + e.getV()
                                + " в разных компонентах — ребро добавляется в остов."));
            }
        }

        // Завершение алгоритма.
        String result;
        if (chosen == needed && !graph.getVertices().isEmpty()) {
            result = "Алгоритм завершён. Минимальное остовное дерево построено: "
                    + chosen + " рёбер, суммарный вес " + mstWeight + ".";
        } else {
            result = "Алгоритм завершён. Граф несвязный — построен минимальный остовный лес: "
                    + chosen + " рёбер, суммарный вес " + mstWeight + ".";
        }
        steps.add(new KruskalStep(KruskalStep.Type.FINISH, null, mstWeight, chosen, result));
        return steps;
    }
}
