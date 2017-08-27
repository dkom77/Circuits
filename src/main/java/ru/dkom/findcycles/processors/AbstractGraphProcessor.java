package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.Graph;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphProcessor {
    protected Graph graph;
    protected List<List<Integer>> loops;

    public AbstractGraphProcessor() {
        this.loops = new ArrayList<>();
        this.graph = null;
    }

    public AbstractGraphProcessor(Graph graph) {
        this();
        this.graph = graph;
    }

    public void setGraph(Graph graph){
        this.graph = graph;
    }

    public abstract AbstractGraphProcessor findCycles() throws IllegalStateException;

    public String printCycles(){
        StringBuilder sb = new StringBuilder();
        loops.forEach(cs -> sb.append(unwindCycle(cs)).append("\n"));
        return sb.toString().trim();
    }

    protected Graph invertGraph(Graph graph) {
        Graph reverse = getGraphInstance(graph);
        graph.getVertices().forEach(reverse::addVertex);
        reverse.getVertices().forEach(v -> graph.getEdges(v).forEach(e -> reverse.addEdge(e, v)));
        return reverse;
    }

    protected Graph getGraphInstance(Graph graph){
        try {
            Constructor<? extends Graph> constructor = graph.getClass().getConstructor();
            return constructor.newInstance();
        }catch (Exception e){
            throw new RuntimeException("failed to invert graph");
        }
    }

    private String unwindCycle(List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        path.forEach(v -> sb.append(v).append(" "));
        return sb.toString().trim();
    }
}
