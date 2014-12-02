
Implementation of the AHRSZ algorithm, for maintaining a topological order upon
insertion of edges as described in

"A Dynamic Algorithm for Topologically Sorting Directed Acyclic Graphs" by David J. Pearce, Paul H. J. Kelly .

In contrast to the algorithm described in the paper, this implementation also
deals with cycles. When a new edge is inserted that completes a cycle in the
graph, then all edges composing the cycle are diminished by the weight of the
minimum edge of the cycle. This way the cycle is removed. Note that this
algorithm is not deterministic. A newly inserted edge may introduce multiple
cycles at once. In this case it is up to the implementation to chose the order
of removing the cycles. This may lead to different remaining graphs.


