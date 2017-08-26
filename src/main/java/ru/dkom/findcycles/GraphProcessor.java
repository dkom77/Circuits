package ru.dkom.findcycles;

import java.util.*;

public class GraphProcessor {

    private final static int MIN_LOOP_SIZE = 2;

    private Graph graph;
    private Set<Cycle> cycles;

    public GraphProcessor(Graph graph) {
        this.graph = graph;
        this.cycles = new HashSet<>();
    }

    public GraphProcessor findCycles() {
        cycles = getCycles();
        return this;
    }

    public String printCycles() {
        StringBuilder sb = new StringBuilder();
        //cycles.forEach(cs -> sb.append(unwindCycle(cs)).append("\n"));
        cycles.forEach(c-> sb.append(c.toString()).append("\n"));
        return sb.toString().trim();
    }

    private String unwindCycle(List<Integer> cycle) {
        StringBuilder sb = new StringBuilder();
        cycle.forEach(c -> sb.append(c).append(" "));
        return sb.toString().trim();
    }

    public Set<Cycle> getCycles() {
        HashSet<Cycle> cycles = new HashSet<>();

        DirectedGraph reverse = invertGraph();

        Map<Integer, Integer> done = new HashMap<>();


        for (Integer start : graph.getVertices()) {
            int k = 0;
            for (Integer dest : reverse.getEdges(start)) {
                ArrayList<Integer> cycle;
                try {
                    cycle = exploreCycle(start, dest, done);
                    for(int i = 0; i < cycle.size() - 1; i++){
                        //done.put(cycle.get(i), cycle.get(i + 1));
                    }
                    int t = 0;

                } catch (Exception e) {
                    continue;
                }

                if (cycle.size() > MIN_LOOP_SIZE) {
                    cycles.add(new Cycle(cycle));
                }
            }
        }
        return cycles;
    }


//    public List<List<Integer>> getCycles() {
//        List<List<Integer>> cycles = new ArrayList<>();
//
//        DirectedGraph reverse = invertGraph();
//
//        Map<Integer, Integer> done = new HashMap<>();
//
//
//        for (Integer start : graph.getVertices()) {
//            int k = 0;
//            for (Integer dest : reverse.getEdges(start)) {
//                ArrayList<Integer> cycle;
//                try {
//                    cycle = exploreCycle(start, dest, done);
//                    for(int i = 0; i < cycle.size() - 1; i++){
//                        //done.put(cycle.get(i), cycle.get(i + 1));
//                    }
//                    int t = 0;
//
//                } catch (Exception e) {
//                    continue;
//                }
//
//                if (cycle.size() > MIN_LOOP_SIZE) {
//                    cycles.add(cycle);
//                }
//            }
//        }

//        HashMap<Integer, Integer> toVisit = new HashMap<>();
//        graph.getVertices().forEach(v -> toVisit.put(v, graph.getEdges(v).size()));
//
//        Set<Integer> done = new HashSet<>();
//
//        Iterator<Map.Entry<Integer,Integer>> iter = toVisit.entrySet().iterator();
//        while (iter.hasNext()){
//            Integer curr = iter.next().getKey();
//            if (toVisit.get(curr) <= 0){
//                iter.remove();
//                continue;
//            }
//            ArrayList<Integer> cycle;
//            try{
//                cycle = exploreCycle(curr, done);
//            }catch (Exception e){
//                continue;
//            }
//
//            if (cycle.size() > MIN_LOOP_SIZE){
//                cycles.add(cycle);
//            }
//
//            for(int i = 0; i < cycle.size() - 1; i++){
//                int cycleStep = cycle.get(i);
//                Integer count = toVisit.get(cycleStep);
//                count--;
//                toVisit.put(cycleStep, count);
//                if (count == 0){
//                    done.add(cycleStep);
//                }
//            }
//            iter = toVisit.entrySet().iterator();
//        }
//
//        return cycles;
//    }

    private ArrayList<Integer> exploreCycle(Integer start, Integer dest, Map<Integer, Integer> done) throws Exception {
        LinkedList<Integer> stack = new LinkedList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> cycle = new ArrayList<>();

        visited.add(start);
        stack.add(start);

        while (stack.size() > 0) {
            Integer curr = stack.removeLast();
            cycle.add(curr);
            HashSet<Integer> edges = graph.getEdges(curr);
            for (Integer n : edges) {
                if (done.get(curr)!= null && done.get(curr).equals(n)) {
                    continue;
                }
                if (visited.contains(n)) {
                    if (curr.equals(dest)) {
                        cycle.add(start);
                        return cycle;
                    }else {
                        //throw new Exception("not a cycle");
                        break;
                    }
                }
                visited.add(n);
                stack.add(n);
            }
        }
        throw new Exception("not a cycle");
    }

    private ArrayList<Integer> exploreCycle(Integer start, Set<Integer> done) throws Exception {
        LinkedList<Integer> stack = new LinkedList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> cycle = new ArrayList<>();

        visited.add(start);
        stack.add(start);

        while (stack.size() > 0) {
            Integer curr = stack.removeLast();
            cycle.add(curr);
            HashSet<Integer> edges = graph.getEdges(curr);
            for (Integer n : edges) {
                if (done.contains(n)) {
                    continue;
                }
                if (visited.contains(n)) {
                    if (visited.get(0).equals(n)) {
                        cycle.add(n);
                        return cycle;
                    } else {
                        throw new Exception("not a cycle");
                    }
                }
                visited.add(n);
                stack.add(n);
            }
        }
        throw new Exception("not a cycle");
    }

    private DirectedGraph invertGraph() {
        DirectedGraph reverse = new DirectedGraph();

        graph.getVertices().forEach(reverse::addVertex);
        reverse.getVertices().forEach(v -> graph.getEdges(v).forEach(e -> reverse.addEdge(e, v)));
//
//        return reverse;

//        DirectedGraph reverse = new DirectedGraph();
//
//        graph.getVertices().forEach(reverse::addVertex);
//        for (int v: reverse.getVertices()){
//            HashSet<Integer> edges = graph.getEdges(v);
//            for (int e: edges){
//                reverse.addEdge(e, v);
//            }
//        }

        return reverse;

    }

}
