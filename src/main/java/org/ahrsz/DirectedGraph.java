package org.ahrsz;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface DirectedGraph<N extends Comparable<N>> {

    HashMap<N, Float> getF(N key);

    HashMap<N, Float> getB(N key);

    void addEdge(N from, N to, float weight);

    void removeCycle(List<N> cycle);

    boolean hasEdge(N from, N to);

    Set<N> getForwardKeys();

    Set<N> getBackwardKeys();
}
