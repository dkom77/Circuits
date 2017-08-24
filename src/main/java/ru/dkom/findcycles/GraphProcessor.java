package ru.dkom.findcycles;

import java.util.*;
import java.util.stream.Collectors;

public class GraphProcessor {

    private Graph graph;
    private List<List<Integer>> cycles;

    public GraphProcessor(Graph graph){
        this.graph = graph;
        this.cycles = new ArrayList<>();
    }

    public GraphProcessor findCycles(){
        List<List<Integer>> cycles = getCycles();
        cycles = cycles.stream()
                .filter(entry -> entry.size() > 1 )
                .collect(Collectors.toList());
        return this;
    }

    public String printCycles(){
        StringBuilder sb = new StringBuilder();
//        for (Graph c: cycles){
//            sb.append(unwindCycle(c)).append("\n");
//        }
        return sb.toString().trim();
    }

    private String unwindCycle(Graph cycle){
        StringBuilder sb = new StringBuilder();

        //LinkedList<Integer> order = getFinishingOrder((DirectedGraph) cycle, new LinkedList<>(cycle.getVertices()));

//        for (Integer v: cycle.getVertices()){
//            if ((order.size() == 0)||(!order.get(order.size() - 1).equals(v))){
//                order.add(v);
//            }
//            for(Integer e: cycle.getEdges(v)){
//                if (order.get(order.size() - 1).equals(e)){
//                    continue;
//                }
//                order.add(e);
//            }
//        }

//        order.forEach(o -> sb.append(o).append(" "));
        return sb.toString().trim();
    }

    public List<List<Integer>> getCycles(){
        List<List<Integer>> cycles = new ArrayList<>();

        LinkedList<Integer> order = new LinkedList<>(graph.getVertices());
        for (Integer o: order){
            ArrayList<Integer> cycle = exploreCycle(o);
            cycles.add(cycle);
        }

        return null;
    }

    private ArrayList<Integer> exploreCycle(Integer start){
        LinkedList<Integer> stack = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        ArrayList<Integer> cycle = new ArrayList<>();

        visited.add(start);
        stack.add(start);

        while (stack.size() > 0){
            Integer curr = stack.removeLast();
            HashSet<Integer> edges = graph.getEdges(curr);
            for(Integer n: edges){
                if (visited.contains(n)){
                    cycle.add(curr);
                    cycle.add(n);
                    break;
                }
                visited.add(n);
                cycle.add(curr);
                stack.add(n);
            }
        }
        return cycle;
    }

}
