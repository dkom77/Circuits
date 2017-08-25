package ru.dkom.findcycles;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;


public class GraphProcessorTest {


    @Test
    public void findCycles1() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("1 2").append("\n");
        sb.append("2 1").append("\n");
        sb.append("3 4").append("\n");
        sb.append("5 6").append("\n");
        sb.append("6 5").append("\n");

        Scanner scanner = new Scanner(sb.toString());
        InputProcessor ip = new InputProcessor(scanner);
        GraphProcessor gp = new GraphProcessor(ip.loadGraph(new DirectedGraph()));
        assertEquals("1 2 1\n5 6 5", gp.findCycles().printCycles());
    }

    @Test
    public void longCycle() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("1 2").append("\n");
        sb.append("2 3").append("\n");
        sb.append("3 1").append("\n");

        Scanner scanner = new Scanner(sb.toString());
        InputProcessor ip = new InputProcessor(scanner);
        GraphProcessor gp = new GraphProcessor(ip.loadGraph(new DirectedGraph()));
        assertEquals("1 2 3 1", gp.findCycles().printCycles());
    }

    @Test
    public void multipleDependencies(){
        Graph modelGraph = new DirectedGraph();
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);

        modelGraph.addEdge(0, 1);
        modelGraph.addEdge(0, 2);

        modelGraph.addEdge(1, 3);

        modelGraph.addEdge(2, 0);

        modelGraph.addEdge(3, 0);

        GraphProcessor gp = new GraphProcessor(modelGraph);
        assertEquals("0 2 0\n0 1 3 0", gp.findCycles().printCycles());
    }

    @Test
    public void multipleDependencies2(){
        Graph modelGraph = new DirectedGraph();
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

        modelGraph.addEdge(0, 1);
        modelGraph.addEdge(0, 2);

        modelGraph.addEdge(1, 3);

        modelGraph.addEdge(2, 0);

        modelGraph.addEdge(3, 0);
        modelGraph.addEdge(3, 4);

        modelGraph.addEdge(4, 3);


        GraphProcessor gp = new GraphProcessor(modelGraph);
        assertEquals("0 2 0\n0 1 3 0\n3 4 3", gp.findCycles().printCycles());
    }

    @Test
    public void cycleInTheMiddle(){
        Graph modelGraph = new DirectedGraph();
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

        modelGraph.addEdge(0, 1);

        modelGraph.addEdge(1, 2);

        modelGraph.addEdge(2, 3);

        modelGraph.addEdge(3, 4);
        modelGraph.addEdge(3, 2);

        GraphProcessor gp = new GraphProcessor(modelGraph);
        assertEquals("2 3 2", gp.findCycles().printCycles());
    }

    @Test
    public void crossCycles(){
        Graph modelGraph = new DirectedGraph();
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);

        modelGraph.addEdge(0, 1);

        modelGraph.addEdge(1, 2);

        modelGraph.addEdge(2, 0);
        modelGraph.addEdge(2, 3);

        modelGraph.addEdge(3, 0);
        modelGraph.addEdge(3, 1);

        GraphProcessor gp = new GraphProcessor(modelGraph);
        assertEquals("0 1 2 0\n", gp.findCycles().printCycles());
    }

    @Test
    public void shouldNotFindAnyCycles(){
        Graph modelGraph = new DirectedGraph();
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

        modelGraph.addEdge(0, 1);

        modelGraph.addEdge(1, 2);

        modelGraph.addEdge(2, 3);

        modelGraph.addEdge(3, 4);

        GraphProcessor gp = new GraphProcessor(modelGraph);
        assertEquals("", gp.findCycles().printCycles());
    }






}