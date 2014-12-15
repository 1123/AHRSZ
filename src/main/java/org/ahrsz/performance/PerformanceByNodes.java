package org.ahrsz.performance;

import org.ahrsz.InvalidAhrszStateException;
import org.ahrsz.InvalidExpansionStateException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

import java.io.StringWriter;

public class PerformanceByNodes {

    static final int MIN_NODES = 10;
    static final int NODE_STEP = 1;
    static final int NODE_MEASUREMENTS = 30;
    static final int EDGES = 62000;
    static final int REPEAT = 15;
    static final float MIN_WEIGHT = 10.0f;

    float [] measurements = new float[NODE_MEASUREMENTS];

    @Test
    public void test() throws InvalidExpansionStateException, InvalidAhrszStateException {
        System.err.println("Warmup");
        measureAll(REPEAT);
        System.err.println("Real run");
        measureAll(REPEAT);
    }

    private void measureAll(int numberOfMeasurements) throws InvalidExpansionStateException, InvalidAhrszStateException {
        for (int i = 0; i < NODE_MEASUREMENTS; i++) {
            measurements[i] = 0;
            int nodes = MIN_NODES + i * NODE_STEP;
            for (int m = 0; m < numberOfMeasurements; m++) {
                measurements[i] += (float) (PerformanceUtils.measure(nodes, EDGES, MIN_WEIGHT) / 1000000.0);
            }
            measurements[i] = measurements[i] / (numberOfMeasurements + 0.0f);
            System.err.println(String.format("Nodes: %d, time : %f", nodes, measurements[i]));
        }
        System.err.println(serializeToR(measurements));
    }

    /**
     * print the measurements as an R 3d plot.
     */
     public static String serializeToR(float[] measurements) {
            VelocityContext context = new VelocityContext();
            StringWriter w = new StringWriter();
            context.put("yValues", yValues(measurements));
            context.put("xValues", xValues());
            context.put("insertions", EDGES);
            context.put("minWeight", MIN_WEIGHT);
            Template t = Context.getVelocityEngine().getTemplate("src/main/java/org/ahrsz/performance/performanceByNodes.vt");
            t.merge(context, w);
            return w.toString();
    }

    private static String yValues(float[] measurements) {
        StringBuilder builder = new StringBuilder();
        builder.append(measurements[0]);
        for (int i = 1; i < measurements.length; i++) {
            builder.append(", " + measurements[i]);
        }
        return builder.toString();
    }


    private static String xValues() {
        StringBuilder builder = new StringBuilder();
        builder.append(MIN_NODES);
        for (int i = 1; i <NODE_MEASUREMENTS; i++) {
            builder.append(", " + (MIN_NODES + i * NODE_STEP));
        }
        return builder.toString();
    }
}

