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
        cycles = getCycles();
        return this;
    }

    public String printCycles(){
        StringBuilder sb = new StringBuilder();
        for (List<Integer> c: cycles){
            sb.append(unwindCycle(c)).append("\n");
        }
        return sb.toString().trim();
    }

    private String unwindCycle(List<Integer> cycle){
        StringBuilder sb = new StringBuilder();
        cycle.forEach(o -> sb.append(o).append(" "));
        return sb.toString().trim();
    }

    public List<List<Integer>> getCycles(){
        List<List<Integer>> cycles = new ArrayList<>();

        HashMap<Integer, Integer> toVisit = new HashMap<>();
        for (Integer v: graph.getVertices()){
            int count = graph.getEdges(v).size();
            toVisit.put(v, count);
        }

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

            if (cycle.size() > 2){
                cycles.add(cycle);
            }

            for(int i = 0; i < cycle.size() - 1; i++){
                int c = cycle.get(i);
                Integer count = toVisit.get(c);
                count--;
                toVisit.put(c, count);
                if (count == 0){
                    done.add(c);
                }
            }
            iter = toVisit.entrySet().iterator();
        }

        return cycles;
    }

    private ArrayList<Integer> exploreCycle(Integer start, Set<Integer> done) throws Exception{
        LinkedList<Integer> stack = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
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
                //if (visited){
                    cycle.add(n);
                    return cycle;
                }
                visited.add(n);
                stack.add(n);
            }
        }
        //return new ArrayList<>();
        throw new Exception("not a cycle");
        //return cycle;
    }

}
