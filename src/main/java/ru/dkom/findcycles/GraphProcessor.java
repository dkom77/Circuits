package ru.dkom.findcycles;

import java.util.*;

public class GraphProcessor {

    private final static int MIN_LOOP_SIZE = 2;

    private Graph graph;
    //private Map<DirectedGraph, LinkedList<Integer>> cycles;

    List<List<Integer>> loops;


    public GraphProcessor(Graph graph) {
        this.graph = graph;
        //this.cycles = new HashMap<>();
        this.loops = new ArrayList<>();
    }

    public GraphProcessor findCycles() {
        //findAllPath();
        //System.out.println("finding loops");

        SCC scc = new SCC(graph);

        System.out.println("finding Scc: ");
        List<DirectedGraph> components = scc.getSCCs();
        System.out.println("" + components.size() + " found");


        for (Graph c: components){
            List<List<Integer>> cycles = getCycles(c);
            loops.addAll(cycles);
        }

        return this;
    }

    public String printCycles() {
        StringBuilder sb = new StringBuilder();
        loops.forEach(cs -> sb.append(unwindCycle(cs)).append("\n"));
        //loops.forEach(c-> sb.append(c.toString()).append("\n"));

//        for(Map.Entry<DirectedGraph, LinkedList<Integer>> entry: cycles.entrySet()){
//            sb.append(unwindCycle(entry.getValue())).append("\n");
//        }

        return sb.toString().trim();
    }

    private String unwindCycle(List<Integer> path) {
        StringBuilder sb = new StringBuilder();

        path.forEach(v -> sb.append(v).append(" "));

        return sb.toString().trim();
    }

    private List<List<Integer>> getCycles(Graph graph) {
        List<List<Integer>> cycles = new ArrayList<>();
        List<List<Integer>> allPath = new ArrayList<>();

        DirectedGraph reverse = invertGraph(graph);

        for (Integer start : graph.getVertices()) {
            for (Integer dest : reverse.getEdges(start)) {
                try {
                    List<Integer> path = exploreCycle(graph, start, dest);
                    allPath.add(path);
                } catch (Exception e) {
                    //continue;
                }
            }
        }

        System.out.println("All paths explored");

        //remove dupes
        ArrayList<LinkedList<Integer>> copy = new ArrayList<>();
        for (List<Integer> ll: allPath){
            copy.add(new LinkedList<>(ll));
        }

        HashSet<List<Integer>> m = new HashSet<>();
        for (int i = 0; i < allPath.size(); i++){
            Collections.sort(copy.get(i));
            if (!m.contains(copy.get(i))){

                //allPath.get(i).add(allPath.get(i).get(0));

                cycles.add(allPath.get(i));
                m.add(copy.get(i));
            }
        }

        System.out.println("duplicated loops excluded");

        int t = 0;
        for (int i = 0; i < cycles.size(); i++) {

            List<Integer> l = cycles.get(i);
            int min = l.get(0);
            int minPos = 0;
            for (int j = 0; j < l.size(); j++){
                if (l.get(j) < min){
                    min = l.get(j);
                    minPos = j;
                }
            }


            List<Integer> norm = new ArrayList<>(l.subList(minPos, l.size()));
            norm.addAll(l.subList(0, minPos));
            norm.add(norm.get(0));

            cycles.get(i).clear();
            cycles.get(i).addAll(norm);
        }

        Collections.sort(cycles, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                for (int i = 0; i < Math.min(o1.size(), o2.size()); i++){
                    if (!o1.get(i).equals(o2.get(i))){
                        return  (o1.get(i) < o2.get(i)) ? -1 : 1;
                    }
                }

                if (o1.size() < o2.size()) {
                    return -1;
                }

                if (o1.size() < o2.size()) {
                    return 1;
                }

                return 0;
            }
        });


        int z = 0;


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
                LinkedList<Integer> path = new LinkedList<>();

                while (!curr.equals(start)) {
                    path.addFirst(curr);
                    curr = map.get(curr);
                }
                path.addFirst(start);

                return path;
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

    private DirectedGraph invertGraph(Graph g) {
        DirectedGraph reverse = new DirectedGraph();

        graph.getVertices().forEach(reverse::addVertex);
        reverse.getVertices().forEach(v -> g.getEdges(v).forEach(e -> reverse.addEdge(e, v)));
        return reverse;

    }

}