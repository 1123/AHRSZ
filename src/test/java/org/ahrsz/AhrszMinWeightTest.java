package org.ahrsz;

import org.junit.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class AhrszMinWeightTest {

    @Test
    public void testSingleEdge() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new MinWeightHashMapGraph<>(1.0f));
        ahrsz.addEdge(1,2,1.1f);
        assert(ahrsz.before(1,2));
    }

    /**
     * A->B   C->D
     * ^------+
     */

    @Test
    public void testReorder() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Character> ahrsz = new AhrszAlgorithm<Character>(new MinWeightHashMapGraph<>(1.0f));
        ahrsz.addEdge('A','B',1.1f);
        ahrsz.addEdge('C','D',1.1f);
        ahrsz.addEdge('C','A',1.1f);
        assertTrue(ahrsz.before('A','B'));
        assertTrue(ahrsz.before('C','D'));
        assertTrue(ahrsz.before('C','A'));
    }

    @Test
    public void testSingleNodeCycle() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new MinWeightHashMapGraph<Integer>(1.0f));
        ahrsz.addEdge(1, 1, 1.1f);
    }

    @Test
    public void testComplexGraph() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Character> ahrsz = new AhrszAlgorithm<Character>(new MinWeightHashMapGraph<>(1.0f));
        ahrsz.addEdge('A','B',1.1f);
        ahrsz.addEdge('C','E',1.1f);
        ahrsz.addEdge('E','F',1.1f);
        ahrsz.addEdge('G', 'F', 1.1f);
        assertTrue(ahrsz.before('A', 'B'));
        assertTrue(ahrsz.before('C', 'E'));
        assertTrue(ahrsz.before('E', 'F'));
        assertTrue(ahrsz.before('G', 'F'));
        assertTrue(ahrsz.before('G', 'A'));
        ahrsz.addEdge('E', 'A', 1.1f);
        assertTrue(ahrsz.before('G', 'C'));
        assertTrue(ahrsz.before('C', 'E'));
        assertTrue(ahrsz.before('E', 'A'));
        assertTrue(ahrsz.before('A', 'B'));
        assertTrue(ahrsz.before('B', 'F'));
    }

    @Test
    public void testMultipleNodeCycle() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Character> ahrsz = new AhrszAlgorithm<Character>(new MinWeightHashMapGraph<Character>(0.05f));
        ahrsz.addEdge('A', 'B', 1.2f);
        ahrsz.addEdge('B', 'C', 1.4f);
        ahrsz.addEdge('C', 'D', 1.3f);
        assertTrue(ahrsz.before('A', 'D'));
        ahrsz.addEdge('D', 'A', 1.5f);
        assertTrue(ahrsz.before('B', 'C'));
        assertTrue(ahrsz.before('C','D'));
        assertTrue(ahrsz.before('D', 'A'));
    }


    @Test
    public void testSwitchPositions2() throws InvalidAhrszStateException {
        Set<Integer> shiftUp = new TreeSet<>(Arrays.asList(10,15));
        Set<Integer> shiftDown = new TreeSet<>(Arrays.asList(2,4,5,19));
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new MinWeightHashMapGraph<Integer>(0.1f));
        ahrsz.node2Index.put(15,-4);
        ahrsz.node2Index.put(5,-3);
        ahrsz.node2Index.put(4,0);
        ahrsz.node2Index.put(10,2);
        ahrsz.node2Index.put(19,3);
        ahrsz.node2Index.put(2,4);
        ahrsz.switchPositions(shiftUp, shiftDown);
        assertTrue(ahrsz.node2Index.get(5) == -4);
        assertTrue(ahrsz.node2Index.get(4) == -3);
        assertTrue(ahrsz.node2Index.get(19) == 0);
        assertTrue(ahrsz.node2Index.get(2) == 2);
        assertTrue(ahrsz.node2Index.get(15) == 3);
        assertTrue(ahrsz.node2Index.get(10) == 4);
    }

}


