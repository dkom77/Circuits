package ru.dkom.findcycles.utils;

import ru.dkom.findcycles.data.Graph;

import java.util.*;

public class GraphLoader {

    private Scanner scanner;


    public GraphLoader(Scanner scanner){
        this.scanner = scanner;
    }

    public Graph loadGraph(Graph graph){
        Scanner lineScanner = new Scanner("");

        System.out.println("reading file");
        int counter = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("")){
                break;
            }

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
            counter++;
        }

        lineScanner.close();
        scanner.close();

        System.out.println("Graph loaded " + counter + " lines read. Graph vertices amount: " + graph.getVertices().size());

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
