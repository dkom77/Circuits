package ru.dkom.findcycles;

import org.junit.Test;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.*;


public class InputProcessorTest {
    private Graph modelGraph;

    public InputProcessorTest(){
        modelGraph = new DirectedGraph();
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

        InputStream stream = new ByteArrayInputStream(sb.toString().getBytes());
        Scanner scanner = new Scanner(stream);
        InputProcessor processor = new InputProcessor(scanner);

        DirectedGraph readGraph = (DirectedGraph) processor.loadGraph(new DirectedGraph());

        assertEquals(modelGraph, readGraph);
    }

    private void initModelGraph(){
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

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