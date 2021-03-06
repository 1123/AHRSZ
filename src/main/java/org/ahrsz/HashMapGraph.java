package org.ahrsz;


import java.lang.Comparable;
import java.lang.Float;
import java.lang.RuntimeException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HashMapGraph<N extends Comparable<N>> implements DirectedGraph<N> {

    public HashMap<N, HashMap<N, Float>> forward;
    public HashMap<N, HashMap<N, Float>> backward;

    public HashMapGraph() {
        this.forward = new HashMap<>();
        this.backward = new HashMap<>();
    }

    /**
     * This method returns all outgoing edges together with its weights for a given node.
     */

    @Override
    public HashMap<N, Float> getF(N key) {
        return this.forward.get(key);
    }

    @Override
    public HashMap<N, Float> getB(N key) {
        return this.backward.get(key);
    }

    private void addForwardEdge(N from, N to, Float weight) {
        if (forward.containsKey(from)) {
            if (forward.get(from).containsKey(to)) {
                float original = forward.get(from).get(to);
                forward.get(from).put(to, original + weight);
            } else {
                forward.get(from).put(to, weight);
            }
        } else {
            HashMap<N, Float> outgoing = new HashMap<>();
            outgoing.put(to, weight);
            forward.put(from, outgoing);
        }
    }

    private void addBackwardEdge(N from, N to, Float weight) {
        if (backward.containsKey(to)) {
            if (backward.get(to).containsKey(from)) {
                float original = backward.get(to).get(from);
                backward.get(to).put(from, original + weight);
            } else {
                backward.get(to).put(from, weight);
            }
        } else {
            HashMap<N, Float> incoming = new HashMap<>();
            incoming.put(from, weight);
            backward.put(to, incoming);
        }
    }

    @Override
    public void addEdge(N from, N to, float weight) {
        if (weight < 0f) throw new RuntimeException("Negative weights are not supported.");
        this.addForwardEdge(from, to, weight);
        this.addBackwardEdge(from, to, weight);
    }

    /**
     * Removes a cycle from the directed graph.
     * This works by finding the minimum weight of the edges within the cycle,
     * and then decreasing each edge of the cycle by the minimum weight.
     * @param cycle the cycle to be removed.
     */

    @Override
    public void removeCycle(List<N> cycle) {
        N minFrom = cycle.get(cycle.size() - 1);
        N minTo = cycle.get(0);
        float minWeight = forward.get(minFrom).get(minTo);
        for (int i = 0; i < cycle.size() - 1; i++) {
            N from = cycle.get(i);
            N to = cycle.get(i + 1);
            float weight = forward.get(from).get(to);
            if (weight < minWeight) {
                minWeight = weight;
            }
        }
        decreaseCycle(cycle, minWeight);
    }

    /**
     * This method decreases the weights the edges within a cycle by the value of decrement.
     * If decrement is equal to the minimum weight of the edges within the cycle, then the cycle
     * is removed.
     * @param cycle the cycle to decrement.
     * @param decrement the value by which to decrement the edges.
     */

    private void decreaseCycle(List<N> cycle, float decrement) {
        N firstFrom = cycle.get(cycle.size() - 1);
        N firstTo = cycle.get(0);
        float firstEdgeWeight = forward.get(firstFrom).get(firstTo) - decrement;
        if (FloatUtils.floatEqual(firstEdgeWeight, 0f)) {
            forward.get(firstFrom).remove(firstTo);
            backward.get(firstTo).remove(firstFrom);
        } else {
            forward.get(firstFrom).put(firstTo, firstEdgeWeight);
            backward.get(firstTo).put(firstFrom, firstEdgeWeight);
        }
        for (int i = 0; i < cycle.size() - 1; i++) {
            N from = cycle.get(i);
            N to = cycle.get(i + 1);
            float edgeWeight = forward.get(from).get(to) - decrement;
            if (FloatUtils.floatEqual(edgeWeight, 0f)) {
                forward.get(from).remove(to);
                backward.get(to).remove(from);
            } else {
                forward.get(from).put(to, edgeWeight);
                backward.get(to).put(from, edgeWeight);
            }
        }
    }

    /**
     * returns true if and only if there is an edge from node @from to node @to.
     */

    @Override
    public boolean hasEdge(N from, N to) {
        if (!this.forward.containsKey(from)) return false;
        if (!this.forward.get(from).containsKey(to)) return false;
        return (!FloatUtils.floatEqual(this.forward.get(from).get(to), 0f));
    }

    @Override
    public Set<N> getForwardKeys() {
        return this.forward.keySet();
    }

    @Override
    public Set<N> getBackwardKeys() {
        return this.backward.keySet();
    }


}
