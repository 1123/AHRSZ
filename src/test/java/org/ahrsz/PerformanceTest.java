package org.ahrsz;

import org.junit.Test;

import java.util.Random;

public class PerformanceTest {

    final int MIN_NODES = 10;
    final int MIN_EDGES = 400;
    final int EDGE_STEP = 400;
    final int NODE_STEP = 1;
    final int EDGE_MEASUREMENTS = 10;
    final int NODE_MEASUREMENTS = 1;

    long [][] measurements = new long[NODE_MEASUREMENTS][EDGE_MEASUREMENTS];

    @Test
    public void test() throws InvalidExpansionStateException, InvalidAhrszStateException {
        System.err.println("Warumup");
        measureAll();
        System.err.println("Real run");
        measureAll();
    }

    private void measureAll() throws InvalidExpansionStateException, InvalidAhrszStateException {
        for (int i = 0; i < NODE_MEASUREMENTS; i++) {
            for (int j = 0; j < EDGE_MEASUREMENTS; j++) {
                int nodes = MIN_NODES + i * NODE_STEP;
                int edges = MIN_EDGES + j * EDGE_STEP;
                measurements[i][j] = this.measure(nodes, edges);
                System.err.println(String.format("Nodes: %d, Edges :%d, time : %d", nodes, edges, measurements[i][j] / 1000));
            }
        }
    }

    private long measure(int nodes, int edges) throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<>();
        Random r = new Random();
        long startTime = System.nanoTime();
        for (int i = 0; i < edges; i++) {
            float weight = r.nextFloat();
            if (weight <= FloatUtils.MIN_WEIGHT) { i--; continue; }
            ahrsz.addEdge(r.nextInt(nodes), r.nextInt(nodes), weight);
        }
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
