package ru.dkom.findcycles.processors;

import ru.dkom.findcycles.data.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class JohnsonGP extends GraphProcessor {

    private LinkedList<Integer> verticesStack;
    private Map<Integer, Set<Integer>> blocked;

    public JohnsonGP(){
        super();
    }

    @Override
    public JohnsonGP findCircuits() throws IllegalStateException {
        if (graph == null) throw new IllegalStateException("Graph not defined");

        loops = new ArrayList<>();
        List<Graph> sccs = getSCCs(graph);

        for (Graph scc: sccs){
            List<Integer> vertices = new ArrayList<>(scc.getVertices());
            vertices.sort(Integer::compare);

            vertices.forEach(v -> {
                verticesStack = new LinkedList<>();
                blocked = new HashMap<>();
                findCircuitsSCC(v, v, scc, loops);
            });
        }
        sortPaths(loops);
        return this;
    }

    private boolean findCircuitsSCC(Integer vertex, Integer root, Graph graph, List<List<Integer>> circuits){
        boolean isCircuit = false;
        verticesStack.add(vertex);
        blocked.computeIfAbsent(vertex, k -> new HashSet<>());

        List<Integer> neighbours = graph
                .getEdges(vertex)
                .stream()
                .filter(n -> n >= root)
                .collect(Collectors.toList());

        for (Integer neighbour: neighbours){
            if (neighbour.equals(root)){
                ArrayList<Integer> circuit = new ArrayList<>(verticesStack);
                circuit.add(circuit.get(0));
                circuits.add(circuit);
                isCircuit = true;
            }else{
                if (blocked.get(neighbour) == null){
                    if (findCircuitsSCC(neighbour, root, graph, circuits)){
                        isCircuit = true;
                    }
                }
            }
        }

        if (isCircuit){
            unblock(vertex);
        }else{
            for (Integer neighbour: neighbours){
                Set<Integer> dependants = blocked.get(neighbour);
                if (dependants != null){
                    dependants.add(vertex);
                    blocked.put(neighbour, dependants);
                }
            }
        }

        verticesStack.remove(vertex);
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
