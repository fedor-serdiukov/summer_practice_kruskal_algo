package kruskal;

/** Один шаг алгоритма Краскала (для пошаговой визуализации и журнала) */
public class KruskalStep {

    public enum Type {
        /** Ребро взято на рассмотрение (подсвечивается жёлтым) */
        CONSIDER,
        /** Ребро добавлено в остов (зелёное) */
        ACCEPT,
        /** Ребро образует цикл и отклонено (красное) */
        REJECT,
        /** Алгоритм завершён  */
        FINISH
    }

    private final Type type;
    private final Edge edge;           // null для FINISH
    private final int mstWeight;       // вес остова ПОСЛЕ этого шага
    private final int chosenCount;     // число выбранных рёбер ПОСЛЕ этого шага
    private final String description;  // текстовое пояснение действия

    public KruskalStep(Type type, Edge edge, int mstWeight, int chosenCount, String description) {
        this.type = type;
        this.edge = edge;
        this.mstWeight = mstWeight;
        this.chosenCount = chosenCount;
        this.description = description;
    }

    public Type getType() { return type; }
    public Edge getEdge() { return edge; }
    public int getMstWeight() { return mstWeight; }
    public int getChosenCount() { return chosenCount; }
    public String getDescription() { return description; }
}
