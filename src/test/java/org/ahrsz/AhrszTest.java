package org.ahrsz;

import org.junit.Test;

import java.lang.Character;import java.lang.Integer;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.TestCase.assertTrue;

public class AhrszTest {

    @Test
    public void testSingleEdge() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new HashMapGraph<Integer>());
        ahrsz.addEdge(1,2,0.1f);
        assert(ahrsz.before(1,2));
    }

    /**
     * A->B   C->D
     * ^------+
     */

    @Test
    public void testReorder() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Character> ahrsz = new AhrszAlgorithm<Character>(new HashMapGraph<Character>());
        ahrsz.addEdge('A','B',0.1f);
        ahrsz.addEdge('C','D',0.1f);
        ahrsz.addEdge('C', 'A', 0.1f);
        assertTrue(ahrsz.before('A','B'));
        assertTrue(ahrsz.before('C','D'));
        assertTrue(ahrsz.before('C', 'A'));
    }

    @Test
    public void testSingleNodeCycle() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new HashMapGraph<Integer>());
        ahrsz.addEdge(1, 1, 0.1f);
    }

    @Test
    public void testComplexGraph() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Character> ahrsz = new AhrszAlgorithm<Character>(new HashMapGraph<Character>());
        ahrsz.addEdge('A','B',0.1f);
        ahrsz.addEdge('C','E',0.1f);
        ahrsz.addEdge('E','F',0.1f);
        ahrsz.addEdge('G', 'F', 0.1f);
        assertTrue(ahrsz.before('A', 'B'));
        assertTrue(ahrsz.before('C', 'E'));
        assertTrue(ahrsz.before('E', 'F'));
        assertTrue(ahrsz.before('G', 'F'));
        assertTrue(ahrsz.before('G', 'A'));
        ahrsz.addEdge('E', 'A', 0.1f);
        assertTrue(ahrsz.before('G', 'C'));
        assertTrue(ahrsz.before('C', 'E'));
        assertTrue(ahrsz.before('E', 'A'));
        assertTrue(ahrsz.before('A', 'B'));
        assertTrue(ahrsz.before('B', 'F'));
    }

    @Test
    public void testMultipleNodeCycle() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new HashMapGraph<Integer>());
        ahrsz.addEdge(1, 2, 0.2f);
        ahrsz.addEdge(2, 3, 0.4f);
        ahrsz.addEdge(3, 4, 0.3f);
        assertTrue(ahrsz.before(1, 4));
        ahrsz.addEdge(4, 1, 0.5f);
        assertTrue(ahrsz.before(2, 3));
        assertTrue(ahrsz.before(3,4));
        assertTrue(ahrsz.before(4, 1));
    }


    @Test
    public void testSwitchPositions() throws InvalidExpansionStateException, InvalidAhrszStateException {
        Set<Integer> shiftUp = new TreeSet<>(Arrays.asList(1));
        Set<Integer> shiftDown = new TreeSet<>(Arrays.asList(0,5,8));
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new HashMapGraph<Integer>());
        ahrsz.addEdge(2,1,1f);
        ahrsz.addEdge(0,8,1f);
        ahrsz.addEdge(8,5,1f);
        assertTrue(ahrsz.before(2,1));
        assertTrue(ahrsz.before(0,8));
        assertTrue(ahrsz.before(8,5));
        ahrsz.switchPositions(shiftUp, shiftDown);
        assertTrue(ahrsz.before(0,8));
        assertTrue(ahrsz.before(5,1));
        assertTrue(ahrsz.before(8,5));
    }

    @Test
    public void testSwitchPositions2() throws InvalidAhrszStateException {
        Set<Integer> shiftUp = new TreeSet<>(Arrays.asList(10,15));
        Set<Integer> shiftDown = new TreeSet<>(Arrays.asList(2,4,5,19));
        AhrszAlgorithm<Integer> ahrsz = new AhrszAlgorithm<Integer>(new HashMapGraph<Integer>());
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


