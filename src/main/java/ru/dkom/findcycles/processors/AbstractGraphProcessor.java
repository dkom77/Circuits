package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.Graph;

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

    public abstract String printCycles();
}
