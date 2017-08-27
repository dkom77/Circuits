package ru.dkom.findcycles;

import java.lang.reflect.Constructor;
import java.util.*;

public class SCC {

    private Graph graph;

    public SCC(Graph graph){
        this.graph = graph;
    }


    public List<Graph> getSCCs() {
        LinkedList<Integer> order = getFinishingOrder(graph, new LinkedList<>(graph.getVertices()));
        Graph reverse = invertGraph();

        List<Graph> stronglyConnectedComponents = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Set<Integer> finished = new LinkedHashSet<>();

        while(order.size() > 0) {
            int v = order.removeLast();
            if (finished.contains(v)){
                continue;
            }

            LinkedHashSet<Integer> sccVertices = new LinkedHashSet<>();
            visit(reverse, v, visited, sccVertices);

            Graph scc = new AdjListGraph();
            finished.addAll(sccVertices);
            sccVertices.forEach(sccV -> {
                graph.getEdges(sccV).forEach(sccE-> scc.addEdge(sccV, sccE));
            });


            stronglyConnectedComponents.add(scc);

        }
        return stronglyConnectedComponents;
    }

    private LinkedList<Integer> getFinishingOrder(Graph graph, LinkedList<Integer> order){
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

    private Graph invertGraph(){
        try{
            Constructor<? extends Graph> constructor = graph.getClass().getConstructor();
            Graph reverse = constructor.newInstance();

            graph.getVertices().forEach(reverse::addVertex);
            reverse.getVertices().forEach(v -> graph.getEdges(v).forEach(e -> reverse.addEdge(e, v)));
            return reverse;
        }catch (Exception e){
            throw new RuntimeException("failed to invert graph");
        }
    }



}



