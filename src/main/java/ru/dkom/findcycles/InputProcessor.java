package ru.dkom.findcycles;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class InputProcessor {

    private Scanner scanner;

    public InputProcessor(Scanner scanner){
        this.scanner = scanner;
    }

    public Graph loadGraph(Graph graph){
        Set<Integer> seen = new HashSet<Integer>();
        while (scanner.hasNextInt()) {
            int v1 = scanner.nextInt();
            int v2 = scanner.nextInt();
            if (!seen.contains(v1)) {
                graph.addVertex(v1);
                seen.add(v1);
            }
            if (!seen.contains(v2)) {
                graph.addVertex(v2);
                seen.add(v2);
            }
            graph.addEdge(v1, v2);
        }

        scanner.close();
        return graph;
    }

//    public String echo(){
//        StringBuilder sb = new StringBuilder();
//        while(scanner.hasNextLine()) {
//            sb.append(scanner.nextLine()).append("\n");
//        }
//        return sb.toString();
//
//    }

}
