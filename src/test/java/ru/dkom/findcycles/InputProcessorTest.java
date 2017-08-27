package ru.dkom.findcycles;

import org.junit.Test;
import ru.dkom.findcycles.data.AdjListGraph;
import ru.dkom.findcycles.data.Graph;
import ru.dkom.findcycles.utils.InputProcessor;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;


public class InputProcessorTest {
    private Graph modelGraph;

    public InputProcessorTest(){
        modelGraph = new AdjListGraph();
        initModelGraph();
    }

    @Test
    public void loadedGraphShouldMatchModel() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("0 1").append("\n");
        sb.append("0 2").append("\n");
        sb.append("0 3").append("\n");
        sb.append("1 0").append("\n");
        sb.append("1 2").append("\n");
        sb.append("1 3").append("\n");
        sb.append("2 1").append("\n");
        sb.append("2 3").append("\n");
        sb.append("2 4").append("\n");
        sb.append("3 0").append("\n");
        sb.append("4 2").append("\n");
        sb.append("5").append("\n");
        sb.append(" 6").append("\n");

        Scanner scanner = new Scanner(sb.toString());
        InputProcessor processor = new InputProcessor(scanner);

        AdjListGraph readGraph = (AdjListGraph) processor.loadGraph(new AdjListGraph());

        assertEquals(modelGraph, readGraph);
    }

    @Test
    public void stringsContainingIllegalSymbolsShouldBeIgnored() throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(" ").append("\n");
        sb.append("1.2 ").append("\n");
        sb.append("-100 ").append("\n");
        sb.append(" a").append("\n");
        sb.append("18 a").append("\n");
        sb.append("-4 2").append("\n");
        sb.append("2 -4").append("\n");
        sb.append("10 10 100").append("\n");
        sb.append("3 3").append("\n");

        InputStream stream = new ByteArrayInputStream(sb.toString().getBytes());
        Scanner scanner = new Scanner(stream);
        InputProcessor processor = new InputProcessor(scanner);

        AdjListGraph readGraph = (AdjListGraph) processor.loadGraph(new AdjListGraph());

        assertEquals(new AdjListGraph(), readGraph);
    }



    private void initModelGraph(){
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);
        modelGraph.addVertex(5);
        modelGraph.addVertex(6);

        modelGraph.addEdge(0, 1);
        modelGraph.addEdge(0, 2);
        modelGraph.addEdge(0, 3);

        modelGraph.addEdge(1, 0);
        modelGraph.addEdge(1, 2);
        modelGraph.addEdge(1, 3);

        modelGraph.addEdge(2, 1);
        modelGraph.addEdge(2, 3);
        modelGraph.addEdge(2, 4);

        modelGraph.addEdge(3, 0);

        modelGraph.addEdge(4, 2);
    }

}