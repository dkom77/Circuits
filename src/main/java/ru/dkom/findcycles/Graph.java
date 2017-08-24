package ru.dkom.findcycles;

import java.util.HashSet;
import java.util.Set;

public interface Graph {
    void addVertex(int id);
    void addEdge(int from, int to);
    boolean containsVertex(int id);

    Set<Integer> getVertices();
    HashSet<Integer> getEdges(int id);

}
