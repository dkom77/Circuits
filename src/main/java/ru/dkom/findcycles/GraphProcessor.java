package ru.dkom.findcycles;

import java.lang.reflect.Constructor;
import java.util.*;

public class GraphProcessor {

    private final static int MIN_LOOP_SIZE = 2;

    private Graph graph;
    List<List<Integer>> loops;


    public GraphProcessor(Graph graph) {
        this.graph = graph;
        this.loops = new ArrayList<>();
    }

    public GraphProcessor findCycles() {
        //findAllPath();
        //System.out.println("finding loops");

        SCC scc = new SCC(graph);

        System.out.println("finding Scc: ");
        List<Graph> components = scc.getSCCs();
        System.out.println("" + components.size() + " found");


        for (Graph c : components) {
            List<List<Integer>> cycles = getCycles(c);
            //List<List<Integer>> cycles = getCycles2(c);
            loops.addAll(cycles);
        }

        return this;
    }

    public String printCycles() {
        StringBuilder sb = new StringBuilder();
        loops.forEach(cs -> sb.append(unwindCycle(cs)).append("\n"));
        return sb.toString().trim();
    }

    private String unwindCycle(List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        path.forEach(v -> sb.append(v).append(" "));
        return sb.toString().trim();
    }


    private List<List<Integer>> getCycles2(Graph graph) {
        List<List<Integer>> cycles = new ArrayList<>();

        ArrayList<Integer> visited = new ArrayList<>();
        List<Integer> vertices = new ArrayList<>(graph.getVertices());

        Collections.sort(vertices);

        for (Integer cur: vertices){
            try {
                List<Integer> path = exploreCycle2(graph, cur);
                path.add(path.get(0));
                cycles.add(path);
            } catch (Exception e) {

            }
        }

        return cycles;
    }

    private List<Integer> exploreCycle2(Graph graph, Integer start) throws Exception {
        LinkedList<Integer> stack = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        HashMap<Integer, Integer> map = new HashMap<>();

        boolean doNotCheck = true;
        //visited.add(start);

        stack.add(start);
        stack.add(start);

        while (stack.size() > 0) {
            Integer curr = stack.removeLast();

            if (curr.equals(start) && (!doNotCheck)) {
                LinkedList<Integer> path = new LinkedList<>();
                do {
                    path.addFirst(curr);
                    curr = map.get(curr);
                } while (!curr.equals(start));
                //path.addFirst(start);
                return path;
            }

            doNotCheck = false;
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
                    //continue;
                }
            }
        }

        System.out.println("All paths explored");

        //remove dupes
        ArrayList<LinkedList<Integer>> copy = new ArrayList<>();
        for (List<Integer> ll : allPath) {
            copy.add(new LinkedList<>(ll));
        }

        HashSet<List<Integer>> m = new HashSet<>();
        for (int i = 0; i < allPath.size(); i++) {
            Collections.sort(copy.get(i));
            if (!m.contains(copy.get(i))) {

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
            for (int j = 0; j < l.size(); j++) {
                if (l.get(j) < min) {
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
                for (int i = 0; i < Math.min(o1.size(), o2.size()); i++) {
                    if (!o1.get(i).equals(o2.get(i))) {
                        return (o1.get(i) < o2.get(i)) ? -1 : 1;
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

    private Graph invertGraph(Graph graph){
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