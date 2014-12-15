package org.ahrsz;


import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MinWeightHashMapGraph<N extends Comparable<N>> implements DirectedGraph<N> {

    private HashMap<N, HashMap<N, Float>> forward;
    private HashMap<N, HashMap<N, Float>> backward;

    float minWeight;

    public MinWeightHashMapGraph(float minWeight) {
        this.minWeight = minWeight;
        this.forward = new HashMap<>();
        this.backward = new HashMap<>();
    }

    /**
     * This method returns all outgoing edges together with its weights for a given node.
     * @param key the nodes for which to return the outgoing edges.
     * @return the outgoing edges as a map.
     */

    @Override
    public HashMap<N, Float> getF(N key) {
        HashMap<N, Float> result = new HashMap<>();
        HashMap<N, Float> candidates = this.forward.get(key);
        if (candidates != null) {
            candidates.keySet().stream().filter(
                    k -> candidates.get(k).compareTo(minWeight) > 0
            ).forEach(k -> result.put(k, this.forward.get(key).get(k)));
        }
        return result;
    }

    /**
     * Returns all the incoming edges for a node together with their weights.
     * @param key the node for which to return the incoming edges.
     * @return the incoming edges as a map.
     */

    @Override
    public HashMap<N, Float> getB(N key) {
        HashMap<N, Float> result = new HashMap<>();
        HashMap<N, Float> candidates = this.backward.get(key);
        if (candidates != null) {
            candidates.keySet().stream().filter(
                    k -> candidates.get(k).compareTo(minWeight) > 0
            ).forEach(k -> result.put(k, candidates.get(k)));
        }
        return result;
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
     * This method decreases the weights of the edges within a cycle by the value of decrement.
     * By decrementing the edges of a cycle, the decremented edges may become invisible to the outside
     * due to the minimum weight.
     *
     * @param cycle the cycle to decrement. This method check that the given list of nodes is in fact a cycle.
     * @param decrement the value by which to decrement the edges.
     */

    private void decreaseCycle(List<N> cycle, float decrement) {
        N firstFrom = cycle.get(cycle.size() - 1);
        N firstTo = cycle.get(0);
        float firstEdgeWeight = forward.get(firstFrom).get(firstTo) - decrement;
        forward.get(firstFrom).put(firstTo, firstEdgeWeight);
        backward.get(firstTo).put(firstFrom, firstEdgeWeight);
        for (int i = 0; i < cycle.size() - 1; i++) {
            N from = cycle.get(i);
            N to = cycle.get(i + 1);
            float edgeWeight = forward.get(from).get(to) - decrement;
            forward.get(from).put(to, edgeWeight);
            backward.get(to).put(from, edgeWeight);
        }
    }

    /**
     * returns true if and only if there is an edge from node @from to node @to with weight greater
     * than this.minWeight.
     */

    @Override
    public boolean hasEdge(N from, N to) {
        if (!this.forward.containsKey(from)) return false;
        if (!this.forward.get(from).containsKey(to)) return false;
        return (this.forward.get(from).get(to).compareTo(minWeight) > 0);
    }

    /**
     * Find all nodes that have at least one outgoing edge that is larger than minWeight.
     * This is used by algorithms that are only interested in edges with weight more than minWeight.
     * @return a set of nodes.
     */

    @Override
    public Set<N> getForwardKeys() {
        return this.forward.keySet().stream().filter(
            from -> this.forward.get(from).keySet().stream().anyMatch(
                to -> this.forward.get(from).get(to).compareTo(this.minWeight) > 0
            )
        ).collect(Collectors.toSet());
    }

    @Override
    public Set<N> getBackwardKeys() {
        return this.backward.keySet().stream().filter(
                to ->  this.backward.get(to).keySet().stream().anyMatch(
                        from -> this.backward.get(to).get(from).compareTo(this.minWeight) > 0
                )
        ).collect(Collectors.toSet());
    }

}
