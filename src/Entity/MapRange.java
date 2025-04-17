package Entity;

public class MapRange {
    
    /**
     * Linear interpolate on the scale given by a to b, using t as the point on that scale.
     * Examples:
     * 50 == lerp(0, 100, 0.5)
     * 4.2 == lerp(1, 5, 0.8)
     */
    public static float lerp(float a, float b, float t) {
        return (1 - t) * a + t * b;
    }
    
    /**
     * Inverse Linear Interpolation, get the fraction between a and b on which v resides.
     * Examples:
     * 0.5 == invLerp(0, 100, 50)
     * 0.8 == invLerp(1, 5, 4.2)
     */
    public static float invLerp(float a, float b, float v) {
        return (v - a) / (b - a);
    }
    
    /**
     * Remap values from one linear scale to another, a combination of lerp and invLerp.
     * iMin and iMax are the scale on which the original value resides,
     * oMin and oMax are the scale to which it should be mapped.
     * Examples:
     * 45 == remap(0, 100, 40, 50, 50)
     * 6.2 == remap(1, 5, 3, 7, 4.2)
     */
    public static float remap(float iMin, float iMax, float oMin, float oMax, float v) {
        return lerp(oMin, oMax, invLerp(iMin, iMax, v));
    }
}
