package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.AdjListGraph;
import ru.dkom.findcycles.data.Graph;

import java.lang.reflect.Constructor;
import java.util.*;

public class JohnsonAlgGraphProcessor extends AbstractGraphProcessor{

    private boolean[] blocked;
    private List<Integer> stack;

    public JohnsonAlgGraphProcessor(){
        super();
    }

    @Override
    public JohnsonAlgGraphProcessor findCycles() throws IllegalStateException {
        if (graph == null) throw new IllegalStateException("Graph not defined");

        loops = new ArrayList<>();

        stack = new LinkedList<>();
        blocked = new boolean[graph.getVertices().size()];

        List<Graph> sccs = getSCCs(graph);


        return this;
    }

    public List<Graph> getSCCs(Graph graph) {
        LinkedList<Integer> order = getFinishingOrder(graph, new LinkedList<>(graph.getVertices()));
        Graph reverse = invertGraph(graph);

        List<Graph> stronglyConnectedComponents = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Set<Integer> finished = new LinkedHashSet<>();

        List<Set<Integer>> sccsVertices = new ArrayList<>();

        while(order.size() > 0) {
            int v = order.removeLast();
            if (visited.contains(v)){
                continue;
            }
            visit(reverse, v, visited, finished);

            sccsVertices.add(new HashSet<>(finished));
            finished = new LinkedHashSet<>();
        }

        //restore links
        for (Set<Integer> scc: sccsVertices){
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

    private LinkedList<Integer> getFinishingOrder(Graph graph, LinkedList<Integer> order){
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

    private void visit(Graph graph, int v, Set<Integer> visited, Set<Integer> finished){
        visited.add(v);
        graph.getEdges(v).forEach(e -> {
            if (!visited.contains(e)){
                visit(graph, e, visited, finished);
            }
        });
        finished.add(v);
    }





}
