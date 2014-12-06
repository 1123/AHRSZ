package org.ahrsz;

public class FloatUtils {

    public static float MIN_WEIGHT = 0.0001f;

    public static boolean floatEqual(float first, float second) {
        return Math.abs(first - second) < MIN_WEIGHT;
    }

}
