package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.Graph;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;

public class AdvancedGP extends GraphProcessor {

    private LinkedList<Integer> vertStack;
    //Set<Integer> blockedVertices;
    Map<Integer, Set<Integer>> blocked;



    public AdvancedGP(){
        super();
    }

    @Override
    public AdvancedGP findCycles() throws IllegalStateException {
        if (graph == null) throw new IllegalStateException("Graph not defined");

        loops = new ArrayList<>();

        List<Graph> sccs = getSCCs(graph);

        for (Graph scc: sccs){



            List<List<Integer>> circuits = new ArrayList<>();

            findCircuitsInSCC(scc, circuits);



            int t = 0;


            loops.addAll(circuits);
        }

//        loops = normaliseRoutes(loops);
        sortPaths(loops);


        return this;
    }

    private void findCircuitsInSCC(Graph scc, List<List<Integer>> circuits){
        List<Integer> vertices = new ArrayList<>(scc.getVertices());
        vertices.sort(Integer::compare);

        //Integer root = vertices.get(0);

        for (int i = 0; i < vertices.size(); i++){

            Integer curr = vertices.get(i);

//            Set<Integer> visited = new HashSet<>();
//            Set<Integer> finished = new HashSet<>();
//
//            visit(scc, curr, visited, finished);

            vertStack = new LinkedList<>();
            blocked = new HashMap<>();

            findCircuits(curr, curr, scc, circuits);

            int t = 0;
        }


//        Set<Integer> visited = new HashSet<>();
//        visited.add(root);



    }


    private boolean findCircuits(Integer vertex, Integer root, Graph graph, List<List<Integer>> circuits){
        boolean isCircuit = false;

        vertStack.add(vertex);
        //blockedVertices.add(vertex);
        blocked.computeIfAbsent(vertex, k -> new HashSet<>());

        HashSet<Integer> neighbours = graph.getEdges(vertex);

        for (Integer neighbour: neighbours){
            if (neighbour < root){
                continue;
            }
            if (neighbour.equals(root)){
                ArrayList<Integer> circuit = new ArrayList<>(vertStack);
                circuit.add(circuit.get(0));
                circuits.add(circuit);
                isCircuit = true;
            }else{
                if (blocked.get(neighbour) == null){
                    if (findCircuits(neighbour, root, graph, circuits)){
                        isCircuit = true;
                    };
                }
            }
        }

        if (isCircuit){
            unblock(vertex);
        }else{
            for (Integer neighbour: neighbours){
                Set<Integer> dep = blocked.get(neighbour);
                if (dep != null){
                    dep.add(vertex);
                    blocked.put(neighbour, dep);
                }
            }
        }

        vertStack.remove(vertex);
        return isCircuit;
    }

    private void unblock(int vertex){
        Set<Integer> blockedBy = blocked.get(vertex);
        if (blockedBy == null){
            return;
        }
        blocked.remove(vertex);
        blockedBy.forEach(this::unblock);
    }

}
