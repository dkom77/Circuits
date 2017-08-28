package ru.dkom.findcycles;

import org.junit.Before;
import org.junit.Test;
import ru.dkom.findcycles.data.AdjListGraph;
import ru.dkom.findcycles.data.Graph;
import ru.dkom.findcycles.processors.GraphProcessor;
import ru.dkom.findcycles.processors.AdvancedGP;
import ru.dkom.findcycles.processors.SimpleGP;

import static org.junit.Assert.assertEquals;


public class GraphProcessorTest {

    private GraphProcessor gp;
    private Graph modelGraph;

    @Before
    public void setUp(){
        this.gp = new SimpleGP();
        //this.gp = new AdvancedGP();
        this.modelGraph = new AdjListGraph();
    }


    @Test
    public void findCycles1() throws Exception {
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);
        modelGraph.addVertex(5);
        modelGraph.addVertex(6);

        modelGraph.addEdge(1,2);

        modelGraph.addEdge(2,1);

        modelGraph.addEdge(3,4);

        modelGraph.addEdge(5,6);

        modelGraph.addEdge(6, 5);

        gp.setGraph(modelGraph);

        assertEquals("1 2 1\n5 6 5", gp.findCycles().printCycles());
    }

    @Test
    public void longCycle() throws Exception {
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

        modelGraph.addEdge(1,2);

        modelGraph.addEdge(2,3);

        modelGraph.addEdge(3,4);

        modelGraph.addEdge(4,1);

        gp.setGraph(modelGraph);
        assertEquals("1 2 3 4 1", gp.findCycles().printCycles());
    }

    @Test
    public void multipleDependencies(){
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);

        modelGraph.addEdge(0, 1);
        modelGraph.addEdge(0, 2);

        modelGraph.addEdge(1, 3);

        modelGraph.addEdge(2, 0);

        modelGraph.addEdge(3, 0);

        gp.setGraph(modelGraph);
        assertEquals("0 1 3 0\n0 2 0", gp.findCycles().printCycles());
    }

    @Test
    public void multipleDependencies2(){
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

        gp.setGraph(modelGraph);
        assertEquals("0 1 3 0\n0 2 0\n3 4 3", gp.findCycles().printCycles());
    }

    @Test
    public void cycleInTheMiddle(){
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

        gp.setGraph(modelGraph);
        assertEquals("2 3 2", gp.findCycles().printCycles());
    }

    @Test
    public void crossCycles(){
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

        gp.setGraph(modelGraph);
        assertEquals("0 1 2 0\n0 1 2 3 0\n1 2 3 1", gp.findCycles().printCycles());
    }

    @Test
    public void innerLoops(){
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

        modelGraph.addEdge(0, 1);

        modelGraph.addEdge(1, 2);
        modelGraph.addEdge(1, 3);

        modelGraph.addEdge(2, 4);

        modelGraph.addEdge(3, 4);

        modelGraph.addEdge(4, 0);

        gp.setGraph(modelGraph);
        assertEquals("0 1 2 4 0\n0 1 3 4 0", gp.findCycles().printCycles());
    }

    @Test
    public void innerLoops2(){
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

        modelGraph.addEdge(1, 3);
        modelGraph.addEdge(1, 2);

        modelGraph.addEdge(2, 1);
        modelGraph.addEdge(2, 4);

        modelGraph.addEdge(3, 4);

        modelGraph.addEdge(4, 2);

        gp.setGraph(modelGraph);

        assertEquals("1 2 1\n1 3 4 2 1\n2 4 2", gp.findCycles().printCycles());
    }

    @Test
    public void scc(){
        modelGraph.addVertex(18);
        modelGraph.addVertex(23);
        modelGraph.addVertex(25);
        modelGraph.addVertex(32);
        modelGraph.addVertex(44);
        modelGraph.addVertex(50);
        modelGraph.addVertex(65);

        modelGraph.addEdge(18, 23);
        modelGraph.addEdge(18, 44);

        modelGraph.addEdge(23, 18);
        modelGraph.addEdge(23, 25);

        modelGraph.addEdge(25, 23);
        modelGraph.addEdge(25, 65);

        modelGraph.addEdge(32, 44);
        modelGraph.addEdge(32, 50);

        modelGraph.addEdge(44, 50);

        modelGraph.addEdge(65, 23);

        gp.setGraph(modelGraph);

        assertEquals("18 23 18\n23 25 23\n23 25 65 23", gp.findCycles().printCycles());
    }

    @Test
    public void shouldNotFindAnyCycles(){
        modelGraph.addVertex(0);
        modelGraph.addVertex(1);
        modelGraph.addVertex(2);
        modelGraph.addVertex(3);
        modelGraph.addVertex(4);

        modelGraph.addEdge(0, 1);

        modelGraph.addEdge(1, 2);

        modelGraph.addEdge(2, 3);

        modelGraph.addEdge(3, 4);

        gp.setGraph(modelGraph);
        assertEquals("", gp.findCycles().printCycles());
    }






}