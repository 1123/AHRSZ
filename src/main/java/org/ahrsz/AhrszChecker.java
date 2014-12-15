package org.ahrsz;

public class AhrszChecker {

    public static <N extends Comparable<N>> void checkAhrsz(final AhrszAlgorithm<N> ahrsz)
            throws InvalidAhrszStateException {
        checkForward(ahrsz);
        checkBackward(ahrsz);
    }

    private static <N extends Comparable<N>> void checkForward(final AhrszAlgorithm<N> ahrsz)
            throws InvalidAhrszStateException {
        for (final N source : ahrsz.forwardKeys()) {
            for (final N sink : ahrsz.getF(source)) {
                if (ahrsz.node2Index.get(source) >= ahrsz.node2Index.get(sink))
                    throw new InvalidAhrszStateException();
            }
        }

    }

    private static <N extends Comparable<N>> void checkBackward(final AhrszAlgorithm<N> ahrsz)
            throws InvalidAhrszStateException {
        for (final N source : ahrsz.backwardKeys()) {
            for (final N sink : ahrsz.getB(source)) {
                if (ahrsz.node2Index.get(source) <= ahrsz.node2Index.get(sink))
                    throw new InvalidAhrszStateException();
            }
        }
    }

}
