package ru.dkom.findcycles;

import java.util.*;

/**
 * Created by User on 26.08.2017.
 */
public class SCC {

    private Graph graph;

    public SCC(Graph graph){
        this.graph = graph;
    }


    public List<DirectedGraph> getSCCs() {
        LinkedList<Integer> order = getFinishingOrder((DirectedGraph) graph, new LinkedList<>(graph.getVertices()));
        DirectedGraph reverse = invertGraph();

        List<DirectedGraph> stronglyConnectedComponents = new ArrayList<>();


        Set<Integer> visited = new HashSet<>();
        LinkedHashSet<Integer> finished = new LinkedHashSet<>();

        while(order.size() > 0) {
            int v = order.removeLast();
            if (finished.contains(v)){
                continue;
            }

            LinkedHashSet<Integer> sccVertices = new LinkedHashSet<>();
            visit(reverse, v, visited, sccVertices);

            Graph scc = new DirectedGraph();
            finished.addAll(sccVertices);


            for (int sccV: sccVertices){
                scc.addVertex(sccV);
                for (int sccE: graph.getEdges(sccV)){
                    if (sccVertices.contains(sccE)){
                        scc.addEdge(sccV, sccE);
                    }

                }
            }

            stronglyConnectedComponents.add((DirectedGraph) scc);

        }
        return stronglyConnectedComponents;
    }

    private LinkedList<Integer> getFinishingOrder(DirectedGraph graph, LinkedList<Integer> order){
        //implement DFS
        Set<Integer> visited = new HashSet<>();
        //preserving order
        LinkedHashSet<Integer> finished = new LinkedHashSet<>();
        //LinkedList<Integer> order = new LinkedList<>(graph.getVertices());

        while (order.size() > 0){
            int v = order.removeLast();
            if (!visited.contains(v)){
                visit(graph, v, visited, finished);
            }
        }

        return new LinkedList<>(finished);
    }

    private void visit(DirectedGraph graph, int v, Set<Integer> visited, LinkedHashSet<Integer> finished){
        visited.add(v);
        for (int n: graph.getEdges(v)){
            if (!visited.contains(n)){
                try{
                    visit(graph, n, visited, finished);
                }catch (Exception e){
                    int t = 0;
                }

            }
        }
        finished.add(v);
    }

    private DirectedGraph invertGraph() {
        DirectedGraph reverse = new DirectedGraph();

        graph.getVertices().forEach(reverse::addVertex);
        reverse.getVertices().forEach(v -> graph.getEdges(v).forEach(e -> reverse.addEdge(e, v)));
        return reverse;

    }



}



