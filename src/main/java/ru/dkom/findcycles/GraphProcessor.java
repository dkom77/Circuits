package ru.dkom.findcycles;

import java.util.*;
import java.util.stream.Collectors;

public class GraphProcessor {

    private final static int MIN_LOOP_SIZE = 2;

    private Graph graph;
    private List<List<Integer>> cycles;

    public GraphProcessor(Graph graph){
        this.graph = graph;
        this.cycles = new ArrayList<>();
    }

    public GraphProcessor findCycles(){
        cycles = getCycles();
        return this;
    }

    public String printCycles(){
        StringBuilder sb = new StringBuilder();
        cycles.forEach(cs -> sb.append(unwindCycle(cs)).append("\n"));
        return sb.toString().trim();
    }

    private String unwindCycle(List<Integer> cycle){
        StringBuilder sb = new StringBuilder();
        cycle.forEach(c -> sb.append(c).append(" "));
        return sb.toString().trim();
    }

    public List<List<Integer>> getCycles(){
        List<List<Integer>> cycles = new ArrayList<>();

        HashMap<Integer, Integer> toVisit = new HashMap<>();
        graph.getVertices().forEach(v -> toVisit.put(v, graph.getEdges(v).size()));

        Set<Integer> done = new HashSet<>();

        Iterator<Map.Entry<Integer,Integer>> iter = toVisit.entrySet().iterator();
        while (iter.hasNext()){
            Integer o = iter.next().getKey();
            if (toVisit.get(o) <= 0){
                iter.remove();
                continue;
            }
            ArrayList<Integer> cycle;
            try{
                cycle = exploreCycle(o, done);
            }catch (Exception e){
                continue;
            }

            if (cycle.size() > MIN_LOOP_SIZE){
                cycles.add(cycle);
            }

            for(int i = 0; i < cycle.size() - 1; i++){
                int cycleStep = cycle.get(i);
                Integer count = toVisit.get(cycleStep);
                count--;
                toVisit.put(cycleStep, count);
                if (count == 0){
                    done.add(cycleStep);
                }
            }
            iter = toVisit.entrySet().iterator();
        }

        return cycles;
    }

    private ArrayList<Integer> exploreCycle(Integer start, Set<Integer> done) throws Exception{
        LinkedList<Integer> stack = new LinkedList<>();
        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> cycle = new ArrayList<>();

        visited.add(start);
        stack.add(start);

        while (stack.size() > 0){
            Integer curr = stack.removeLast();
            cycle.add(curr);
            HashSet<Integer> edges = graph.getEdges(curr);
            for(Integer n: edges){
                if (done.contains(n)){
                    continue;
                }
                if (visited.contains(n)){
                    if (visited.get(0).equals(n)){
                        cycle.add(n);
                        return cycle;
                    }else {
                        throw new Exception("not a cycle");
                    }
                }
                visited.add(n);
                stack.add(n);
            }
        }
        throw new Exception("not a cycle");
    }

}
