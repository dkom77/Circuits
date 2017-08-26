package ru.dkom.findcycles;

import java.util.*;

public class InputProcessor {

    private Scanner scanner;


    public InputProcessor(Scanner scanner){
        this.scanner = scanner;
    }

    public Graph loadGraph(Graph graph){
        Scanner lineScanner = new Scanner("");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineScanner = new Scanner(line);

            Integer mainId = readInt(lineScanner);
            Integer depId = readInt(lineScanner);

            boolean mainIdIsValid = (mainId != null) && (mainId >= 0);
            boolean depIdIsValid = (depId != null) && (depId >= 0) || (depId == null);

            if ((!mainIdIsValid)||(!depIdIsValid)||(lineScanner.hasNext())||(mainId.equals(depId))){
                System.out.println("Illegal string: \"" + line + "\" was ignored");
                continue;
            }

            graph.addVertex(mainId);
            if (depId != null){
                graph.addVertex(depId);
                graph.addEdge(mainId, depId);
            }
        }

        lineScanner.close();
        scanner.close();

        return graph;
    }


    private Integer readInt(Scanner scanner){
        try {
            return scanner.nextInt();
        }catch (NoSuchElementException nsee){
            return null;
        }
    }

}
