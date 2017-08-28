package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.Graph;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;

public class AdvancedGP extends GraphProcessor {

    public static class NotACycleException extends Throwable{

    }


    public AdvancedGP(){
        super();
    }

    @Override
    public AdvancedGP findCycles() throws IllegalStateException {
        if (graph == null) throw new IllegalStateException("Graph not defined");

        loops = new ArrayList<>();


        List<Graph> sccs = getSCCs(graph);

        for (Graph scc: sccs){
            loops.addAll(findCyclesInSCC(scc));
        }

        loops = removeDuplicatedRoutes(loops);
        sortPaths(loops);


        return this;
    }

    private List<List<Integer>> findCyclesInSCC(Graph g){
        List<Integer> vertices = new ArrayList<>(g.getVertices());
        if (vertices.isEmpty()) return null;

        vertices.sort(Integer::compare);
        List<List<Integer>> paths = new ArrayList<>();

        for (Integer vertex: vertices){
            Graph subGraph = subGraph(g, vertex);
            findCyclesInSCC(subGraph);
        }



        return paths;
    }

    private Graph subGraph(Graph graph, Integer start){
        Graph invert = invertGraph(graph);

        HashSet<Integer> roots = invert.getEdges(start);

        Graph subGraph = getGraphInstance(graph);

        for (Integer b: roots){
            try {
                List<Integer> subGraphVertices = exploreCycle(graph, start, b);
                subGraphVertices.forEach(subGraph::addVertex);

                Set<Integer> sgvs = subGraph.getVertices();
                for(Integer subGraphVertex: sgvs){
                    HashSet<Integer> subGraphEdges = graph.getEdges(subGraphVertex);
                    for(Integer edge: subGraphEdges){
                        if (!sgvs.contains(edge)){
                            continue;
                        }
                        subGraph.addEdge(subGraphVertex, edge);
                    }
                }

            } catch (NotACycleException e) {
                //e.printStackTrace();
            }
        }

        return subGraph;
    }


    private boolean pathExist(Graph graph, Integer start, Integer dest){
        try{
            exploreCycle(graph, start, dest);
            return true;
        }catch (NotACycleException nac){
            return false;
        }
    }

    private List<Integer> exploreCycle(Graph graph, Integer start, Integer dest) throws NotACycleException {
        LinkedList<Integer> stack = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();

        HashMap<Integer, Integer> map = new HashMap<>();

        visited.add(start);
        stack.add(start);
        while (stack.size() > 0) {
            Integer curr = stack.removeLast();

            if (curr.equals(dest)) {
                return reconstructPath(start, dest, map);
            }

            HashSet<Integer> edges = graph.getEdges(curr);

            for (Integer n : edges) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    stack.add(n);
                    map.put(n, curr);
                }
            }
        }
        throw new NotACycleException();
    }

    private List<List<Integer>> removeDuplicatedRoutes(List<List<Integer>> allPath){

        List<List<Integer>> result = new ArrayList<>();

        for (List<Integer> path: allPath){
            int minIndex = IntStream.range(0,path.size()).boxed()
                    .min(comparingInt(path::get))
                    .orElseThrow(() -> new IllegalArgumentException("list is empty"));

            //min element goes first, list rewinded
            List<Integer> normalized = new ArrayList<>(path.subList(minIndex, path.size()));
            normalized.addAll(path.subList(0, minIndex));
            normalized.add(normalized.get(0));


            result.add(normalized);
        }

        return result;
    }

    private void sortPaths (List<List<Integer>> cycles){
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


}
