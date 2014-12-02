Project summary
---------------

Java Implementation of the AHRSZ algorithm, for maintaining a topological order of a directed graph upon
insertion of edges as described in

"A Dynamic Algorithm for Topologically Sorting Directed Acyclic Graphs" by David J. Pearce, Paul H. J. Kelly .

http://www.doc.ic.ac.uk/~phjk/Publications/DynamicTopoSortAlg-JEA-07.pdf

In contrast to the algorithm described in the paper, this implementation also
deals with cycles. When a new edge is inserted that completes a cycle in the
graph, then all edges composing the cycle are diminished by the weight of the
minimum edge of the cycle. This way the cycle is removed. Note that this
algorithm is not deterministic. A newly inserted edge may introduce multiple
cycles at once. In this case it is up to the implementation to chose the order
of removing the cycles. This may lead to different remaining graphs.

Usage
-----

1. clone this repository
2. install jar via maven : ``mvn install``
3. add the jar as a dependency to your project:
```maven
<dependency>
  <groupId>AHRSZ</groupId>
  <artifactId>AHRSZ</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```
4. Use it as in the test cases of this project:
```java
    @Test
    public void testComplexGraph() throws InvalidExpansionStateException, InvalidAhrszStateException {
        AhrszAlgorithm<Character> ahrsz = new AhrszAlgorithm<>();
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
```
