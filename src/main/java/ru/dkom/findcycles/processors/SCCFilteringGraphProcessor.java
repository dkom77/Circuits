package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.AdjListGraph;
import ru.dkom.findcycles.data.Graph;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;

public class SCCFilteringGraphProcessor extends AbstractGraphProcessor {

    public SCCFilteringGraphProcessor() {
        super();
    }

    @Override
    public SCCFilteringGraphProcessor findCycles() {
        if (graph == null) throw new IllegalStateException("Graph not defined");
        System.out.println("finding Scc: ");
        List<Graph> components = getSCCs(graph);
        System.out.println("" + components.size() + " found");

        for (Graph c : components) {
            List<List<Integer>> cycles = getCycles(c);
            loops.addAll(cycles);
        }
        return this;
    }

    private List<List<Integer>> getCycles(Graph graph) {
        List<List<Integer>> cycles = new ArrayList<>();
        List<List<Integer>> allPath = new ArrayList<>();

        Graph reverse = invertGraph(graph);

        for (Integer start : graph.getVertices()) {
            for (Integer dest : reverse.getEdges(start)) {
                try {
                    List<Integer> path = exploreCycle(graph, start, dest);
                    allPath.add(path);
                } catch (Exception e) {
                    //cycle not found for route start-dest
                }
            }
        }

        cycles = removeDuplicatedRoutes(allPath);
        sortPaths(cycles);

        return cycles;
    }

    private List<Integer> exploreCycle(Graph graph, Integer start, Integer dest) throws Exception {
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
        throw new Exception("not a cycle");
    }

    private List<Integer> reconstructPath(Integer start, Integer finish, HashMap<Integer, Integer> map) {
        LinkedList<Integer> path = new LinkedList<>();
        Integer curr = finish;
        while (!curr.equals(start)) {
            path.addFirst(curr);
            curr = map.get(curr);
        }
        path.addFirst(start);
        return path;
    }

    private List<List<Integer>> removeDuplicatedRoutes(List<List<Integer>> allPath){
        Set<List<Integer>> uniquePaths = new HashSet<>();

        for (List<Integer> path: allPath){
            int minIndex = IntStream.range(0,path.size()).boxed()
                    .min(comparingInt(path::get))
                    .orElseThrow(() -> new IllegalArgumentException("list is empty"));

            //min element goes first, list rewinded
            List<Integer> normalized = new ArrayList<>(path.subList(minIndex, path.size()));
            normalized.addAll(path.subList(0, minIndex));
            normalized.add(normalized.get(0));

            uniquePaths.add(normalized);
        }

        return new ArrayList<>(uniquePaths);
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

    public List<Graph> getSCCs(Graph graph) {
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