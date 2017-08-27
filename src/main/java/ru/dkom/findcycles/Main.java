package ru.dkom.findcycles;

import ru.dkom.findcycles.data.AdjListGraph;
import ru.dkom.findcycles.data.Graph;
import ru.dkom.findcycles.processors.AbstractGraphProcessor;
import ru.dkom.findcycles.processors.BruteGraphProcessor;
import ru.dkom.findcycles.utils.InputProcessor;

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

        InputProcessor loader = new InputProcessor(scanner);
        Graph graph = loader.loadGraph(new AdjListGraph());
        AbstractGraphProcessor processor = new BruteGraphProcessor();

        processor.setGraph(graph);

        System.out.println(processor.findCycles().printCycles());
    }
}
