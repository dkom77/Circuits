package ru.dkom.findcycles;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class GraphTest {

    private Graph graph;

    @Before
    public void setup(){
        graph = new AdjListGraph();
    }

    @Test
    public void addVertex() throws Exception {
        graph.addVertex(0);
        Set<Integer> v = graph.getVertices();
        assertEquals("should contain 1 entry", 1, v.size());
        assertEquals("vertex id should be 0", 0, v.toArray()[0]);
        assertTrue("graph should contain new vertex", graph.containsVertex(0));
        assertFalse("graph should not contain unknown vertex", graph.containsVertex(100));

    }

    @Test
    public void addEdge() throws Exception {
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge(0, 1);

        Set<Integer> e = graph.getEdges(0);
        assertEquals("should contain 1 entry", 1, e.size());
        assertEquals("destination vertex id should be 1", 1, e.toArray()[0]);

    }



}