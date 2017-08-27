package ru.dkom.findcycles.data;

import java.util.*;

public class AdjListGraph implements Graph{

    private Map<Integer, HashSet<Integer>> graph;

    public AdjListGraph(){
        graph = new HashMap<>();
    }

    public void addVertex(int id) {
        if (!graph.containsKey(id)){
            graph.put(id, new HashSet<>());
        }
    }

    public void addEdge(int from, int to) {
        addVertex(from);
        addVertex(to);
        graph.get(from).add(to);
    }

    public boolean containsVertex(int id) {
        return graph.containsKey(id);
    }

    public Set<Integer> getVertices() {
        return graph.keySet();
    }

    public HashSet<Integer> getEdges(int id) {
        return (graph.get(id) == null) ? new HashSet<>(): graph.get(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdjListGraph)) return false;
        AdjListGraph other = (AdjListGraph) o;
        return graph.equals(other.graph);
    }

    @Override
    public int hashCode() {
        return graph.hashCode();
    }

    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        graph.forEach((key, value) -> {
            sb.append(key).append(": ");
            value.forEach(v -> sb.append(v).append(" "));
            sb.append("\n");
        });
        return sb.toString();
    }


}
