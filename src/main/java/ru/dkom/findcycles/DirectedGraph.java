package ru.dkom.findcycles;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DirectedGraph implements Graph{

    private Map<Integer, HashSet<Integer>> graph;

    public DirectedGraph(){
        graph = new HashMap<Integer, HashSet<Integer>>();
    }

    public void addVertex(int id) {
        if (!graph.containsKey(id)){
            graph.put(id, new HashSet<Integer>());
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
        return (graph.get(id) == null) ? new HashSet<Integer>(): graph.get(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectedGraph)) return false;
        DirectedGraph other = (DirectedGraph) o;
        return graph.equals(other.graph);
    }

    @Override
    public int hashCode() {
        return graph.hashCode();
    }

    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        //graph.entrySet().forEach(e -> {sb.append(e.getKey()).append(": "); e.getValue().forEach(ev -> sb.append(ev).append(" ")); sb.append("\n");});
        graph.forEach((key, value) -> {sb.append(key).append(": "); value.forEach(v -> sb.append(v).append(" ")); sb.append("\n");});
        return sb.toString();
    }


}
