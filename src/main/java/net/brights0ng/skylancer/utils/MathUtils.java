package net.brights0ng.skylancer.utils;

public class MathUtils {

    public static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }
}
