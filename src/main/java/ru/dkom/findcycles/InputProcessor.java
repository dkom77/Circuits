package ru.dkom.findcycles;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class InputProcessor {

    private Scanner scanner;
    private Scanner lineScanner;

    public InputProcessor(Scanner scanner){
        this.scanner = scanner;
    }

    public Graph loadGraph(Graph graph){
        Set<Integer> seen = new HashSet<Integer>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineScanner = new Scanner(line);

            Integer mainEntity;
            Integer dependsOn;

            boolean mainIdIsValid;
            boolean dependsIdIsValid;

            try {
                mainEntity = readInt();
                mainIdIsValid = (mainEntity!= null) && (mainEntity >= 0);
            }catch (NoSuchElementException nsee){
                continue;
            }

            try {
                dependsOn = readInt();
                dependsIdIsValid = ((dependsOn!= null) && (dependsOn >= 0)) || (dependsOn == null);
            }catch (NoSuchElementException nsee){
                continue;
            }

            if ((lineScanner.hasNext())||(!mainIdIsValid)||(!dependsIdIsValid)){
                System.out.println("Illegal string: \"" + line + "\" was ignored");
                //continue;
            }

            //graph.addVertex(mainEntity);

            //if (dependsOn == null)

            //graph.addVertex(dependsOn);

            if (!addVertex(graph, seen, mainEntity)){
                continue;
            }

            if (!addVertex(graph, seen, dependsOn)){
                continue;
            }

            graph.addEdge(mainEntity, dependsOn);
        }

        lineScanner.close();
        scanner.close();

        return graph;
    }

    private boolean addVertex(Graph graph, Set<Integer> seen, Integer id){
        if (id == null) return false;
        if (!seen.contains(id)) {
            graph.addVertex(id);
            seen.add(id);
        }
        return true;
    }

    private Integer readInt(){
        try {
            return lineScanner.nextInt();
        }catch (NoSuchElementException nsee){
            return null;
        }
    }

}
