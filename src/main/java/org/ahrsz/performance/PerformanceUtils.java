package org.ahrsz.performance;

import org.ahrsz.*;

import java.util.Random;

public class PerformanceUtils {

    public static long measure(int nodes, int edges, float minWeight) throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new MinWeightHashMapGraph<>(minWeight));
        long startTime = System.nanoTime();
        for (int i = 0; i < edges; i++) {
            float weight = (float) Math.random();
            if (weight <= FloatUtils.MIN_WEIGHT) { i--; continue; }
            ahrsz.addEdge((int) Math.round(Math.random() * nodes), (int) Math.round(Math.random() * nodes), weight);
        }
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
