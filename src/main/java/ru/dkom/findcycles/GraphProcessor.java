package ru.dkom.findcycles;

import java.util.*;

public class GraphProcessor {

    private final static int MIN_LOOP_SIZE = 2;

    private Graph graph;
    private Map<DirectedGraph, LinkedList<Integer>> cycles;

    public GraphProcessor(Graph graph) {
        this.graph = graph;
        this.cycles = new HashMap<>();
    }

    public GraphProcessor findCycles() {
        getCycles();
        //cycles = getCycles();
        //cycles = new HashSet<DirectedGraph>(getSCCs());
        return this;
    }

    public String printCycles() {
        StringBuilder sb = new StringBuilder();
        //cycles.forEach(cs -> sb.append(unwindCycle(cs)).append("\n"));
        //cycles.forEach(c-> sb.append(c.toString()).append("\n"));

        for(Map.Entry<DirectedGraph, LinkedList<Integer>> entry: cycles.entrySet()){
            sb.append(unwindCycle(entry.getValue())).append("\n");
        }

        return sb.toString().trim();
    }

    private String unwindCycle(LinkedList<Integer> path) {
        StringBuilder sb = new StringBuilder();

        path.forEach(v -> sb.append(v).append(" "));


//        //cycle.getVertices().forEach(v -> sb.append(v).append(" "));
//        List<Integer> cycleVertices = new ArrayList<>(cycle.getVertices());
//        ArrayList<Integer> list = new ArrayList<>();
//
//        for(Iterator<Integer> iterator = cycleVertices.iterator(); iterator.hasNext();){
//            Integer vertex = iterator.next();
//
//        }
//
//        for (int i = 0; i < cycleVertices.size(); i ++){
//            Integer currVertex = cycleVertices.get(i);
//            list.add(currVertex);
//
//            HashSet<Integer> edges = cycle.getEdges(currVertex);
//            if (edges.size() != 1){
//                throw new RuntimeException("Bad cycle");
//            }
//
//
//        }
//
//        list.add(list.get(0));
//
//        list.forEach(v -> sb.append(v).append(" "));
//
        return sb.toString().trim();
    }

    //private HashMap<DirectedGraph, LinkedList<Integer>> getCycles() {
    private void getCycles() {
        //HashSet<DirectedGraph> cycles = new HashSet<>();

        DirectedGraph reverse = invertGraph();

        for (Integer start : graph.getVertices()) {
            for (Integer dest : reverse.getEdges(start)) {
                DirectedGraph cycle;
                try {
                    //cycle = exploreCycle(start, dest);

                    exploreCycle(start, dest);

//                    if (cycle.getVertices().size() >= MIN_LOOP_SIZE) {
//                        cycles.add(cycle);
//                    }



                } catch (Exception e) {
                    continue;
                }
            }
        }
        //return cycles;
    }

    private DirectedGraph exploreCycle(Integer start, Integer dest) throws Exception {
        LinkedList<Integer> stack = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();

        HashMap<Integer, Integer> map = new HashMap<>();

        DirectedGraph cycle = new DirectedGraph();

        visited.add(start);
        stack.add(start);
        while (stack.size() > 0) {
            Integer curr = stack.removeLast();

            if (curr.equals(dest)) {
                LinkedList<Integer> path = new LinkedList<>();

                while (!curr.equals(start)) {
                    path.addFirst(curr);
                    curr = map.get(curr);
                }
                path.addFirst(start);
                path.addLast(start);

                //return path;
                for (int i = 0; i < path.size() - 1; i++){
                    cycle.addVertex(path.get(i));
                    cycle.addEdge(path.get(i), path.get(i + 1));
                }
                cycle.addEdge(path.get(path.size() - 1), path.get(0));

                cycles.put(cycle, path);

                return cycle;
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


    private DirectedGraph invertGraph() {
        DirectedGraph reverse = new DirectedGraph();

        graph.getVertices().forEach(reverse::addVertex);
        reverse.getVertices().forEach(v -> graph.getEdges(v).forEach(e -> reverse.addEdge(e, v)));
        return reverse;

    }

}