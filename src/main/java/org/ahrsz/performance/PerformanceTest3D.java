package org.ahrsz.performance;

import org.ahrsz.InvalidAhrszStateException;
import org.ahrsz.InvalidExpansionStateException;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerformanceTest3D {

    static final int MIN_NODES = 20;
    static final int MIN_EDGES = 200;
    static final int EDGE_STEP = 200;
    static final int NODE_STEP = 10;
    static final int EDGE_MEASUREMENTS = 10;
    static final int NODE_MEASUREMENTS = 10;
    static final float MIN_WEIGHT = 10.0f;

    float [][] measurements = new float[NODE_MEASUREMENTS][EDGE_MEASUREMENTS];

    @Test
    public void test() throws InvalidExpansionStateException, InvalidAhrszStateException {
        System.err.println("Warumup");
        measureAll(5);
        System.err.println("Real run");
        measureAll(10);
    }

    private void measureAll(int numberOfMeasurements) throws InvalidExpansionStateException, InvalidAhrszStateException {
        for (int i = 0; i < NODE_MEASUREMENTS; i++) {
            for (int j = 0; j < EDGE_MEASUREMENTS; j++) {
                for (int m = 0; m < numberOfMeasurements; m++) {
                    int nodes = MIN_NODES + i * NODE_STEP;
                    int edges = MIN_EDGES + j * EDGE_STEP;
                    measurements[i][j] = (float) (PerformanceUtils.measure(nodes, edges, MIN_WEIGHT) / 1000000000.0);
                    System.err.println(String.format("Nodes: %d, Edges :%d, time : %f", nodes, edges, measurements[i][j]));
                }
                measurements[i][j] /= numberOfMeasurements;
            }
        }
        System.err.println(serializeToR(measurements));
    }

    /**
     * print the measurements as an R 3d plot.
     */
     public static String serializeToR(float[][] measurement) {
            VelocityContext context = new VelocityContext();
            context.put("values", matrixToString(measurement));
            context.put("cols", NODE_MEASUREMENTS);
            context.put("edgeStep", EDGE_STEP);
            context.put("nodeStep", NODE_STEP);
            StringWriter w = new StringWriter();
            Template t = Context.getVelocityEngine().getTemplate("src/test/java/org/ahrsz/performance.vt");
            t.merge(context, w);
            return w.toString();
    }

    public static String matrixToString(float [][] measurement) {
        List<String> lines = new ArrayList<>();
        for (float[] row : Arrays.asList(measurement)) {
            List<String> elements = new ArrayList<>();
            for (int j = 0; j < measurement[0].length; j++) {
                elements.add(row[j] + "");
            }
            lines.add(StringUtils.join(elements, ","));
        }
        return StringUtils.join(lines, ", " + System.getProperty("line.separator"));
    }

}

