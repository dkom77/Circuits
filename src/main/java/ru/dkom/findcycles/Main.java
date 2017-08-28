package ru.dkom.findcycles;

import ru.dkom.findcycles.data.AdjListGraph;
import ru.dkom.findcycles.data.Graph;
import ru.dkom.findcycles.processors.GraphProcessor;
import ru.dkom.findcycles.processors.SimpleGP;
import ru.dkom.findcycles.utils.GraphLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner;
        try{
            scanner = (args.length == 0) ? new Scanner(System.in) : new Scanner(new File(args[0]));
        }catch (FileNotFoundException e){
            System.out.println("failed to read file");
            return;
        }

        GraphLoader loader = new GraphLoader(scanner);
        Graph graph = loader.loadGraph(new AdjListGraph());
        GraphProcessor processor = new SimpleGP();
        //GraphProcessor processor = new AdvancedGP();

        processor.setGraph(graph);

        System.out.println(processor.findCycles().printCycles());
    }
}
