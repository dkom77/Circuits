package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.Graph;

import java.lang.reflect.Constructor;
import java.util.*;

public abstract class GraphProcessor {
    protected Graph graph;
    protected List<List<Integer>> loops;

    public GraphProcessor() {
        this.loops = new ArrayList<>();
        this.graph = null;
    }

    public GraphProcessor(Graph graph) {
        this();
        this.graph = graph;
    }

    public void setGraph(Graph graph){
        this.graph = graph;
    }

    public abstract GraphProcessor findCycles() throws IllegalStateException;

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

    protected List<Graph> getSCCs(Graph graph) {
        LinkedList<Integer> order = getFinishingOrder(graph, new LinkedList<>(graph.getVertices()));
        Graph reverse = invertGraph(graph);

        List<Graph> stronglyConnectedComponents = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Set<Integer> finished = new LinkedHashSet<>();

        List<Set<Integer>> sccs = new ArrayList<>();

        while(order.size() > 0) {
            int v = order.removeLast();
            if (visited.contains(v)){
                continue;
            }
            visit(reverse, v, visited, finished);

            sccs.add(new HashSet<>(finished));
            finished = new LinkedHashSet<>();
        }

        //restore links
        for (Set<Integer> scc: sccs){
            Graph component = getGraphInstance(graph);
            for (Integer v: scc){
                component.addVertex(v);
                HashSet<Integer> edges = graph.getEdges(v);
                for(Integer edge: edges){
                    if (!scc.contains(edge)){
                        continue;
                    }
                    component.addEdge(v, edge);
                }
            }
            stronglyConnectedComponents.add(component);
        }

        return stronglyConnectedComponents;
    }

    protected LinkedList<Integer> getFinishingOrder(Graph graph, LinkedList<Integer> order){
        Comparator<Integer> comparator = Integer::compareTo;
        order.sort(comparator.reversed());

        Set<Integer> visited = new HashSet<>();
        Set<Integer> finished = new LinkedHashSet<>();

        while (order.size() > 0){
            int v = order.removeLast();
            if (!visited.contains(v)){
                visit(graph, v, visited, finished);
            }
        }
        return new LinkedList<>(finished);
    }

    protected void visit(Graph graph, int v, Set<Integer> visited, Set<Integer> finished){
        visited.add(v);
        graph.getEdges(v).forEach(e -> {
            if (!visited.contains(e)){
                visit(graph, e, visited, finished);
            }
        });
        finished.add(v);
    }

    protected List<Integer> reconstructPath(Integer start, Integer finish, HashMap<Integer, Integer> map) {
        LinkedList<Integer> path = new LinkedList<>();
        Integer curr = finish;
        while (!curr.equals(start)) {
            path.addFirst(curr);
            curr = map.get(curr);
        }
        path.addFirst(start);
        return path;
    }

    protected void sortPaths (List<List<Integer>> cycles) {
        cycles.sort((List<Integer> c1, List<Integer> c2) -> {
            for (int i = 0; i < Math.min(c1.size(), c2.size()); i++) {
                if (!c1.get(i).equals(c2.get(i))) {
                    return (c1.get(i) < c2.get(i)) ? -1 : 1;
                }
            }

            if (c1.size() < c2.size()) {
                return -1;
            }

            if (c1.size() < c2.size()) {
                return 1;
            }

            return 0;
        });
    }

    private String unwindCycle(List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        path.forEach(v -> sb.append(v).append(" "));
        return sb.toString().trim();
    }
}
